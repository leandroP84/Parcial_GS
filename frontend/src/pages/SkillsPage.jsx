import { useEffect, useState } from 'react'
import {
  Box, Typography, Button, Card, CardContent, IconButton, Dialog, DialogTitle,
  DialogContent, DialogActions, TextField, Grid, MenuItem, Chip
} from '@mui/material'
import { Add, Edit, Delete } from '@mui/icons-material'
import { skillService } from '../services/skillService'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorAlert from '../components/common/ErrorAlert'

const levels = ['BASICO', 'INTERMEDIO', 'AVANZADO', 'EXPERTO']
const levelColors = { BASICO: 'default', INTERMEDIO: 'info', AVANZADO: 'warning', EXPERTO: 'success' }

const emptyForm = { nombre: '', nivel: 'INTERMEDIO' }

export default function SkillsPage() {
  const [items, setItems] = useState([])
  const [initialLoading, setInitialLoading] = useState(true)
  const [error, setError] = useState('')
  const [dialogOpen, setDialogOpen] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [form, setForm] = useState(emptyForm)

  const load = (isInitial = false) => {
    if (isInitial) setInitialLoading(true)
    skillService.getAll()
      .then(({ data }) => setItems(data))
      .catch(() => setError('Error al cargar habilidades'))
      .finally(() => { if (isInitial) setInitialLoading(false) })
  }

  useEffect(() => { load(true) }, [])

  const openCreate = () => { setEditingId(null); setForm(emptyForm); setDialogOpen(true) }

  const openEdit = (item) => {
    setEditingId(item.id)
    setForm({ nombre: item.nombre, nivel: item.nivel })
    setDialogOpen(true)
  }

  const handleSave = async () => {
    try {
      if (editingId) await skillService.update(editingId, form)
      else await skillService.create(form)
      setDialogOpen(false)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Error al guardar')
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('¿Eliminar esta habilidad?')) return
    try { await skillService.delete(id); load() }
    catch { setError('Error al eliminar') }
  }

  if (initialLoading) return <LoadingSpinner />

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4">Habilidades</Typography>
        <Button variant="contained" startIcon={<Add />} onClick={openCreate}>Agregar</Button>
      </Box>
      <ErrorAlert message={error} onClose={() => setError('')} />

      {items.length === 0 ? (
        <Card><CardContent><Typography color="text.secondary">No hay habilidades registradas</Typography></CardContent></Card>
      ) : (
        <Box display="flex" flexWrap="wrap" gap={1}>
          {items.map((item) => (
            <Card key={item.id} sx={{ minWidth: 200 }}>
              <CardContent sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', py: 1.5, '&:last-child': { pb: 1.5 } }}>
                <Box>
                  <Typography fontWeight={600}>{item.nombre}</Typography>
                  <Chip label={item.nivel} size="small" color={levelColors[item.nivel]} sx={{ mt: 0.5 }} />
                </Box>
                <Box>
                  <IconButton size="small" onClick={() => openEdit(item)}><Edit fontSize="small" /></IconButton>
                  <IconButton size="small" color="error" onClick={() => handleDelete(item.id)}><Delete fontSize="small" /></IconButton>
                </Box>
              </CardContent>
            </Card>
          ))}
        </Box>
      )}

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="xs" fullWidth>
        <DialogTitle>{editingId ? 'Editar' : 'Nueva'} habilidad</DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 0.5 }}>
            <Grid item xs={12}><TextField fullWidth label="Nombre" required value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })} /></Grid>
            <Grid item xs={12}>
              <TextField fullWidth select label="Nivel" value={form.nivel} onChange={(e) => setForm({ ...form, nivel: e.target.value })}>
                {levels.map((l) => <MenuItem key={l} value={l}>{l}</MenuItem>)}
              </TextField>
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Cancelar</Button>
          <Button variant="contained" onClick={handleSave}>Guardar</Button>
        </DialogActions>
      </Dialog>
    </Box>
  )
}
