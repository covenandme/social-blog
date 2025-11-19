<template>
  <div class="flex min-h-[60vh] items-center justify-center">
    <div class="w-full max-w-md rounded-2xl bg-white p-8 shadow-lg">
      <h2 class="mb-6 text-center text-2xl font-semibold text-slate-800">欢迎来到 Social Blog</h2>
      <el-tabs v-model="active">
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" @submit.prevent>
            <el-form-item>
              <el-input v-model="loginForm.username" placeholder="用户名" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="loginForm.password" type="password" placeholder="密码" />
            </el-form-item>
            <el-button class="w-full" type="primary" :loading="loading" @click="doLogin">
              登录
            </el-button>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-form :model="registerForm" @submit.prevent>
            <el-form-item>
              <el-input v-model="registerForm.username" placeholder="设置用户名" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="registerForm.password" type="password" placeholder="设置密码" />
            </el-form-item>
            <el-button class="w-full" type="success" :loading="loading" @click="doRegister">
              注册并登录
            </el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/user';
import { ElMessage } from 'element-plus';

const active = ref<'login' | 'register'>('login');
const loginForm = reactive({ username: '', password: '' });
const registerForm = reactive({ username: '', password: '' });
const loading = ref(false);
const router = useRouter();
const userStore = useUserStore();

const doLogin = async () => {
  try {
    loading.value = true;
    await userStore.loginByPassword(loginForm.username, loginForm.password);
    router.push({ name: 'feed' });
  } catch (error) {
    ElMessage.error('登录失败');
  } finally {
    loading.value = false;
  }
};

const doRegister = async () => {
  try {
    loading.value = true;
    await userStore.registerAccount(registerForm.username, registerForm.password);
    router.push({ name: 'feed' });
  } catch (error) {
    ElMessage.error('注册失败');
  } finally {
    loading.value = false;
  }
};
</script>

