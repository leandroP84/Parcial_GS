import { useState } from 'react'
import { Box, Toolbar } from '@mui/material'
import Navbar from './Navbar'
import Sidebar, { drawerWidth } from './Sidebar'

export default function AppLayout({ children }) {
  const [mobileOpen, setMobileOpen] = useState(false)

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: 'background.default' }}>
      <Navbar onMenuClick={() => setMobileOpen(true)} />
      <Sidebar mobileOpen={mobileOpen} onClose={() => setMobileOpen(false)} />
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          width: { md: `calc(100% - ${drawerWidth}px)` },
          p: { xs: 2, md: 3 }
        }}
      >
        <Toolbar />
        {children}
      </Box>
    </Box>
  )
}
