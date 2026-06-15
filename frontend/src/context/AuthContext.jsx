import { createContext, useContext, useState, useEffect, useCallback } from 'react'
import { authService } from '../services/authService'

const AuthContext = createContext(null)

function loadStoredUser() {
  try {
    const stored = localStorage.getItem('user')
    return stored ? JSON.parse(stored) : null
  } catch {
    localStorage.removeItem('user')
    return null
  }
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(loadStoredUser)
  const [token, setToken] = useState(() => localStorage.getItem('token'))
  const [loading, setLoading] = useState(false)

  const isAuthenticated = Boolean(token)

  // Sincronizar sesión si token y user están desincronizados
  useEffect(() => {
    const storedToken = localStorage.getItem('token')
    if (!storedToken) {
      setToken(null)
      setUser(null)
    } else {
      setToken(storedToken)
    }
  }, [])

  // Escuchar logout global (ej. interceptor 401 de Axios)
  useEffect(() => {
    const handleLogout = () => {
      setToken(null)
      setUser(null)
    }
    window.addEventListener('auth:logout', handleLogout)
    return () => window.removeEventListener('auth:logout', handleLogout)
  }, [])

  const persistSession = useCallback((data) => {
    localStorage.setItem('token', data.token)
    const userData = {
      email: data.email,
      nombre: data.nombre,
      apellido: data.apellido,
      rol: data.rol
    }
    localStorage.setItem('user', JSON.stringify(userData))
    setToken(data.token)
    setUser(userData)
    return data
  }, [])

  const login = async (credentials) => {
    setLoading(true)
    try {
      const { data } = await authService.login(credentials)
      return persistSession(data)
    } finally {
      setLoading(false)
    }
  }

  const register = async (userData) => {
    setLoading(true)
    try {
      const { data } = await authService.register(userData)
      return persistSession(data)
    } finally {
      setLoading(false)
    }
  }

  const logout = useCallback(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setToken(null)
    setUser(null)
  }, [])

  return (
    <AuthContext.Provider value={{ user, login, register, logout, loading, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth debe usarse dentro de AuthProvider')
  }
  return context
}
