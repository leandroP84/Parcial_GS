import { useEffect, useState } from 'react'
import {
  Box, Typography, Button, Card, CardContent, IconButton, Dialog, DialogTitle,
  DialogContent, DialogActions, TextField, Grid
} from '@mui/material'
import { Add, Edit, Delete } from '@mui/icons-material'
import { experienceService } from '../services/experienceService'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorAlert from '../components/common/ErrorAlert'

const emptyForm = { empresa: '', cargo: '', fechaInicio: '', fechaFin: '', descripcion: '' }

export default function ExperiencesPage() {
  const [items, setItems] = useState([])
  const [initialLoading, setInitialLoading] = useState(true)
  const [error, setError] = useState('')
  const [dialogOpen, setDialogOpen] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [form, setForm] = useState(emptyForm)

  const load = (isInitial = false) => {
    if (isInitial) setInitialLoading(true)
    experienceService.getAll()
      .then(({ data }) => setItems(data))
      .catch(() => setError('Error al cargar experiencias'))
      .finally(() => { if (isInitial) setInitialLoading(false) })
  }

  useEffect(() => { load(true) }, [])

  const openCreate = () => {
    setEditingId(null)
    setForm(emptyForm)
    setDialogOpen(true)
  }

  const openEdit = (item) => {
    setEditingId(item.id)
    setForm({
      empresa: item.empresa,
      cargo: item.cargo,
      fechaInicio: item.fechaInicio,
      fechaFin: item.fechaFin || '',
      descripcion: item.descripcion || ''
    })
    setDialogOpen(true)
  }

  const handleSave = async () => {
    const payload = { ...form, fechaFin: form.fechaFin || null }
    try {
      if (editingId) {
        await experienceService.update(editingId, payload)
      } else {
        await experienceService.create(payload)
      }
      setDialogOpen(false)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Error al guardar')
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('¿Eliminar esta experiencia?')) return
    try {
      await experienceService.delete(id)
      load()
    } catch {
      setError('Error al eliminar')
    }
  }

  if (initialLoading) return <LoadingSpinner />

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4">Experiencia Laboral</Typography>
        <Button variant="contained" startIcon={<Add />} onClick={openCreate}>Agregar</Button>
      </Box>
      <ErrorAlert message={error} onClose={() => setError('')} />

      {items.length === 0 ? (
        <Card><CardContent><Typography color="text.secondary">No hay experiencias registradas</Typography></CardContent></Card>
      ) : items.map((item) => (
        <Card key={item.id} sx={{ mb: 2 }}>
          <CardContent>
            <Box display="flex" justifyContent="space-between">
              <Box>
                <Typography variant="h6">{item.cargo}</Typography>
                <Typography color="primary">{item.empresa}</Typography>
                <Typography variant="body2" color="text.secondary">
                  {item.fechaInicio} — {item.fechaFin || 'Actualidad'}
                </Typography>
                {item.descripcion && <Typography mt={1}>{item.descripcion}</Typography>}
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
        <DialogTitle>{editingId ? 'Editar' : 'Nueva'} experiencia</DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 0.5 }}>
            <Grid item xs={12}><TextField fullWidth label="Empresa" required value={form.empresa} onChange={(e) => setForm({ ...form, empresa: e.target.value })} /></Grid>
            <Grid item xs={12}><TextField fullWidth label="Cargo" required value={form.cargo} onChange={(e) => setForm({ ...form, cargo: e.target.value })} /></Grid>
            <Grid item xs={6}><TextField fullWidth label="Fecha inicio" type="date" InputLabelProps={{ shrink: true }} required value={form.fechaInicio} onChange={(e) => setForm({ ...form, fechaInicio: e.target.value })} /></Grid>
            <Grid item xs={6}><TextField fullWidth label="Fecha fin" type="date" InputLabelProps={{ shrink: true }} value={form.fechaFin} onChange={(e) => setForm({ ...form, fechaFin: e.target.value })} /></Grid>
            <Grid item xs={12}><TextField fullWidth label="Descripción" multiline rows={3} value={form.descripcion} onChange={(e) => setForm({ ...form, descripcion: e.target.value })} /></Grid>
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
