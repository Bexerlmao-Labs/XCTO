import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  // 统一 API 前缀：生产环境由宿主机 nginx 剥掉 /api 转发到后端，
  // 开发环境由 vite proxy 剥掉 /api 转发到本地后端（见 vite.config.ts）
  baseURL: '/api',
  timeout: 10000,
})

// 附带 sa-token（登录后 localStorage 中的 tokenValue）
request.interceptors.request.use((config) => {
  const token = localStorage.getItem('satoken')
  if (token) {
    config.headers['satoken'] = token
  }
  return config
})

request.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status
    const message = error.response?.data?.message || error.message || '请求失败'

    // 登录过期/未登录：清除本地凭据并踢回登录页
    if (status === 401) {
      localStorage.removeItem('satoken')
      localStorage.removeItem('xcto-username')
      if (!window.location.pathname.startsWith('/login')) {
        ElMessage.warning('登录已过期，请重新登录')
        const redirect = encodeURIComponent(window.location.pathname + window.location.search)
        window.location.assign(`/login?redirect=${redirect}`)
      }
    } else {
      ElMessage.error(message)
    }
    return Promise.reject(error)
  },
)

export default request
