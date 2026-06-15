import { useState } from 'react'
import {
  Box, Typography, Button, Card, CardContent, TextField, Grid
} from '@mui/material'
import { Description, ContentCopy } from '@mui/icons-material'
import { aiService } from '../services/aiService'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorAlert from '../components/common/ErrorAlert'

export default function CoverLetterPage() {
  const [form, setForm] = useState({ puestoObjetivo: '', empresaObjetivo: '' })
  const [carta, setCarta] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [copied, setCopied] = useState(false)

  const handleGenerate = async () => {
    setLoading(true)
    setError('')
    setCarta('')
    try {
      const { data } = await aiService.generateCoverLetter(form)
      setCarta(data.carta)
    } catch (err) {
      setError(err.response?.data?.message || 'Error al generar carta. Verifique su API key de Gemini.')
    } finally {
      setLoading(false)
    }
  }

  const handleCopy = () => {
    navigator.clipboard.writeText(carta)
    setCopied(true)
    setTimeout(() => setCopied(false), 2000)
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>Carta de Presentación</Typography>
      <Typography color="text.secondary" mb={3}>
        Genera una carta de presentación profesional basada en tu perfil
      </Typography>
      <ErrorAlert message={error} onClose={() => setError('')} />

      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <TextField fullWidth label="Puesto objetivo (opcional)"
                value={form.puestoObjetivo} onChange={(e) => setForm({ ...form, puestoObjetivo: e.target.value })} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField fullWidth label="Empresa objetivo (opcional)"
                value={form.empresaObjetivo} onChange={(e) => setForm({ ...form, empresaObjetivo: e.target.value })} />
            </Grid>
          </Grid>
          <Button
            variant="contained" startIcon={<Description />} onClick={handleGenerate}
            disabled={loading} sx={{ mt: 2 }}
          >
            {loading ? 'Generando...' : 'Generar carta'}
          </Button>
        </CardContent>
      </Card>

      {loading && <LoadingSpinner />}

      {carta && (
        <Card>
          <CardContent>
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
              <Typography variant="h6">Tu carta de presentación</Typography>
              <Button startIcon={<ContentCopy />} onClick={handleCopy} size="small">
                {copied ? '¡Copiado!' : 'Copiar'}
              </Button>
            </Box>
            <Typography sx={{ whiteSpace: 'pre-wrap', lineHeight: 1.8 }}>{carta}</Typography>
          </CardContent>
        </Card>
      )}
    </Box>
  )
}
