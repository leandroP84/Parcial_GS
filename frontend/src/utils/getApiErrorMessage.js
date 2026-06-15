/**
 * Extrae el mensaje de error de una respuesta Axios de forma consistente.
 */
export function getApiErrorMessage(error, fallback = 'Ocurrió un error inesperado') {
  const data = error?.response?.data
  if (!data) return fallback
  if (data.message) return data.message
  if (data.errors?.length > 0) return data.errors[0].message
  return fallback
}
