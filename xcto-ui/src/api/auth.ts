import request from './request'
import type { LoginResult, UserInfo } from '@/types/api'

/** 登录，成功后由调用方保存 tokenValue */
export function login(username: string, password: string): Promise<LoginResult> {
  return request.post<LoginResult>('/auth/login', { username, password }).then((res) => res.data)
}

/** 注册（免费开放），返回用户信息 */
export function register(username: string, password: string): Promise<UserInfo> {
  return request.post<UserInfo>('/auth/register', { username, password }).then((res) => res.data)
}

/** 登出 */
export function logout(): Promise<string> {
  return request.post<string>('/auth/logout').then((res) => res.data)
}

/** 当前登录用户信息 */
export function getUserInfo(): Promise<UserInfo> {
  return request.get<UserInfo>('/user/info').then((res) => res.data)
}
