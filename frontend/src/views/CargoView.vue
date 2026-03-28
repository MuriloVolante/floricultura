<template>
  <div class="card">
    <div class="card-header">
      <h2 class="card-title">Cargos</h2>
      <button class="btn btn-primary" @click="showForm = true">Novo Cargo</button>
    </div>

    <!-- Tabela de Listagem -->
    <div class="table-wrapper" v-if="!showForm">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Descrição</th>
            <th class="text-right">Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cargo in cargos" :key="cargo.id">
            <td>{{ cargo.id }}</td>
            <td>{{ cargo.nomeCargo }}</td>
            <td>{{ cargo.descricao }}</td>
            <td class="text-right flex justify-end gap-2">
              <button class="btn btn-secondary" @click="edit(cargo)">Editar</button>
              <button class="btn btn-danger" @click="remove(cargo.id)">Excluir</button>
            </td>
          </tr>
          <tr v-if="cargos.length === 0">
            <td colspan="4" class="text-center">Nenhum cargo encontrado.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Formulário -->
    <div v-if="showForm" class="form-container">
      <h3>{{ form.id ? 'Editar Cargo' : 'Novo Cargo' }}</h3>
      <form @submit.prevent="save" class="mt-4">
        <div class="form-group">
          <label>Nome do Cargo</label>
          <input type="text" v-model="form.nomeCargo" class="form-control" required />
        </div>
        <div class="form-group">
          <label>Descrição</label>
          <textarea v-model="form.descricao" class="form-control" rows="3"></textarea>
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

const cargos = ref([]);
const showForm = ref(false);
const form = ref({ id: null, nomeCargo: '', descricao: '' });

const fetchCargos = async () => {
  try {
    const res = await api.get('/cargos');
    cargos.value = res.data;
  } catch (error) {
    console.error('Erro ao buscar cargos:', error);
  }
};

const save = async () => {
  try {
    if (form.value.id) {
      // API não separou PUT, salvar faz o merge se tiver ID?
      // Pelo Java @PostMapping salva. Se for JPA, save() com id faz update.
      await api.post('/cargos', form.value);
    } else {
      await api.post('/cargos', form.value);
    }
    await fetchCargos();
    cancelForm();
  } catch (error) {
    console.error('Erro ao salvar:', error);
    alert('Erro ao salvar cargo');
  }
};

const edit = (cargo) => {
  form.value = { ...cargo };
  showForm.value = true;
};

const remove = async (id) => {
  if (confirm('Deseja realmente excluir este cargo?')) {
    try {
      await api.delete(`/cargos/${id}`);
      await fetchCargos();
    } catch (error) {
      console.error('Erro ao excluir:', error);
      alert('Erro ao excluir cargo (pode estar em uso)');
    }
  }
};

const cancelForm = () => {
  form.value = { id: null, nomeCargo: '', descricao: '' };
  showForm.value = false;
};

onMounted(() => {
  fetchCargos();
});
</script>

<style scoped>
.form-container {
  max-width: 600px;
  margin: 0 auto;
}
</style>
