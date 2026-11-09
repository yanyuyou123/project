// 导入路由核心函数
import { createRouter, createWebHistory } from 'vue-router'
// 导入页面组件
import Clazz from '../views/clazz/index.vue'
import Dept from '../views/dept/index.vue'
import Emp from '../views/emp/index.vue'
import Log from '../views/log/index.vue'
import Stu from '../views/stu/index.vue'
import Login from '../views/login/login.vue'
import StuReport from '../views/report/stu/index.vue'
import EmpReport from '../views/report/emp/index.vue'
import Index from '../views/index/index.vue'

// 导入布局组件（你的主布局页面，包含el-header+el-aside）
import Layout from '../views/layout/index.vue'

// 路由规则
const routes = [
  {
    path: '/',
    component: Layout, // 外层布局（顶部+侧边栏，不变）
    redirect: '/index', // 默认打开，重定向到首页
    // 嵌套路由：主体内容区域切换
    children: [
      {path: 'index', component: Index,name: 'index'},
      {path: 'stu', component: Stu,name: 'stu'},
      {path: 'clazz',component: Clazz,name: 'clazz'},
      {path: 'dept',component: Dept,name: 'dept'},
      {path: 'emp',component: Emp,name: 'emp'},
      {path: 'log', component: Log, name: 'log'},
      {path: 'stuReport',component: StuReport,name: 'stuReport'},
      {path: 'empReport',component: EmpReport,name: 'empReport'}
    ]
  },
  {
    path: '/login',
    component: Login,
    name: 'login'
  },
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(), // history模式（不带#）
  routes
})

// 全局前置守卫：未登录访问任何页面都跳转到登录页
router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    return next()
  }
  const token = localStorage.getItem('token')
  if (!token) {
    return next('/login')
  }
  next()
})

// 导出路由实例
export default router
