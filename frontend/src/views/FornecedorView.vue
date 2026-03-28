<template>
  <div class="card">
    <div class="card-header">
      <h2 class="card-title">Fornecedores</h2>
      <button class="btn btn-primary" @click="showForm = true">Novo Fornecedor</button>
    </div>

    <!-- Tabela de Listagem -->
    <div class="table-wrapper" v-if="!showForm">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome / Razão Social</th>
            <th>CNPJ</th>
            <th>Telefone</th>
            <th class="text-right">Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="forn in fornecedores" :key="forn.id">
            <td>{{ forn.id }}</td>
            <td>{{ forn.nome }}</td>
            <td>{{ forn.cnpj }}</td>
            <td>{{ forn.telefone }}</td>
            <td class="text-right flex justify-end gap-2">
              <button class="btn btn-secondary" @click="edit(forn)">Editar</button>
              <button class="btn btn-danger" @click="remove(forn.id)">Excluir</button>
            </td>
          </tr>
          <tr v-if="fornecedores.length === 0">
            <td colspan="5" class="text-center">Nenhum fornecedor encontrado.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Formulário -->
    <div v-if="showForm" class="form-container">
      <h3>{{ form.id ? 'Editar Fornecedor' : 'Novo Fornecedor' }}</h3>
      <form @submit.prevent="save" class="mt-4">
        
        <div class="form-row">
          <div class="form-group">
            <label>Nome / Razão Social</label>
            <input type="text" v-model="form.nome" class="form-control" required />
          </div>
          <div class="form-group">
            <label>CNPJ (Apenas números)</label>
            <input type="text" v-model="form.cnpj" class="form-control" maxlength="14" required />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Telefone</label>
            <input type="text" v-model="form.telefone" class="form-control" maxlength="15" />
          </div>
          <div class="form-group">
            <label>CEP</label>
            <input type="text" v-model="form.cep" class="form-control" maxlength="8" />
          </div>
        </div>

        <div class="form-group">
          <label>Endereço</label>
          <input type="text" v-model="form.endereco" class="form-control" />
        </div>

        <div class="flex justify-end gap-2 mt-4">
          <button type="button" class="btn btn-secondary" @click="cancelForm">Cancelar</button>
          <button type="submit" class="btn btn-success">Salvar</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '../services/api';

const fornecedores = ref([]);
const showForm = ref(false);
const form = ref({ id: null, nome: '', cnpj: '', telefone: '', cep: '', endereco: '' });

const fetchFornecedores = async () => {
  try {
    const res = await api.get('/fornecedores');
    fornecedores.value = res.data;
  } catch (error) {
    console.error('Erro ao buscar fornecedores:', error);
  }
};

const save = async () => {
  try {
    if (form.value.id) {
      await api.post('/fornecedores', form.value);
    } else {
      await api.post('/fornecedores', form.value);
    }
    await fetchFornecedores();
    cancelForm();
  } catch (error) {
    console.error('Erro ao salvar:', error);
    alert('Erro ao salvar fornecedor verifique se CNPJ já existe.');
  }
};

const edit = (forn) => {
  form.value = { ...forn };
  showForm.value = true;
};

const remove = async (id) => {
  if (confirm('Deseja excluir este fornecedor?')) {
    try {
      await api.delete(`/fornecedores/${id}`);
      await fetchFornecedores();
    } catch (error) {
      console.error('Erro ao excluir:', error);
      alert('Erro. Pode estar associado a produtos.');
    }
  }
};

const cancelForm = () => {
  form.value = { id: null, nome: '', cnpj: '', telefone: '', cep: '', endereco: '' };
  showForm.value = false;
};

onMounted(() => {
  fetchFornecedores();
});
</script>

<style scoped>
.form-container {
  max-width: 800px;
  margin: 0 auto;
}
</style>
