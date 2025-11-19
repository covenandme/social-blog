<template>
  <div class="space-y-6">
    <PostComposer />
    <div class="space-y-4">
      <PostCard v-for="post in postStore.posts" :key="post.id" :post="post" />
      <div ref="observerTarget" class="h-10 w-full text-center text-slate-400">
        <span v-if="postStore.loading">加载中...</span>
        <span v-else-if="!postStore.hasMore">已经到底啦</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref } from 'vue';
import PostComposer from '../components/post/PostComposer.vue';
import PostCard from '../components/post/PostCard.vue';
import { usePostStore } from '../stores/post';

const postStore = usePostStore();
const observerTarget = ref<HTMLElement | null>(null);
let observer: IntersectionObserver | null = null;

const initObserver = () => {
  if (observer) observer.disconnect();
  observer = new IntersectionObserver((entries) => {
    const entry = entries[0];
    if (entry.isIntersecting) {
      postStore.loadNext();
    }
  });
  if (observerTarget.value) {
    observer.observe(observerTarget.value);
  }
};

onMounted(async () => {
  await postStore.refresh();
  initObserver();
});

onBeforeUnmount(() => observer?.disconnect());
</script>

