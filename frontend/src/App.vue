<template>
  <div class="app-container">
    <nav v-if="isAuthenticated" class="navbar">
      <div class="navbar-brand">Floricultura ERP</div>
      <ul class="navbar-nav">
        <li><router-link to="/">Home</router-link></li>
        <li><router-link to="/cargos">Cargos</router-link></li>
        <li><router-link to="/usuarios">Usuários</router-link></li>
        <li><router-link to="/fornecedores">Fornecedores</router-link></li>
        <li><router-link to="/colaboradores">Colaboradores</router-link></li>
        <li><router-link to="/produtos">Produtos</router-link></li>
        <li><a href="#" @click.prevent="logout" class="logout-btn">Sair</a></li>
      </ul>
    </nav>
    <main class="main-content">
      <router-view @login-success="checkAuth" />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const isAuthenticated = ref(false);
const router = useRouter();

const checkAuth = () => {
  isAuthenticated.value = !!localStorage.getItem('token');
};

const logout = () => {
  localStorage.removeItem('token');
  isAuthenticated.value = false;
  router.push('/login');
};

onMounted(() => {
  checkAuth();
});
</script>

<style>
/* Estilos aplicados no style.css globalmente. Aqui deixamos sem scoped para estrutura. */
</style>
