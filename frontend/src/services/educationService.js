import api from './api'

export const educationService = {
  getAll: () => api.get('/educaciones'),
  getById: (id) => api.get(`/educaciones/${id}`),
  create: (data) => api.post('/educaciones', data),
  update: (id, data) => api.put(`/educaciones/${id}`, data),
  delete: (id) => api.delete(`/educaciones/${id}`)
}
