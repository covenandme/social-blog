import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { Post } from '../types/post';
import { fetchPosts, createPost } from '../api/post';

export const usePostStore = defineStore('post', () => {
  const posts = ref<Post[]>([]);
  const page = ref(1);
  const hasMore = ref(true);
  const loading = ref(false);

  async function loadNext(userId?: number) {
    if (!hasMore.value || loading.value) return;
    loading.value = true;
    try {
      const res = await fetchPosts(page.value, 10, userId);
      if (page.value === 1) {
        posts.value = res.records;
      } else {
        posts.value = [...posts.value, ...res.records];
      }
      hasMore.value = posts.value.length < res.total;
      if (hasMore.value) {
        page.value += 1;
      }
    } finally {
      loading.value = false;
    }
  }

  async function refresh(userId?: number) {
    page.value = 1;
    hasMore.value = true;
    posts.value = [];
    await loadNext(userId);
  }

  async function publish(payload: { content: string; markdown: boolean; images: string[]; visibility: 'PUBLIC' | 'PRIVATE' }) {
    const post = await createPost(payload);
    posts.value = [post, ...posts.value];
  }

  return {
    posts,
    hasMore,
    loading,
    loadNext,
    refresh,
    publish
  };
});

