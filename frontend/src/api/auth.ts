import client from './client'
import type { UserInfo } from '@/types'

export interface LoginResponse {
  token: string
  user: UserInfo
}

export const authApi = {
  login: (email: string, password: string) =>
    client.post<LoginResponse>('/auth/login', { email, password }),
  me: () =>
    client.get<UserInfo>('/auth/me')
}
