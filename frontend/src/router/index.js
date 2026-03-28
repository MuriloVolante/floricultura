import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import HomeView from '../views/HomeView.vue';
import CargoView from '../views/CargoView.vue';
import UsuarioView from '../views/UsuarioView.vue';
import FornecedorView from '../views/FornecedorView.vue';
import ColaboradorView from '../views/ColaboradorView.vue';
import ProdutoView from '../views/ProdutoView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true }
    },
    {
      path: '/cargos',
      name: 'cargos',
      component: CargoView,
      meta: { requiresAuth: true }
    },
    {
      path: '/usuarios',
      name: 'usuarios',
      component: UsuarioView,
      meta: { requiresAuth: true }
    },
    {
      path: '/fornecedores',
      name: 'fornecedores',
      component: FornecedorView,
      meta: { requiresAuth: true }
    },
    {
      path: '/colaboradores',
      name: 'colaboradores',
      component: ColaboradorView,
      meta: { requiresAuth: true }
    },
    {
      path: '/produtos',
      name: 'produtos',
      component: ProdutoView,
      meta: { requiresAuth: true }
    }
  ]
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  if (to.meta.requiresAuth && !token) {
    next('/login');
  } else {
    next();
  }
});

export default router;
