<script setup lang="ts">
import { ref } from 'vue'
import { RouterView } from 'vue-router'

const THEME_KEY = 'xcto-theme'

const isDark = ref(false)

// 初始化主题：localStorage 优先，否则跟随系统
const initTheme = () => {
  const saved = localStorage.getItem(THEME_KEY)
  const prefersDark = window.matchMedia?.('(prefers-color-scheme: dark)').matches
  isDark.value = saved ? saved === 'dark' : !!prefersDark
  document.documentElement.classList.toggle('dark', isDark.value)
}

const toggleTheme = () => {
  isDark.value = !isDark.value
  document.documentElement.classList.toggle('dark', isDark.value)
  localStorage.setItem(THEME_KEY, isDark.value ? 'dark' : 'light')
}

initTheme()
</script>

<template>
  <!-- 深色/浅色切换按钮 -->
  <button class="theme-toggle" :title="isDark ? '切换到浅色模式' : '切换到深色模式'" @click="toggleTheme">
    <!-- 太阳图标（深色模式下显示，点击切回浅色） -->
    <svg v-if="isDark" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round">
      <circle cx="12" cy="12" r="4" />
      <path d="M12 2v2M12 20v2M4.93 4.93l1.41 1.41M17.66 17.66l1.41 1.41M2 12h2M20 12h2M4.93 19.07l1.41-1.41M17.66 6.34l1.41-1.41" />
    </svg>
    <!-- 月亮图标（浅色模式下显示，点击切换深色） -->
    <svg v-else viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" />
    </svg>
  </button>

  <RouterView />
</template>

<style scoped>
.theme-toggle {
  position: fixed;
  top: 16px;
  right: 16px;
  z-index: 100;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--el-border-color);
  border-radius: 50%;
  background: var(--el-bg-color);
  color: var(--el-text-color-primary);
  cursor: pointer;
  transition: all 0.3s ease;
}

.theme-toggle:hover {
  border-color: var(--el-color-primary);
  color: var(--el-color-primary);
}
</style>

<style>
/* 页面背景跟随主题 */
body {
  margin: 0;
  background-color: #f5f7fa;
  transition: background-color 0.3s ease;
}

html.dark body {
  background-color: #0d0d0d;
}
</style>
