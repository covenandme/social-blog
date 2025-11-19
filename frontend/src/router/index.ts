import { createRouter, createWebHistory } from 'vue-router';
import FeedView from '../views/FeedView.vue';
import ProfileView from '../views/ProfileView.vue';
import AuthView from '../views/AuthView.vue';
import { useUserStore } from '../stores/user';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'feed', component: FeedView },
    { path: '/profile/:id', name: 'profile', component: ProfileView, props: true },
    { path: '/auth', name: 'auth', component: AuthView }
  ]
});

router.beforeEach((to, _from, next) => {
  const store = useUserStore();
  if (!store.token && to.name !== 'auth') {
    return next({ name: 'auth' });
  }
  if (store.token && to.name === 'auth') {
    return next({ name: 'feed' });
  }
  next();
});

export default router;

