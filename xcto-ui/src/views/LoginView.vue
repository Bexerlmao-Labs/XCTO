<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import { login, register } from '@/api/auth'

const route = useRoute()
const router = useRouter()

const activeTab = ref<'login' | 'register'>('login')
const submitting = ref(false)

const loginFormRef = ref<FormInstance>()
const registerFormRef = ref<FormInstance>()

const loginForm = reactive({
  username: '',
  password: '',
})

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  inviteCode: '',
})

const usernameRules = {
  required: true,
  message: '请输入用户名',
  trigger: 'blur',
}

const loginRules: FormRules = {
  username: [usernameRules],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const registerRules: FormRules = {
  username: [usernameRules],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少 6 位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (_rule, value: string, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
  inviteCode: [{ required: true, message: '请输入邀请码', trigger: 'blur' }],
}

/** 登录成功后统一保存凭据并跳转回原页面 */
const handleLoginSuccess = (result: { tokenValue: string; username: string; uploadBalance: number }) => {
  localStorage.setItem('satoken', result.tokenValue)
  localStorage.setItem('xcto-username', result.username)
  ElMessage.success(`欢迎回来，${result.username}`)
  const redirect = (route.query.redirect as string) || '/'
  router.replace(redirect)
}

const handleLogin = async () => {
  const valid = await loginFormRef.value?.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    const result = await login(loginForm.username, loginForm.password)
    handleLoginSuccess(result)
  } catch {
    // 错误提示已由响应拦截器统一弹出
  } finally {
    submitting.value = false
  }
}

const handleRegister = async () => {
  const valid = await registerFormRef.value?.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    await register(registerForm.username, registerForm.password, registerForm.inviteCode.trim())
    ElMessage.success('注册成功，正在自动登录...')
    // 注册即登录
    const result = await login(registerForm.username, registerForm.password)
    handleLoginSuccess(result)
  } catch {
    // 错误提示已由响应拦截器统一弹出
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1 class="login-title">XCTO</h1>
        <p class="login-subtitle">登录后即可开始刷题，注册需邀请码</p>
      </div>

      <el-tabs v-model="activeTab" stretch>
        <el-tab-pane label="登录" name="login">
          <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            label-position="top"
            size="large"
            @keyup.enter="handleLogin"
          >
            <el-form-item label="用户名" prop="username">
              <el-input v-model="loginForm.username" placeholder="请输入用户名" autocomplete="username" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                show-password
                autocomplete="current-password"
              />
            </el-form-item>
            <el-button
              type="primary"
              class="submit-btn"
              size="large"
              :loading="submitting"
              @click="handleLogin"
            >
              登录
            </el-button>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="注册" name="register">
          <el-form
            ref="registerFormRef"
            :model="registerForm"
            :rules="registerRules"
            label-position="top"
            size="large"
            @keyup.enter="handleRegister"
          >
            <el-form-item label="用户名" prop="username">
              <el-input v-model="registerForm.username" placeholder="设置用户名" autocomplete="username" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input
                v-model="registerForm.password"
                type="password"
                placeholder="至少 6 位"
                show-password
                autocomplete="new-password"
              />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="registerForm.confirmPassword"
                type="password"
                placeholder="再次输入密码"
                show-password
                autocomplete="new-password"
              />
            </el-form-item>
            <el-form-item label="邀请码" prop="inviteCode">
              <el-input v-model="registerForm.inviteCode" placeholder="请输入邀请码" />
            </el-form-item>
            <el-button
              type="primary"
              class="submit-btn"
              size="large"
              :loading="submitting"
              @click="handleRegister"
            >
              注册并登录
            </el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <p class="login-footer">注册需邀请码；刷题永久免费</p>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 32px 28px 20px;
  background: var(--el-bg-color);
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
}

.login-header {
  text-align: center;
  margin-bottom: 20px;
}

.login-title {
  margin: 0;
  font-size: 28px;
  letter-spacing: 4px;
  color: var(--el-color-primary);
}

.login-subtitle {
  margin: 8px 0 0;
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.submit-btn {
  width: 100%;
  margin-top: 4px;
}

.login-footer {
  margin: 14px 0 0;
  text-align: center;
  font-size: 12px;
  color: var(--el-text-color-placeholder);
}
</style>
