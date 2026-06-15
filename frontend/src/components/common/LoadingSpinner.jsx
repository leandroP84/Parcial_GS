import { CircularProgress, Box } from '@mui/material'

export default function LoadingSpinner({ size = 40 }) {
  return (
    <Box display="flex" justifyContent="center" alignItems="center" py={4}>
      <CircularProgress size={size} />
    </Box>
  )
}
