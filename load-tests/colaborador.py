import time
from locust import HttpUser, task, between

class ColaboradorUser(HttpUser):
    wait_time = between(1, 2)

    def on_start(self):
        """
        Executado uma vez por usuário virtual.
        Realiza login e cria o Cargo de apoio necessário.
        """
        # 1. Login
        res_login = self.client.post("/api/auth/login", json={
            "email": "teste-carga@floricultura.com",
            "senha": "teste-carga"
        })
        
        if res_login.status_code == 200:
            self.token = res_login.json().get("token")
            self.headers = {"Authorization": f"Bearer {self.token}"}
            
            # 2. Criar Cargo de Apoio
            # Usamos um sufixo para o nome do cargo não repetir entre usuários
            sufixo_cargo = str(int(time.time() * 1000))[-5:]
            payload_cargo = {
                "nomeCargo": f"Cargo Teste {sufixo_cargo}",
                "descricao": "Cargo gerado para suporte ao teste de colaboradores"
            }
            
            res_cargo = self.client.post("/api/cargos", json=payload_cargo, headers=self.headers)
            
            if res_cargo.status_code in [200, 201]:
                # Armazena o ID do cargo (verifique se na sua API é 'id' ou 'idCargo')
                self.cargo_id = res_cargo.json().get("id") or res_cargo.json().get("idCargo")
            else:
                self.cargo_id = None
                print(f"ERRO AO CRIAR CARGO DE APOIO: {res_cargo.status_code}")
        else:
            self.token = None
            print(f"FALHA NO LOGIN: {res_login.status_code}")

    @task
    def fluxo_colaborador(self):
        if not self.token or not self.cargo_id:
            return

        # Gerar valores únicos para cada execução da task
        # Microssegundos garantem que matricula e cpf sejam únicos
        semente_unica = str(int(time.time() * 1000000))
        
        matricula = semente_unica[-6:] # Pega os últimos 6 dígitos
        cpf_unico = semente_unica[-11:] # Pega os últimos 11 dígitos

        payload_colaborador = {
            "matricula": matricula,
            "nome": f"Colaborador {matricula}",
            "cpf": cpf_unico,
            "salario": 3500.00,
            "cargo": { "id": self.cargo_id }
        }

        # --- REQUISITO: POST (Criação) ---
        with self.client.post("/api/colaboradores", json=payload_colaborador, headers=self.headers, catch_response=True) as response:
            if response.status_code in [200, 201]:
                try:
                    # Captura o ID do colaborador criado para o GET
                    colab_id = response.json().get("id") or response.json().get("idColaborador")
                    
                    if colab_id:
                        # --- REQUISITO: GET (Leitura) ---
                        res_get = self.client.get(
                            f"/api/colaboradores/{colab_id}", 
                            headers=self.headers, 
                            name="/api/colaboradores/[id]"
                        )
                        if res_get.status_code != 200:
                            response.failure(f"GET falhou: Status {res_get.status_code}")
                    else:
                        response.failure("ID do colaborador não retornado no POST")
                except Exception as e:
                    response.failure(f"Erro ao processar JSON: {e}")
            else:
                # Se o erro for 400 ou 500, mostra o motivo no Locust
                response.failure(f"POST falhou: {response.status_code} - {response.text}")