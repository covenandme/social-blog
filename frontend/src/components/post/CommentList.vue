<template>
  <div class="mt-4 rounded-2xl bg-slate-50 p-4">
    <div class="mb-3 text-sm font-semibold text-slate-600">评论</div>
    <el-input
      v-model="content"
      type="textarea"
      :autosize="{ minRows: 2, maxRows: 4 }"
      :placeholder="replyingHint || '发表你的看法'"
    />
    <div class="mt-2 flex justify-end">
      <el-button v-if="replyingTarget" size="small" @click="clearReply">取消回复</el-button>
      <el-button size="small" type="primary" :loading="submitting" @click="submit">发送</el-button>
    </div>
    <div v-if="loading" class="text-center text-slate-400">加载中...</div>
    <div v-else>
      <div v-for="item in comments" :key="item.id" class="mt-4 rounded-xl bg-white p-3">
        <div class="flex items-center gap-2 text-sm">
          <img :src="item.author.avatarUrl" class="h-6 w-6 rounded-full" />
          <span class="font-medium text-slate-700">{{ item.author.username }}</span>
          <span class="text-xs text-slate-400">{{ item.createdAt }}</span>
        </div>
        <p class="mt-2 text-sm text-slate-600">{{ item.content }}</p>
        <div class="mt-2">
          <el-button size="small" text @click="setReply(item.id, item.author.username)">回复</el-button>
        </div>
        <div v-if="item.replies?.length" class="mt-3 space-y-2 border-l-2 border-slate-100 pl-3">
          <div v-for="reply in item.replies" :key="reply.id" class="rounded-lg bg-slate-50 p-2 text-sm">
            <div class="flex items-center justify-between">
              <div>
                <span class="font-semibold text-slate-700">{{ reply.author.username }}：</span>
                <span>{{ reply.content }}</span>
              </div>
              <el-button size="small" text @click="setReply(reply.id, reply.author.username)">回复</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { createComment, fetchComments, type CommentNode } from '../../api/comment';
import { ElMessage } from 'element-plus';

const props = defineProps<{ postId: number }>();

const comments = ref<CommentNode[]>([]);
const content = ref('');
const loading = ref(false);
const submitting = ref(false);
const replyingTarget = ref<number | null>(null);
const replyingHint = ref('');

const load = async () => {
  loading.value = true;
  try {
    comments.value = await fetchComments(props.postId);
  } finally {
    loading.value = false;
  }
};

const submit = async () => {
  if (!content.value) return;
  try {
    submitting.value = true;
    await createComment(props.postId, { content: content.value, parentId: replyingTarget.value ?? undefined });
    content.value = '';
    clearReply();
    await load();
  } catch (error) {
    ElMessage.error('发送失败');
  } finally {
    submitting.value = false;
  }
};

const setReply = (id: number, username: string) => {
  replyingTarget.value = id;
  replyingHint.value = `回复 ${username}：`;
};

const clearReply = () => {
  replyingTarget.value = null;
  replyingHint.value = '';
};

onMounted(load);
watch(() => props.postId, load);
</script>

