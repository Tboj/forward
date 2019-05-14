import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)
// route level code-splitting
// this generates a separate chunk (about.[hash].js) for this route
// which is lazy-loaded when the route is visited.
// component: () => import(/* webpackChunkName: "about" */ './views/About.vue')

// 创建通用路由
const createRouter = () => new Router({
    mode: 'hash',
    routes: [{
        path: '/',
        name: 'item',
        component: () => import('@/views/item'),
    }]
});

const router = createRouter();


export default router
