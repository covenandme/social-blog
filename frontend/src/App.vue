<template>
  <AppLayout>
    <RouterView />
  </AppLayout>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router';
import AppLayout from './components/layout/AppLayout.vue';
import { onMounted } from 'vue';
import { useUserStore } from './stores/user';

const userStore = useUserStore();
onMounted(() => userStore.loadProfile());
</script>

