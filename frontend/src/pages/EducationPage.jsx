import { useEffect, useState } from 'react'
import {
  Box, Typography, Button, Card, CardContent, IconButton, Dialog, DialogTitle,
  DialogContent, DialogActions, TextField, Grid
} from '@mui/material'
import { Add, Edit, Delete } from '@mui/icons-material'
import { educationService } from '../services/educationService'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorAlert from '../components/common/ErrorAlert'

const emptyForm = { institucion: '', carrera: '', fechaInicio: '', fechaFin: '' }

export default function EducationPage() {
  const [items, setItems] = useState([])
  const [initialLoading, setInitialLoading] = useState(true)
  const [error, setError] = useState('')
  const [dialogOpen, setDialogOpen] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [form, setForm] = useState(emptyForm)

  const load = (isInitial = false) => {
    if (isInitial) setInitialLoading(true)
    educationService.getAll()
      .then(({ data }) => setItems(data))
      .catch(() => setError('Error al cargar educación'))
      .finally(() => { if (isInitial) setInitialLoading(false) })
  }

  useEffect(() => { load(true) }, [])

  const openCreate = () => { setEditingId(null); setForm(emptyForm); setDialogOpen(true) }

  const openEdit = (item) => {
    setEditingId(item.id)
    setForm({
      institucion: item.institucion,
      carrera: item.carrera,
      fechaInicio: item.fechaInicio,
      fechaFin: item.fechaFin || ''
    })
    setDialogOpen(true)
  }

  const handleSave = async () => {
    const payload = { ...form, fechaFin: form.fechaFin || null }
    try {
      if (editingId) await educationService.update(editingId, payload)
      else await educationService.create(payload)
      setDialogOpen(false)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Error al guardar')
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('¿Eliminar este estudio?')) return
    try { await educationService.delete(id); load() }
    catch { setError('Error al eliminar') }
  }

  if (initialLoading) return <LoadingSpinner />

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4">Educación</Typography>
        <Button variant="contained" startIcon={<Add />} onClick={openCreate}>Agregar</Button>
      </Box>
      <ErrorAlert message={error} onClose={() => setError('')} />

      {items.length === 0 ? (
        <Card><CardContent><Typography color="text.secondary">No hay estudios registrados</Typography></CardContent></Card>
      ) : items.map((item) => (
        <Card key={item.id} sx={{ mb: 2 }}>
          <CardContent>
            <Box display="flex" justifyContent="space-between">
              <Box>
                <Typography variant="h6">{item.carrera}</Typography>
                <Typography color="primary">{item.institucion}</Typography>
                <Typography variant="body2" color="text.secondary">
                  {item.fechaInicio} — {item.fechaFin || 'En curso'}
                </Typography>
              </Box>
              <Box>
                <IconButton onClick={() => openEdit(item)}><Edit /></IconButton>
                <IconButton color="error" onClick={() => handleDelete(item.id)}><Delete /></IconButton>
              </Box>
            </Box>
          </CardContent>
        </Card>
      ))}

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{editingId ? 'Editar' : 'Nueva'} educación</DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 0.5 }}>
            <Grid item xs={12}><TextField fullWidth label="Institución" required value={form.institucion} onChange={(e) => setForm({ ...form, institucion: e.target.value })} /></Grid>
            <Grid item xs={12}><TextField fullWidth label="Carrera" required value={form.carrera} onChange={(e) => setForm({ ...form, carrera: e.target.value })} /></Grid>
            <Grid item xs={6}><TextField fullWidth label="Fecha inicio" type="date" InputLabelProps={{ shrink: true }} required value={form.fechaInicio} onChange={(e) => setForm({ ...form, fechaInicio: e.target.value })} /></Grid>
            <Grid item xs={6}><TextField fullWidth label="Fecha fin" type="date" InputLabelProps={{ shrink: true }} value={form.fechaFin} onChange={(e) => setForm({ ...form, fechaFin: e.target.value })} /></Grid>
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
