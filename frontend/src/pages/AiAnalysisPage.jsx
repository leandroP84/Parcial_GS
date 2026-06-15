import { useState } from 'react'
import {
  Box, Typography, Button, Card, CardContent, Grid, List, ListItem, ListItemIcon, ListItemText, Chip
} from '@mui/material'
import { AutoAwesome, CheckCircle, Warning, Lightbulb, Psychology } from '@mui/icons-material'
import { aiService } from '../services/aiService'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorAlert from '../components/common/ErrorAlert'

export default function AiAnalysisPage() {
  const [result, setResult] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const handleAnalyze = async () => {
    setLoading(true)
    setError('')
    setResult(null)
    try {
      const { data } = await aiService.analyzeCv()
      setResult(data)
    } catch (err) {
      setError(err.response?.data?.message || 'Error al analizar CV. Verifique su API key de Gemini.')
    } finally {
      setLoading(false)
    }
  }

  const sections = [
    { key: 'fortalezas', title: 'Fortalezas', icon: <CheckCircle color="success" />, color: 'success.main' },
    { key: 'debilidades', title: 'Debilidades', icon: <Warning color="warning" />, color: 'warning.main' },
    { key: 'recomendaciones', title: 'Recomendaciones', icon: <Lightbulb color="info" />, color: 'info.main' },
    { key: 'habilidadesSugeridas', title: 'Habilidades Sugeridas', icon: <Psychology color="secondary" />, color: 'secondary.main' },
  ]

  return (
    <Box>
      <Typography variant="h4" gutterBottom>Análisis de CV con IA</Typography>
      <Typography color="text.secondary" mb={3}>
        La IA analizará tu perfil completo y te dará recomendaciones para mejorar tu empleabilidad
      </Typography>
      <ErrorAlert message={error} onClose={() => setError('')} />

      <Button
        variant="contained" size="large" startIcon={<AutoAwesome />}
        onClick={handleAnalyze} disabled={loading} sx={{ mb: 3 }}
      >
        {loading ? 'Analizando...' : 'Analizar mi CV'}
      </Button>

      {loading && <LoadingSpinner />}

      {result && (
        <Grid container spacing={3}>
          {sections.map((section) => (
            <Grid item xs={12} md={6} key={section.key}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <Box display="flex" alignItems="center" gap={1} mb={2}>
                    {section.icon}
                    <Typography variant="h6" color={section.color}>{section.title}</Typography>
                  </Box>
                  {section.key === 'habilidadesSugeridas' ? (
                    <Box display="flex" flexWrap="wrap" gap={1}>
                      {(result[section.key] || []).map((item, i) => (
                        <Chip key={i} label={item} color="secondary" variant="outlined" />
                      ))}
                    </Box>
                  ) : (
                    <List dense>
                      {(result[section.key] || []).map((item, i) => (
                        <ListItem key={i} disableGutters>
                          <ListItemIcon sx={{ minWidth: 32 }}>{section.icon}</ListItemIcon>
                          <ListItemText primary={item} />
                        </ListItem>
                      ))}
                    </List>
                  )}
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </Box>
  )
}
