# 🌸 ERP Floricultura

Este é um sistema de gerenciamento de recursos (ERP) completo, desenhado especificamente para atender às necessidades de uma floricultura. O projeto conta com uma arquitetura robusta, separando as responsabilidades entre um backend de alta performance e um frontend moderno e responsivo.

---

## 🚀 Tecnologias e Destaques

- **Backend:** Desenvolvido em Java com Spring Boot.
  - **Segurança:** Implementação de autenticação via Bearer Token (JWT).
  - **Confiabilidade:** Backend 100% funcional com lógica de negócio para controle de estoque, vendas e clientes.
  - **Performance:** Validado através de testes de carga rigorosos para garantir estabilidade sob alta demanda.
- **Frontend:** Interface moderna construída com Vue.js e Vite.
- **Testes:** Pasta dedicada a scripts de teste de carga (`load-tests`).

---

## 🛠️ Como Iniciar o Projeto

Siga os passos abaixo para configurar o ambiente em sua máquina local.

### 1. Clonar o Repositório

No terminal de sua preferência (ou diretamente pelo IntelliJ):

```bash
git clone https://github.com/seu-usuario/nome-do-repositorio.git
```

### 2. Configurar o Backend (Java / Spring Boot)

1. Abra o **IntelliJ IDEA**.
2. Vá em `File > Open` e selecione a pasta raiz do projeto.
3. Aguarde o IntelliJ importar as dependências do Gradle (conforme a pasta `gradle/wrapper`).
4. Localize o arquivo principal em:
   `src/main/java/br/com/floricultura/erp/ErpApplication.java`
5. Clique com o botão direito no arquivo e selecione **Run 'ErpApplication'**.
   - O backend subirá por padrão na porta **8080**.

### 3. Configurar o Frontend (Vue.js / Vite)

Agora, vamos colocar a interface para funcionar:

1. Abra um terminal e navegue até a pasta do frontend:

```bash
cd frontend
```

2. Instale as dependências necessárias:

```bash
npm install
```

3. Inicie o servidor de desenvolvimento:

```bash
npm run dev
```

- O terminal informará a URL (geralmente `http://localhost:5173`) para acessar o sistema.

---

## 📂 Estrutura de Pastas Principal

```plaintext
├── frontend            # Aplicação Vue.js (Vite, Axios, Router)
├── load-tests          # Scripts de teste de carga e performance
├── src/main/java       # Código fonte do Backend Spring Boot
│   └── controller      # Endpoints da API
│   └── model           # Entidades do banco de dados
│   └── security        # Configurações de autenticação Bearer/JWT
│   └── services        # Regras de negócio
└── src/test            # Testes unitários e de integração
```

---

## 🔐 Autenticação

O sistema utiliza **Bearer Authentication**. Para acessar rotas protegidas:

1. Realize o login via endpoint de autenticação.
2. Capture o token JWT retornado.
3. Inclua o token no Header das requisições:

```
Authorization: Bearer <seu_token_aqui>
```

---

## 📈 Testes de Carga

O projeto inclui uma suíte de testes na pasta `load-tests`. Eles foram fundamentais para garantir que o ERP suporte o fluxo de vendas em datas comemorativas de alta demanda, como o **Dia dos Namorados**.

---

*Desenvolvido por Murilo Volante 🌿*
