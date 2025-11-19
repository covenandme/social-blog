import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { UserProfile } from '../types/user';
import { login, register, fetchCurrent } from '../api/auth';
import { updateProfile, type UpdateProfilePayload } from '../api/user';

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(localStorage.getItem('token'));
  const profile = ref<UserProfile | null>(null);

  const isLogin = computed(() => Boolean(token.value));

  function setToken(value: string | null) {
    token.value = value;
    if (value) {
      localStorage.setItem('token', value);
    } else {
      localStorage.removeItem('token');
    }
  }

  async function loginByPassword(username: string, password: string) {
    const res = await login({ username, password });
    setToken(res.token);
    profile.value = res.profile;
  }

  async function registerAccount(username: string, password: string) {
    await register({ username, password });
    await loginByPassword(username, password);
  }

  async function loadProfile() {
    if (!token.value) return;
    profile.value = await fetchCurrent();
  }

  async function updateProfileInfo(payload: UpdateProfilePayload) {
    const res = await updateProfile(payload);
    profile.value = res;
  }

  function logout() {
    setToken(null);
    profile.value = null;
  }

  return {
    token,
    profile,
    isLogin,
    loginByPassword,
    registerAccount,
    loadProfile,
    updateProfileInfo,
    logout
  };
});

