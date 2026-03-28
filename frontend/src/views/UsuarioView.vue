<template>
  <div class="card">
    <div class="card-header">
      <h2 class="card-title">Usuários</h2>
      <button class="btn btn-primary" @click="showForm = true">Novo Usuário</button>
    </div>

    <!-- Tabela de Listagem -->
    <div class="table-wrapper" v-if="!showForm">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th class="text-right">Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in usuarios" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.email }}</td>
            <td class="text-right flex justify-end gap-2">
              <button class="btn btn-danger" @click="remove(user.id)">Excluir</button>
            </td>
          </tr>
          <tr v-if="usuarios.length === 0">
            <td colspan="3" class="text-center">Nenhum usuário encontrado.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Formulário -->
    <div v-if="showForm" class="form-container">
      <h3>Novo Usuário</h3>
      <form @submit.prevent="save" class="mt-4">
        <div class="form-group">
          <label>Email</label>
          <input type="email" v-model="form.email" class="form-control" required />
        </div>
        <div class="form-group">
          <label>Senha</label>
          <input type="password" v-model="form.senha" class="form-control" required />
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

const usuarios = ref([]);
const showForm = ref(false);
const form = ref({ email: '', senha: '' });

const fetchUsuarios = async () => {
  try {
    const res = await api.get('/usuarios');
    usuarios.value = res.data;
  } catch (error) {
    console.error('Erro ao buscar usuários:', error);
  }
};

const save = async () => {
  try {
    await api.post('/usuarios', form.value);
    await fetchUsuarios();
    cancelForm();
  } catch (error) {
    console.error('Erro ao salvar:', error);
    alert('Erro ao salvar usuário');
  }
};

const remove = async (id) => {
  if (confirm('Deseja realmente excluir este usuário?')) {
    try {
      await api.delete(`/usuarios/${id}`);
      await fetchUsuarios();
    } catch (error) {
      console.error('Erro ao excluir:', error);
      alert('Erro ao excluir usuário (pode estar em uso)');
    }
  }
};

const cancelForm = () => {
  form.value = { email: '', senha: '' };
  showForm.value = false;
};

onMounted(() => {
  fetchUsuarios();
});
</script>

<style scoped>
.form-container {
  max-width: 600px;
  margin: 0 auto;
}
</style>
