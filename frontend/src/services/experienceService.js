import api from './api'

export const experienceService = {
  getAll: () => api.get('/experiencias'),
  getById: (id) => api.get(`/experiencias/${id}`),
  create: (data) => api.post('/experiencias', data),
  update: (id, data) => api.put(`/experiencias/${id}`, data),
  delete: (id) => api.delete(`/experiencias/${id}`)
}
