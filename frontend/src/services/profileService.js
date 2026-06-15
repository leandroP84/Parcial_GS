import api from './api'

export const profileService = {
  get: () => api.get('/perfil'),
  save: (data) => api.put('/perfil', data)
}
