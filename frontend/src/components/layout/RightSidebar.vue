<template>
  <aside class="sticky top-8 space-y-4">
    <div class="rounded-2xl bg-white p-5 shadow-sm">
      <div class="mb-3 text-sm font-semibold text-slate-600">个人名片</div>
      <div v-if="user.profile" class="flex items-center gap-3">
        <img :src="user.profile.avatarUrl" class="h-12 w-12 rounded-full object-cover" />
        <div>
          <div class="font-medium text-slate-800">{{ user.profile.username }}</div>
          <div class="text-sm text-slate-500 line-clamp-2">{{ user.profile.bio }}</div>
        </div>
      </div>
      <div v-else class="text-slate-400">请登录后查看</div>
    </div>
    <div class="rounded-2xl bg-white p-5 shadow-sm">
      <div class="mb-3 text-sm font-semibold text-slate-600">推荐话题</div>
      <ul class="space-y-2 text-sm text-slate-500">
        <li># 创作灵感</li>
        <li># 技术分享</li>
        <li># 生活碎片</li>
      </ul>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { useUserStore } from '../../stores/user';

const user = useUserStore();
</script>

