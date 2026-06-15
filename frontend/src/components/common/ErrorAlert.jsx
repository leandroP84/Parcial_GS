import { Alert } from '@mui/material'

export default function ErrorAlert({ message, onClose }) {
  if (!message) return null
  return (
    <Alert severity="error" onClose={onClose} sx={{ mb: 2 }}>
      {message}
    </Alert>
  )
}
