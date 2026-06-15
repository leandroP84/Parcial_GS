import { useState } from 'react'
import { Link as RouterLink, useNavigate } from 'react-router-dom'
import {
  Box, Card, CardContent, TextField, Button, Typography, Link, Container, Alert, Grid
} from '@mui/material'
import { AutoAwesome } from '@mui/icons-material'
import { getApiErrorMessage } from '../utils/getApiErrorMessage'
import { useAuth } from '../context/AuthContext'

export default function RegisterPage() {
  const { register, loading } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ nombre: '', apellido: '', email: '', password: '' })
  const [error, setError] = useState('')

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    try {
      await register(form)
      navigate('/dashboard')
    } catch (err) {
      setError(getApiErrorMessage(err, 'Error al registrarse'))
    }
  }

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', alignItems: 'center', bgcolor: 'background.default', py: 4 }}>
      <Container maxWidth="sm">
        <Box textAlign="center" mb={3}>
          <AutoAwesome sx={{ fontSize: 48, color: 'primary.main', mb: 1 }} />
          <Typography variant="h4" gutterBottom>SmartCV IA</Typography>
          <Typography color="text.secondary">Crea tu cuenta y comienza a mejorar tu CV</Typography>
        </Box>
        <Card>
          <CardContent sx={{ p: 4 }}>
            <Typography variant="h5" gutterBottom>Registro</Typography>
            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
            <Box component="form" onSubmit={handleSubmit}>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <TextField fullWidth label="Nombre" required
                    value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })} />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField fullWidth label="Apellido" required
                    value={form.apellido} onChange={(e) => setForm({ ...form, apellido: e.target.value })} />
                </Grid>
              </Grid>
              <TextField fullWidth label="Email" type="email" margin="normal" required
                value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
              <TextField fullWidth label="Contraseña" type="password" margin="normal" required
                helperText="Mínimo 6 caracteres"
                value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
              <Button fullWidth type="submit" variant="contained" size="large" sx={{ mt: 2 }} disabled={loading}>
                {loading ? 'Registrando...' : 'Crear cuenta'}
              </Button>
            </Box>
            <Typography variant="body2" textAlign="center" mt={2}>
              ¿Ya tienes cuenta?{' '}
              <Link component={RouterLink} to="/login">Inicia sesión</Link>
            </Typography>
          </CardContent>
        </Card>
      </Container>
    </Box>
  )
}
