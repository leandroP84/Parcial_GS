import { useEffect, useState } from 'react'
import { Box, Typography, Grid, Card, CardContent, Button } from '@mui/material'
import { Work, School, Psychology, AutoAwesome, Description, Person } from '@mui/icons-material'
import { useNavigate } from 'react-router-dom'
import { dashboardService } from '../services/dashboardService'
import { useAuth } from '../context/AuthContext'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorAlert from '../components/common/ErrorAlert'

const statCards = [
  { key: 'cantidadExperiencias', label: 'Experiencias', icon: <Work />, color: '#2563eb', path: '/experiences' },
  { key: 'cantidadEstudios', label: 'Estudios', icon: <School />, color: '#7c3aed', path: '/education' },
  { key: 'cantidadHabilidades', label: 'Habilidades', icon: <Psychology />, color: '#059669', path: '/skills' },
]

export default function DashboardPage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [stats, setStats] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    dashboardService.getStats()
      .then(({ data }) => setStats(data))
      .catch(() => setError('Error al cargar estadísticas'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <LoadingSpinner />

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Hola, {user?.nombre} 👋
      </Typography>
      <Typography color="text.secondary" mb={3}>
        Gestiona tu CV y obtén recomendaciones con IA
      </Typography>
      <ErrorAlert message={error} onClose={() => setError('')} />

      <Grid container spacing={3} mb={4}>
        {statCards.map((card) => (
          <Grid item xs={12} sm={4} key={card.key}>
            <Card
              sx={{ cursor: 'pointer', transition: 'transform 0.2s', '&:hover': { transform: 'translateY(-4px)' } }}
              onClick={() => navigate(card.path)}
            >
              <CardContent sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <Box sx={{ p: 1.5, borderRadius: 2, bgcolor: `${card.color}15`, color: card.color }}>
                  {card.icon}
                </Box>
                <Box>
                  <Typography variant="h4" fontWeight={700}>{stats?.[card.key] ?? 0}</Typography>
                  <Typography color="text.secondary">{card.label}</Typography>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      <Typography variant="h6" gutterBottom>Acciones rápidas</Typography>
      <Grid container spacing={2}>
        <Grid item>
          <Button variant="outlined" startIcon={<Person />} onClick={() => navigate('/profile')}>
            Editar perfil
          </Button>
        </Grid>
        <Grid item>
          <Button variant="contained" startIcon={<AutoAwesome />} onClick={() => navigate('/ai/analysis')}>
            Analizar CV con IA
          </Button>
        </Grid>
        <Grid item>
          <Button variant="outlined" startIcon={<Description />} onClick={() => navigate('/ai/cover-letter')}>
            Generar carta
          </Button>
        </Grid>
      </Grid>
    </Box>
  )
}
