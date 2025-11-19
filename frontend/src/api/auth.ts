import http from './http';
import type { UserProfile } from '../types/user';

export const login = (payload: { username: string; password: string }) =>
  http.post<{ token: string; profile: UserProfile }>('/auth/login', payload);

export const register = (payload: { username: string; password: string }) =>
  http.post<UserProfile>('/auth/register', payload);

export const fetchCurrent = () => http.get<UserProfile>('/users/me');

