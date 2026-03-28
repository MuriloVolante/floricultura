<template>
  <div class="login-wrapper">
    <div class="card login-card">
      <h2 class="card-title text-center mb-4">Acesso ao ERP</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>E-mail</label>
          <input type="email" v-model="email" class="form-control" required />
        </div>
        <div class="form-group">
          <label>Senha</label>
          <input type="password" v-model="senha" class="form-control" required />
        </div>
        <div v-if="error" class="error-msg mb-4">{{ error }}</div>
        <button type="submit" class="btn btn-primary w-100" :disabled="loading">
          {{ loading ? 'Entrando...' : 'Entrar' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '../services/api';

const email = ref('');
const senha = ref('');
const error = ref('');
const loading = ref(false);
const router = useRouter();

const emit = defineEmits(['login-success']);

const handleLogin = async () => {
  error.value = '';
  loading.value = true;
  try {
    const response = await api.post('/auth/login', {
      email: email.value,
      senha: senha.value
    });
    localStorage.setItem('token', response.data.token);
    emit('login-success');
    router.push('/');
  } catch (err) {
    error.value = 'Credenciais inválidas. Verifique seu e-mail e senha.';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 100px);
}
.login-card {
  width: 100%;
  max-width: 400px;
}
.w-100 {
  width: 100%;
}
.error-msg {
  color: var(--danger-color);
  font-size: 0.875rem;
  text-align: center;
}
</style>
