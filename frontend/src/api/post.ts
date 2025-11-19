import http from './http';
import type { Post, PostPage } from '../types/post';

export const fetchPosts = (page: number, size: number, userId?: number) =>
  http.get<PostPage>('/posts', { params: { page, size, userId } });

export const createPost = (payload: {
  content: string;
  markdown: boolean;
  images: string[];
  visibility: 'PUBLIC' | 'PRIVATE';
}) => http.post<Post>('/posts', payload);

