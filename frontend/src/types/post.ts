import type { UserProfile } from './user';

export interface Post {
  id: number;
  content: string;
  markdown: boolean;
  images: string[];
  visibility: 'PUBLIC' | 'PRIVATE';
  likeCount: number;
  commentCount: number;
  createdAt: string;
  updatedAt: string;
  liked: boolean;
  author: Pick<UserProfile, 'id' | 'username' | 'avatarUrl'>;
}

export interface PostPage {
  total: number;
  page: number;
  size: number;
  records: Post[];
}

