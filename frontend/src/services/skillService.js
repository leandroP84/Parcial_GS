import api from './api'

export const skillService = {
  getAll: () => api.get('/habilidades'),
  getById: (id) => api.get(`/habilidades/${id}`),
  create: (data) => api.post('/habilidades', data),
  update: (id, data) => api.put(`/habilidades/${id}`, data),
  delete: (id) => api.delete(`/habilidades/${id}`)
}
