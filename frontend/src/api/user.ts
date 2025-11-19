import http from './http';
import type { UserProfile } from '../types/user';

export interface UpdateProfilePayload {
  avatarUrl?: string;
  bio?: string;
}

export const updateProfile = (payload: UpdateProfilePayload) =>
  http.put<UserProfile>('/users/me', payload);

