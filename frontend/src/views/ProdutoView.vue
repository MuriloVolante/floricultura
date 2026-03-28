<template>
  <div class="card">
    <div class="card-header">
      <h2 class="card-title">Produtos</h2>
      <button class="btn btn-primary" @click="openForm">Novo Produto</button>
    </div>

    <!-- Tabela de Listagem -->
    <div class="table-wrapper" v-if="!showForm">
      <table class="table">
        <thead>
          <tr>
            <th>Descrição</th>
            <th>Fornecedor</th>
            <th>Estoque</th>
            <th>Valor (R$)</th>
            <th>Validade</th>
            <th class="text-right">Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="prod in produtos" :key="prod.id">
            <td>{{ prod.descricao }}</td>
            <td>{{ prod.fornecedor?.nome }}</td>
            <td>{{ prod.quantidadeEstoque }}</td>
            <td>{{ prod.valorVenda }}</td>
            <td>{{ prod.validade }}</td>
            <td class="text-right flex justify-end gap-2">
              <button class="btn btn-secondary" @click="edit(prod)">Gerenciar</button>
              <button class="btn btn-danger" @click="remove(prod.id)">Excluir</button>
            </td>
          </tr>
          <tr v-if="produtos.length === 0">
            <td colspan="6" class="text-center">Nenhum produto encontrado.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Formulário e Abas -->
    <div v-if="showForm" class="form-container">
      <div class="flex justify-between items-center mb-4 border-b">
        <h3>{{ form.id ? 'Gerenciar Produto: ' + form.descricao : 'Novo Produto' }}</h3>
        <button type="button" class="btn btn-secondary" @click="cancelForm">Voltar</button>
      </div>

      <div class="tabs flex gap-4 mb-4" v-if="form.id">
        <button class="btn btn-secondary" :class="{ 'btn-primary': activeTab === 'dados' }" @click="activeTab = 'dados'">Dados Gerais</button>
        <button class="btn btn-secondary" :class="{ 'btn-primary': activeTab === 'estoque' }" @click="activeTab = 'estoque'">Movimentações</button>
      </div>

      <!-- ABA: Dados do Produto -->
      <form v-if="activeTab === 'dados' || !form.id" @submit.prevent="save" class="mt-4">
        <div class="form-group">
          <label>Descrição</label>
          <input type="text" v-model="form.descricao" class="form-control" required />
        </div>
        
        <div class="form-row">
          <div class="form-group">
            <label>Valor de Venda</label>
            <input type="number" step="0.01" v-model="form.valorVenda" class="form-control" required />
          </div>
          <div class="form-group">
            <label>Quantidade em Estoque</label>
            <input type="number" v-model="form.quantidadeEstoque" class="form-control" required />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Fornecedor</label>
            <select v-model="form.fornecedorId" class="form-control" required>
              <option value="" disabled>Selecione</option>
              <option v-for="f in fornecedores" :value="f.id" :key="f.id">{{ f.nome }}</option>
            </select>
          </div>
          <div class="form-group">
            <label>Validade</label>
            <input type="date" v-model="form.validade" class="form-control" />
          </div>
        </div>

        <div class="flex justify-end mt-4">
          <button type="submit" class="btn btn-success">Salvar Produto</button>
        </div>
      </form>

      <!-- ABA: Movimentação de Estoque e Registro de Pedido -->
      <div v-if="activeTab === 'estoque' && form.id">
        <div class="card" style="box-shadow: none; border: 1px solid var(--border-color);">
          <h4>Nova Movimentação (Entrada/Saída)</h4>
          <form @submit.prevent="saveMovimentacao" class="form-row items-center mt-2">
            <div class="form-group mb-0">
              <label>Tipo</label>
              <select v-model="movForm.tipo" class="form-control" required>
                <option value="ENTRADA">Entrada</option>
                <option value="SAIDA">Saída</option>
              </select>
            </div>
            <div class="form-group mb-0">
              <label>Quantidade</label>
              <input type="number" v-model="movForm.quantidade" class="form-control" required min="1" />
            </div>
            <div class="flex items-center gap-2" style="padding-top: 1.5rem">
              <button type="submit" class="btn btn-primary">Registrar Estoque</button>
              <button type="button" @click="criarPedidoSimulado" class="btn btn-success">Gerar Pedido Deste Item</button>
            </div>
          </form>
        </div>

        <div class="mt-4">
          <h4>Histórico (Últimos Carregamentos Listados Globalmente)</h4>
          <p class="text-muted mt-2 text-sm">
            Para ver o histórico acesse a API de movimentações ou implemente um fetch custom com filtro de produto.
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '../services/api';

const produtos = ref([]);
const fornecedores = ref([]);
const showForm = ref(false);
const activeTab = ref('dados');

const baseForm = { id: null, descricao: '', valorVenda: '', quantidadeEstoque: 0, fornecedorId: '', validade: '' };
const form = ref({ ...baseForm });

const movForm = ref({ tipo: 'ENTRADA', quantidade: 1 });

const fetchData = async () => {
  try {
    const [prodRes, fornRes] = await Promise.all([
      api.get('/produtos'),
      api.get('/fornecedores')
    ]);
    produtos.value = prodRes.data;
    fornecedores.value = fornRes.data;
  } catch (error) {
    console.error('Erro ao buscar dados:', error);
  }
};

const openForm = () => {
  form.value = { ...baseForm };
  activeTab.value = 'dados';
  showForm.value = true;
};

const save = async () => {
  try {
    const payload = {
      id: form.value.id,
      descricao: form.value.descricao,
      valorVenda: form.value.valorVenda,
      quantidadeEstoque: form.value.quantidadeEstoque,
      validade: form.value.validade,
      fornecedor: form.value.fornecedorId ? { id: form.value.fornecedorId } : null
    };

    await api.post('/produtos', payload);
    await fetchData();
    cancelForm();
  } catch (error) {
    console.error('Erro ao salvar:', error);
    alert('Erro ao salvar produto.');
  }
};

const saveMovimentacao = async () => {
  try {
    // A API MovimentacaoEstoqueController precisa do Produto
    const payload = {
      tipo: movForm.value.tipo,
      quantidade: movForm.value.quantidade,
      produto: { id: form.value.id },
      dataHora: new Date().toISOString()
    };
    await api.post('/movimentacoes-estoque', payload);
    alert('Estoque atualizado!');
    movForm.value.quantidade = 1;
    await fetchData();
  } catch (error) {
    console.error('Erro de Estoque', error);
    alert('Erro ao movimentar estoque.');
  }
};

const criarPedidoSimulado = async () => {
  try {
    // Simulando criar um pedido rápido com 1 item deste produto para o primeiro cliente que existir (simplificação)
    // Buscando cliente
    const cliRes = await api.get('/clientes');
    let clienteId = cliRes.data[0]?.id;
    
    if (!clienteId) {
       // Se não tem cliente, enviamos sem cliente ou disparamos erro - requer Cliente.
       alert('Você precisa ter pelo menos um Cliente cadastrado no banco para gerar o Pedido automático.');
       return;
    }

    const pedidoPayload = {
      dataPedido: new Date().toISOString().split('T')[0],
      status: 'CONCLUIDO',
      valorTotal: form.value.valorVenda * movForm.value.quantidade,
      cliente: { id: clienteId },
      itens: [
        {
          quantidade: movForm.value.quantidade,
          valorUnitario: form.value.valorVenda,
          produto: { id: form.value.id }
        }
      ]
    };

    await api.post('/pedidos', pedidoPayload);
    alert('Pedido criado com sucesso para este produto!');
  } catch(e) {
    console.error('Pedido error', e);
    alert('Erro ao gerar pedido simulação.');
  }
};

const edit = (prod) => {
  form.value = {
    id: prod.id,
    descricao: prod.descricao,
    valorVenda: prod.valorVenda,
    quantidadeEstoque: prod.quantidadeEstoque,
    validade: prod.validade,
    fornecedorId: prod.fornecedor?.id || ''
  };
  activeTab.value = 'dados';
  showForm.value = true;
};

const remove = async (id) => {
  if (confirm('Deseja excluir este produto?')) {
    try {
      await api.delete(`/produtos/${id}`);
      await fetchData();
    } catch (error) {
      alert('Erro ao excluir. Pode estar vinculado a pedidos.');
    }
  }
};

const cancelForm = () => {
  form.value = { ...baseForm };
  showForm.value = false;
};

onMounted(() => {
  fetchData();
});
</script>

<style scoped>
.form-container {
  max-width: 900px;
  margin: 0 auto;
}
.border-b {
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 1rem;
}
.text-sm { font-size: 0.875rem; color: var(--text-muted); }
</style>
