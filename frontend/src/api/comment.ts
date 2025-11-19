import http from './http';

export interface CommentPayload {
  parentId?: number;
  content: string;
}

export interface CommentNode {
  id: number;
  postId: number;
  parentId?: number;
  content: string;
  createdAt: string;
  author: {
    id: number;
    username: string;
    avatarUrl: string;
  };
  replies: CommentNode[];
}

export const fetchComments = (postId: number) =>
  http.get<CommentNode[]>(`/posts/${postId}/comments`);

export const createComment = (postId: number, payload: CommentPayload) =>
  http.post<CommentNode>(`/posts/${postId}/comments`, payload);

