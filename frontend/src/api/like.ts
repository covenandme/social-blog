import http from './http';

export const likePost = (postId: number) => http.post<void>(`/posts/${postId}/likes`);

export const cancelLike = (postId: number) => http.delete<void>(`/posts/${postId}/likes`);

