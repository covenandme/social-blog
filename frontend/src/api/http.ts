import axios from 'axios';
import { useUserStore } from '../stores/user';

const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
});

http.interceptors.request.use((config) => {
  const store = useUserStore();
  if (store.token) {
    config.headers.Authorization = `Bearer ${store.token}`;
  }
  return config;
});

http.interceptors.response.use(
  (response) => {
    const { code, data, msg } = response.data;
    if (code === 0) {
      return data;
    }
    return Promise.reject(new Error(msg || '请求失败'));
  },
  (error) => {
    if (error.response?.status === 401) {
      const store = useUserStore();
      store.logout();
    }
    return Promise.reject(error);
  }
);

export default http;

