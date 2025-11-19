<template>
  <div class="min-h-screen bg-slate-100">
    <header class="border-b border-slate-200 bg-white">
      <div class="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
        <h1 class="text-xl font-semibold text-slate-800">Social Blog</h1>
        <div class="flex items-center gap-3">
          <el-button type="primary" size="small" @click="toProfile" v-if="user?.profile">
            {{ user.profile.username }}
          </el-button>
          <el-button size="small" @click="showProfile = true" v-if="user?.profile">编辑资料</el-button>
          <el-button size="small" plain @click="logout" v-if="user?.profile">退出</el-button>
        </div>
      </div>
    </header>
    <main class="mx-auto flex max-w-6xl gap-6 px-6 py-6">
      <NavigationSidebar class="w-60 shrink-0" />
      <section class="flex-1">
        <slot />
      </section>
      <RightSidebar class="w-72 shrink-0" />
    </main>
    <ProfileEditDialog v-model="showProfile" />
  </div>
</template>

<script setup lang="ts">
import NavigationSidebar from './NavigationSidebar.vue';
import RightSidebar from './RightSidebar.vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../../stores/user';
import { ref } from 'vue';
import ProfileEditDialog from '../profile/ProfileEditDialog.vue';

const router = useRouter();
const user = useUserStore();
const showProfile = ref(false);

const toProfile = () => {
  if (user.profile) {
    router.push({ name: 'profile', params: { id: user.profile.id } });
  }
};

const logout = () => {
  user.logout();
  router.push({ name: 'auth' });
};
</script>

