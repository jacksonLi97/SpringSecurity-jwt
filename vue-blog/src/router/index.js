import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "../views/Login"
import Blog from "../views/Blog";
import Index from "../views/Index";

Vue.use(VueRouter)

  const routes = [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/blog',
      name: 'Blog',
      component: Blog,
      meta: { auth: true}
    },
    {
      path: '/',
      name: 'Index',
      component: Index,
      meta: {auth: false}
    }
  ]

  const router = new VueRouter({
    mode:'history',
    routes
  })

  //全局前置守卫（回调函数）
  router.beforeEach((to, from, next) => {
    if (to.meta.auth === true){
      if (localStorage.getItem("token")){
        next();
      }else {
        next('/login');
      }
    }else {
      next();
    }
  })

export default router
