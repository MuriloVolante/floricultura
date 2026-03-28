<template>
  <div class="card">
    <div class="card-header">
      <h2 class="card-title">Colaboradores</h2>
      <button class="btn btn-primary" @click="openForm">Novo Colaborador</button>
    </div>

    <!-- Tabela de Listagem -->
    <div class="table-wrapper" v-if="!showForm">
      <table class="table">
        <thead>
          <tr>
            <th>Matrícula</th>
            <th>Nome</th>
            <th>CPF</th>
            <th>Cargo</th>
            <th class="text-right">Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="colab in colaboradores" :key="colab.id">
            <td>{{ colab.matricula }}</td>
            <td>{{ colab.nome }}</td>
            <td>{{ colab.cpf }}</td>
            <td>{{ colab.cargo?.nomeCargo }}</td>
            <td class="text-right flex justify-end gap-2">
              <button class="btn btn-secondary" @click="edit(colab)">Editar</button>
              <button class="btn btn-danger" @click="remove(colab.id)">Excluir</button>
            </td>
          </tr>
          <tr v-if="colaboradores.length === 0">
            <td colspan="5" class="text-center">Nenhum colaborador encontrado.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Formulário -->
    <div v-if="showForm" class="form-container">
      <h3>{{ form.id ? 'Editar Colaborador' : 'Novo Colaborador' }}</h3>
      <form @submit.prevent="save" class="mt-4">
        
        <div class="form-row">
          <div class="form-group">
            <label>Nome Completo</label>
            <input type="text" v-model="form.nome" class="form-control" required />
          </div>
          <div class="form-group">
            <label>CPF</label>
            <input type="text" v-model="form.cpf" class="form-control" required />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Matrícula</label>
            <input type="text" v-model="form.matricula" class="form-control" required />
          </div>
          <div class="form-group">
            <label>Salário</label>
            <input type="number" step="0.01" v-model="form.salario" class="form-control" required />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Cargo</label>
            <select v-model="form.cargoId" class="form-control" required>
              <option value="" disabled>Selecione um Cargo</option>
              <option v-for="c in cargos" :value="c.id" :key="c.id">{{ c.nomeCargo }}</option>
            </select>
          </div>
          <div class="form-group">
            <label>Usuário Vinculado (Login)</label>
            <select v-model="form.usuarioId" class="form-control" required>
              <option value="" disabled>Selecione um Usuário</option>
              <option v-for="u in usuarios" :value="u.id" :key="u.id">{{ u.email }}</option>
            </select>
          </div>
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

const colaboradores = ref([]);
const cargos = ref([]);
const usuarios = ref([]);
const showForm = ref(false);

const baseForm = { id: null, matricula: '', nome: '', cpf: '', salario: '', cargoId: '', usuarioId: '' };
const form = ref({ ...baseForm });

const fetchData = async () => {
  try {
    const [colabRes, cargoRes, usuRes] = await Promise.all([
      api.get('/colaboradores'),
      api.get('/cargos'),
      api.get('/usuarios')
    ]);
    colaboradores.value = colabRes.data;
    cargos.value = cargoRes.data;
    usuarios.value = usuRes.data;
  } catch (error) {
    console.error('Erro ao buscar dados:', error);
  }
};

const openForm = () => {
  form.value = { ...baseForm };
  showForm.value = true;
};

const save = async () => {
  try {
    const payload = {
      id: form.value.id,
      matricula: form.value.matricula,
      nome: form.value.nome,
      cpf: form.value.cpf,
      salario: form.value.salario,
      cargo: form.value.cargoId ? { id: form.value.cargoId } : null,
      usuario: form.value.usuarioId ? { id: form.value.usuarioId } : null
    };

    await api.post('/colaboradores', payload);
    await fetchData();
    cancelForm();
  } catch (error) {
    console.error('Erro ao salvar:', error);
    alert('Erro ao salvar colaborador. Verifique se CPF ou Usuário já estão em uso.');
  }
};

const edit = (colab) => {
  form.value = {
    id: colab.id,
    matricula: colab.matricula,
    nome: colab.nome,
    cpf: colab.cpf,
    salario: colab.salario,
    cargoId: colab.cargo?.id || '',
    usuarioId: colab.usuario?.id || ''
  };
  showForm.value = true;
};

const remove = async (id) => {
  if (confirm('Deseja excluir este colaborador?')) {
    try {
      await api.delete(`/colaboradores/${id}`);
      await fetchData();
    } catch (error) {
      console.error('Erro ao excluir:', error);
      alert('Erro ao excluir colaborador.');
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
  max-width: 800px;
  margin: 0 auto;
}
</style>
