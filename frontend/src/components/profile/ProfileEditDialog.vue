<template>
  <el-dialog v-model="visible" title="编辑个人资料" width="420px" @close="handleClose">
    <el-form :model="form" label-width="80px">
      <el-form-item label="头像地址">
        <el-input v-model="form.avatarUrl" placeholder="输入图片 URL" />
      </el-form-item>
      <el-form-item label="个人简介">
        <el-input type="textarea" v-model="form.bio" :autosize="{ minRows: 3, maxRows: 5 }" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, watch, ref } from 'vue';
import { useUserStore } from '../../stores/user';
import { ElMessage } from 'element-plus';

const props = defineProps<{ modelValue: boolean }>();
const emit = defineEmits<{ (e: 'update:modelValue', value: boolean): void }>();

const visible = ref(props.modelValue);
const form = reactive({ avatarUrl: '', bio: '' });
const saving = ref(false);
const userStore = useUserStore();

watch(
  () => props.modelValue,
  (value) => {
    visible.value = value;
    if (value && userStore.profile) {
      form.avatarUrl = userStore.profile.avatarUrl;
      form.bio = userStore.profile.bio;
    }
  }
);

watch(visible, (value) => emit('update:modelValue', value));

const handleClose = () => {
  visible.value = false;
};

const handleSave = async () => {
  try {
    saving.value = true;
    await userStore.updateProfileInfo({ avatarUrl: form.avatarUrl, bio: form.bio });
    ElMessage.success('保存成功');
    handleClose();
  } catch (error) {
    ElMessage.error('保存失败');
  } finally {
    saving.value = false;
  }
};
</script>

