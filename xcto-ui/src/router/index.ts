import { createRouter, createWebHistory } from 'vue-router'
import HomeView from "@/views/HomeView.vue";
import QuestionView from "@/views/QuestionView.vue";
import CourseSelectView from "@/views/CourseSelectView.vue";
import LoginView from "@/views/LoginView.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
    {
      path: '/',
      name: 'home',
      component: CourseSelectView,
    },
    {
      path: '/courses',
      name: 'courses',
      component: CourseSelectView,
    },
    {
      path: '/question',
      name: 'question',
      component: QuestionView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
  ]
})

// 登录守卫：未登录一律跳转登录页，登录后访问 /login 回首页
router.beforeEach((to) => {
  const token = localStorage.getItem('satoken')
  if (!token && to.name !== 'login') {
    return { name: 'login', query: { redirect: to.fullPath } }
  }
  if (token && to.name === 'login') {
    return { name: 'home' }
  }
})

export default router
