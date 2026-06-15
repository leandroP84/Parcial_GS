import { useState } from 'react'
import { Link as RouterLink, useNavigate } from 'react-router-dom'
import {
  Box, Card, CardContent, TextField, Button, Typography, Link, Container, Alert
} from '@mui/material'
import { AutoAwesome } from '@mui/icons-material'
import { getApiErrorMessage } from '../utils/getApiErrorMessage'
import { useAuth } from '../context/AuthContext'

export default function LoginPage() {
  const { login, loading } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ email: '', password: '' })
  const [error, setError] = useState('')

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    try {
      await login(form)
      navigate('/dashboard')
    } catch (err) {
      setError(getApiErrorMessage(err, 'Error al iniciar sesión'))
    }
  }

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', alignItems: 'center', bgcolor: 'background.default' }}>
      <Container maxWidth="sm">
        <Box textAlign="center" mb={3}>
          <AutoAwesome sx={{ fontSize: 48, color: 'primary.main', mb: 1 }} />
          <Typography variant="h4" gutterBottom>SmartCV IA</Typography>
          <Typography color="text.secondary">Mejora tu empleabilidad con inteligencia artificial</Typography>
        </Box>
        <Card>
          <CardContent sx={{ p: 4 }}>
            <Typography variant="h5" gutterBottom>Iniciar sesión</Typography>
            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
            <Box component="form" onSubmit={handleSubmit}>
              <TextField
                fullWidth label="Email" type="email" margin="normal" required
                value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })}
              />
              <TextField
                fullWidth label="Contraseña" type="password" margin="normal" required
                value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })}
              />
              <Button fullWidth type="submit" variant="contained" size="large" sx={{ mt: 2 }} disabled={loading}>
                {loading ? 'Ingresando...' : 'Ingresar'}
              </Button>
            </Box>
            <Typography variant="body2" textAlign="center" mt={2}>
              ¿No tienes cuenta?{' '}
              <Link component={RouterLink} to="/register">Regístrate</Link>
            </Typography>
          </CardContent>
        </Card>
      </Container>
    </Box>
  )
}
