import {
  Drawer, List, ListItemButton, ListItemIcon, ListItemText, Toolbar, Box, Divider, Typography
} from '@mui/material'
import {
  Dashboard, Person, Work, School, Psychology, AutoAwesome, Description
} from '@mui/icons-material'
import { useLocation, useNavigate } from 'react-router-dom'

const drawerWidth = 260

const menuItems = [
  { text: 'Dashboard', icon: <Dashboard />, path: '/dashboard' },
  { text: 'Perfil Profesional', icon: <Person />, path: '/profile' },
  { text: 'Experiencias', icon: <Work />, path: '/experiences' },
  { text: 'Educación', icon: <School />, path: '/education' },
  { text: 'Habilidades', icon: <Psychology />, path: '/skills' },
]

const aiItems = [
  { text: 'Analizar CV', icon: <AutoAwesome />, path: '/ai/analysis' },
  { text: 'Carta de Presentación', icon: <Description />, path: '/ai/cover-letter' },
]

function SidebarContent() {
  const location = useLocation()
  const navigate = useNavigate()

  const renderItems = (items) => items.map((item) => (
    <ListItemButton
      key={item.path}
      selected={location.pathname === item.path}
      onClick={() => navigate(item.path)}
      sx={{
        mx: 1, borderRadius: 2, mb: 0.5,
        '&.Mui-selected': { bgcolor: 'primary.main', color: 'white', '& .MuiListItemIcon-root': { color: 'white' } }
      }}
    >
      <ListItemIcon sx={{ minWidth: 40 }}>{item.icon}</ListItemIcon>
      <ListItemText primary={item.text} primaryTypographyProps={{ fontSize: 14, fontWeight: 500 }} />
    </ListItemButton>
  ))

  return (
    <Box>
      <Toolbar />
      <Box sx={{ px: 2, py: 2 }}>
        <Typography variant="overline" color="text.secondary">Mi CV</Typography>
      </Box>
      <List>{renderItems(menuItems)}</List>
      <Divider sx={{ my: 1 }} />
      <Box sx={{ px: 2, py: 1 }}>
        <Typography variant="overline" color="text.secondary">Inteligencia Artificial</Typography>
      </Box>
      <List>{renderItems(aiItems)}</List>
    </Box>
  )
}

export default function Sidebar({ mobileOpen, onClose }) {
  return (
    <>
      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={onClose}
        ModalProps={{ keepMounted: true }}
        sx={{ display: { xs: 'block', md: 'none' }, '& .MuiDrawer-paper': { width: drawerWidth } }}
      >
        <SidebarContent />
      </Drawer>
      <Drawer
        variant="permanent"
        sx={{
          display: { xs: 'none', md: 'block' },
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': { width: drawerWidth, borderRight: 1, borderColor: 'divider', bgcolor: 'background.paper' }
        }}
        open
      >
        <SidebarContent />
      </Drawer>
    </>
  )
}

export { drawerWidth }
