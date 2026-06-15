import { useEffect, useState } from 'react'
import { Box, Typography, TextField, Button, Card, CardContent, Alert } from '@mui/material'
import { Save } from '@mui/icons-material'
import { profileService } from '../services/profileService'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorAlert from '../components/common/ErrorAlert'

export default function ProfilePage() {
  const [form, setForm] = useState({
    resumenProfesional: '', telefono: '', linkedin: '', github: ''
  })
  const [userInfo, setUserInfo] = useState({ nombre: '', apellido: '', email: '' })
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState(false)

  useEffect(() => {
    profileService.get()
      .then(({ data }) => {
        setForm({
          resumenProfesional: data.resumenProfesional || '',
          telefono: data.telefono || '',
          linkedin: data.linkedin || '',
          github: data.github || ''
        })
        setUserInfo({ nombre: data.nombre, apellido: data.apellido, email: data.email })
      })
      .catch(() => setError('Error al cargar perfil'))
      .finally(() => setLoading(false))
  }, [])

  const handleSave = async (e) => {
    e.preventDefault()
    setSaving(true)
    setError('')
    setSuccess(false)
    try {
      await profileService.save(form)
      setSuccess(true)
    } catch (err) {
      setError(err.response?.data?.message || 'Error al guardar')
    } finally {
      setSaving(false)
    }
  }

  if (loading) return <LoadingSpinner />

  return (
    <Box>
      <Typography variant="h4" gutterBottom>Perfil Profesional</Typography>
      <Typography color="text.secondary" mb={3}>
        {userInfo.nombre} {userInfo.apellido} — {userInfo.email}
      </Typography>
      <ErrorAlert message={error} onClose={() => setError('')} />
      {success && <Alert severity="success" sx={{ mb: 2 }}>Perfil guardado correctamente</Alert>}

      <Card>
        <CardContent>
          <Box component="form" onSubmit={handleSave}>
            <TextField
              fullWidth label="Resumen profesional" multiline rows={5} margin="normal"
              value={form.resumenProfesional}
              onChange={(e) => setForm({ ...form, resumenProfesional: e.target.value })}
              helperText="Describe tu experiencia y objetivos profesionales"
            />
            <TextField fullWidth label="Teléfono" margin="normal"
              value={form.telefono} onChange={(e) => setForm({ ...form, telefono: e.target.value })} />
            <TextField fullWidth label="LinkedIn" margin="normal"
              value={form.linkedin} onChange={(e) => setForm({ ...form, linkedin: e.target.value })} />
            <TextField fullWidth label="GitHub" margin="normal"
              value={form.github} onChange={(e) => setForm({ ...form, github: e.target.value })} />
            <Button type="submit" variant="contained" startIcon={<Save />} sx={{ mt: 2 }} disabled={saving}>
              {saving ? 'Guardando...' : 'Guardar perfil'}
            </Button>
          </Box>
        </CardContent>
      </Card>
    </Box>
  )
}
