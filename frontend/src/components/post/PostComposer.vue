<template>
  <div class="rounded-2xl bg-white p-5 shadow-sm">
    <div class="mb-3 flex items-center justify-between">
      <span class="font-semibold text-slate-700">发布动态</span>
      <el-switch v-model="form.markdown" active-text="Markdown" />
    </div>
    <el-input
      v-model="form.content"
      type="textarea"
      :autosize="{ minRows: 3, maxRows: 8 }"
      placeholder="分享你的想法..."
    />
    <div class="mt-3 space-y-3">
      <div class="flex flex-wrap gap-3">
        <div
          v-for="img in form.images"
          :key="img"
          class="relative h-20 w-20 overflow-hidden rounded-xl border border-slate-200"
        >
          <img :src="img" class="h-full w-full object-cover" />
          <button
            class="absolute right-1 top-1 rounded-full bg-white/80 px-2 text-xs text-slate-500"
            @click="removeImage(img)"
            type="button"
          >
            ✕
          </button>
        </div>
        <button
          class="flex h-20 w-20 items-center justify-center rounded-xl border-2 border-dashed border-slate-300 text-sm text-slate-400"
          type="button"
          @click="handlePickFiles"
        >
          {{ uploading ? '上传中…' : '选择图片' }}
        </button>
      </div>
      <input
        type="file"
        ref="fileInput"
        class="hidden"
        multiple
        accept="image/*"
        @change="onFileChange"
      />
    </div>
    <div class="mt-3 flex items-center justify-between">
      <el-radio-group v-model="form.visibility">
        <el-radio-button label="PUBLIC">公开</el-radio-button>
        <el-radio-button label="PRIVATE">仅自己</el-radio-button>
      </el-radio-group>
      <el-button type="primary" :loading="submitting" @click="handlePublish" :disabled="!form.content">
        发布
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { usePostStore } from '../../stores/post';
import { ElMessage } from 'element-plus';
import { uploadImages } from '../../api/file';

const form = reactive({
  content: '',
  markdown: false,
  visibility: 'PUBLIC' as 'PUBLIC' | 'PRIVATE',
  images: [] as string[]
});

const submitting = ref(false);
const uploading = ref(false);
const fileInput = ref<HTMLInputElement | null>(null);
const postStore = usePostStore();

const handlePickFiles = () => {
  fileInput.value?.click();
};

const onFileChange = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  const files = target.files ? Array.from(target.files) : [];
  if (!files.length) {
    return;
  }
  try {
    uploading.value = true;
    const urls = await uploadImages(files);
    form.images.push(...urls);
    ElMessage.success('图片上传成功');
  } catch (error) {
    ElMessage.error('图片上传失败');
  } finally {
    uploading.value = false;
    if (target) {
      target.value = '';
    }
  }
};

const removeImage = (url: string) => {
  form.images = form.images.filter((item) => item !== url);
};

const handlePublish = async () => {
  try {
    submitting.value = true;
    await postStore.publish({
      content: form.content,
      markdown: form.markdown,
      images: form.images,
      visibility: form.visibility
    });
    form.content = '';
    form.images = [];
    ElMessage.success('发布成功');
  } catch (error) {
    ElMessage.error((error as Error).message);
  } finally {
    submitting.value = false;
  }
};
</script>

