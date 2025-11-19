<template>
  <nav class="sticky top-8 rounded-2xl bg-white p-5 shadow-sm">
    <div class="mb-6 text-sm font-medium text-slate-400">导航</div>
    <el-menu :default-active="active" class="border-none" @select="handleSelect">
      <el-menu-item index="feed">
        <span>动态广场</span>
      </el-menu-item>
      <el-menu-item index="profile" v-if="user.profile">
        <span>我的主页</span>
      </el-menu-item>
    </el-menu>
  </nav>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '../../stores/user';

const router = useRouter();
const route = useRoute();
const user = useUserStore();

const active = computed(() => {
  if (route.name === 'profile') return 'profile';
  return 'feed';
});

const handleSelect = (index: string) => {
  if (index === 'feed') router.push({ name: 'feed' });
  if (index === 'profile' && user.profile) {
    router.push({ name: 'profile', params: { id: user.profile.id } });
  }
};
</script>

