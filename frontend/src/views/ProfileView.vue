<template>
  <div class="space-y-6">
    <div class="rounded-2xl bg-white p-6 shadow-sm" v-if="profile">
      <div class="flex items-center gap-4">
        <img :src="profile.avatarUrl" class="h-20 w-20 rounded-full object-cover" />
        <div>
          <div class="text-2xl font-semibold text-slate-800">{{ profile.username }}</div>
          <p class="text-slate-500">{{ profile.bio }}</p>
        </div>
      </div>
    </div>
    <div class="space-y-4">
      <PostCard v-for="post in postStore.posts" :key="post.id" :post="post" />
      <div ref="target" class="h-10 text-center text-slate-400">
        <span v-if="postStore.loading">加载中...</span>
        <span v-else-if="!postStore.hasMore">没有更多了</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, onBeforeUnmount } from 'vue';
import { useRoute } from 'vue-router';
import PostCard from '../components/post/PostCard.vue';
import { usePostStore } from '../stores/post';
import http from '../api/http';
import type { UserProfile } from '../types/user';

const route = useRoute();
const postStore = usePostStore();
const profile = ref<UserProfile | null>(null);
const target = ref<HTMLElement | null>(null);
let observer: IntersectionObserver | null = null;

const loadProfile = async () => {
  const { id } = route.params;
  profile.value = await http.get<UserProfile>(`/users/${id}`);
};

const setupObserver = () => {
  if (observer) observer.disconnect();
  observer = new IntersectionObserver((entries) => {
    if (entries[0].isIntersecting) {
      postStore.loadNext(Number(route.params.id));
    }
  });
  if (target.value) observer.observe(target.value);
};

const init = async () => {
  await loadProfile();
  await postStore.refresh(Number(route.params.id));
  setupObserver();
};

onMounted(init);
watch(() => route.params.id, init);
onBeforeUnmount(() => observer?.disconnect());
</script>

