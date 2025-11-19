import http from './http';

export const uploadImages = (files: File[]) => {
  const formData = new FormData();
  files.forEach((file) => formData.append('files', file));
  return http.post<string[]>('/files/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  });
};

