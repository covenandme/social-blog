<template>
  <article class="rounded-2xl bg-white p-5 shadow-sm">
    <header class="mb-3 flex items-center gap-3">
      <img :src="post.author.avatarUrl" class="h-12 w-12 rounded-full object-cover" />
      <div>
        <div class="font-semibold text-slate-800">{{ post.author.username }}</div>
        <div class="text-xs text-slate-400">{{ post.createdAt }}</div>
      </div>
      <el-tag size="small" class="ml-auto" :type="post.visibility === 'PUBLIC' ? 'success' : 'info'">
        {{ post.visibility === 'PUBLIC' ? '公开' : '仅自己' }}
      </el-tag>
    </header>
    <div class="prose prose-slate max-w-none" v-if="post.markdown" v-html="renderedMarkdown"></div>
    <p v-else class="whitespace-pre-wrap text-slate-700">
      {{ post.content }}
    </p>
    <div v-if="post.images?.length" class="mt-3 grid grid-cols-3 gap-2">
      <img v-for="img in post.images" :key="img" :src="img" class="h-28 w-full rounded-xl object-cover" />
    </div>
    <footer class="mt-4 flex items-center gap-4 text-sm text-slate-500">
      <button class="flex items-center gap-1 text-rose-500" @click="toggleLike">
        <el-icon>
          <component :is="post.liked ? StarFilled : Star" />
        </el-icon>
        <span>{{ likeCount }}</span>
      </button>
      <span>评论 {{ post.commentCount }}</span>
    </footer>
    <CommentList :post-id="post.id" />
  </article>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import MarkdownIt from 'markdown-it';
import type { Post } from '../../types/post';
import { likePost, cancelLike } from '../../api/like';
import CommentList from './CommentList.vue';
import { ElMessage } from 'element-plus';
import { Star, StarFilled } from '@element-plus/icons-vue';

const props = defineProps<{
  post: Post;
}>();

const md = new MarkdownIt({ html: false, linkify: true });
const renderedMarkdown = computed(() => md.render(props.post.content));
const likeCount = ref(props.post.likeCount);

const toggleLike = async () => {
  try {
    if (props.post.liked) {
      await cancelLike(props.post.id);
      props.post.liked = false;
      likeCount.value -= 1;
    } else {
      await likePost(props.post.id);
      props.post.liked = true;
      likeCount.value += 1;
    }
  } catch (error) {
    ElMessage.error('操作失败');
  }
};
</script>


