import api from './api'

export const aiService = {
  analyzeCv: () => api.post('/ai/analyze-cv'),
  generateCoverLetter: (data) => api.post('/ai/generate-cover-letter', data)
}
