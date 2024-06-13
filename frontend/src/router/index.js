import Vue from 'vue'
import VueRouter from 'vue-router'
import HomePage from '../views/HomePage.vue'
import {jwtDecode} from 'jwt-decode';

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'HomePage',
    component: HomePage
  },
  {
    path: '/most-read',
    name: 'MostRead',
    meta: {
      authRequired: false,
    },
    component: () => import(/* webpackChunkName: "most-read" */ '../views/MostRead.vue')
  },
  
  {
    path: '/destinations',
    name: 'AllDestinations',
    meta: {
      authRequired: true,
    },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/destinations/AllDestinations.vue')
  },
  {
    path: '/destinations/new',
    name: 'NewDestination',
    meta: {
      authRequired: true,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/destinations/NewDestination.vue')
  },
  {
    path: '/articles',
    name: 'AllArticles',
    meta: {
      authRequired: true,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/articles/AllArticles.vue')
  },
  {
    path: '/articles/new',
    name: 'NewArticle',
    meta: {
      authRequired: true,
    },
    props: route => ({ destinationId: route.query.destinationId }),
    component: () => import(/* webpackChunkName: "about" */ '../views/articles/NewArticle.vue')
  },
  { // ArticlesByDestination
    path: '/articles/:destinationId',
    name: 'ArticlesByDestination',
    meta: {
      authRequired: true,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/articles/ArticlesByDestination.vue')
  },
  {
    // EditDestination
    path: '/destinations/:id',
    name: 'EditDestination',
    meta: {
      authRequired: true,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/destinations/EditDestination.vue')
  },
  {
    // editArticle
    path: '/articles/:id',
    name: 'EditArticle',
    meta: {
      authRequired: true,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/articles/EditArticle.vue')
  },
  {
    // article Dtails
    path: '/articledetails/:id',
    name: 'ArticleDetails',
    meta: {
      authRequired: false,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/articles/ArticleDetails.vue')
  },
  {
    // articles by activity
    path: '/articles/activity/:activity',
    name: 'ArticlesByActivity',
    meta: {
      authRequired: false,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/articles/ArticlesByActivity.vue')
  },
  {
    // all users
    path: '/users',
    name: 'AllUsers',
    meta: {
      authRequired: true,
      checkInactiveStatus: true
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/users/AllUsers.vue')
  },
  {

    // edit user
    path: '/users/:id',
    name: 'EditUser',
    meta: {
      authRequired: true,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/users/EditUser.vue')
  },
  {
    // add new user
    path: '/usersnew/new',
    name: 'NewUser',
    meta: {
      authRequired: true,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/users/NewUser.vue')
  },
  {
    path: '/inactive-status',
    name: 'InactiveStatus',
    component: () => import('../views/InactiveStatus.vue'),
    meta: {
      authRequired: true
    }
  },  
  {
    // Guest Destination
    path: '/guestdestination/:destinationId',
    name: 'GuestDestination',
    meta: {
      authRequired: false,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/GuestDestination.vue')
  },
  {
    path: '/login',
    name: 'LoginPage',
    component: () => import(/* webpackChunkName: "about" */ '../views/LoginPage.vue')
  }
]

const router = new VueRouter({
  routes
})


router.beforeEach((to, from, next) => {
  if (to.meta.authRequired) {
    const jwt = localStorage.getItem('jwt');

    if (!jwt) {
      next({ name: 'LoginPage' });
      return;
    }

    const payload = JSON.parse(atob(jwt.split('.')[1]));
    const expDate = new Date(payload.exp * 1000);
    if (expDate < new Date()) {
      next({ name: 'LoginPage' });
      return;
    }

    if (to.meta.checkInactiveStatus) {
      console.log('checkInactiveStatus');
      const decoded = jwtDecode(jwt);
      fetch(`http://localhost:8080/api/users/${decoded.userId}`, {
        headers: {
          'Authorization': `Bearer ${jwt}`
        }
      })
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then(data => {
          if (data.userType === 'EDITOR') {
            console.log(data.status);
            next({ name: 'InactiveStatus' }); // redirect to inactive status page
          } else {
            next();
          }
        })
        .catch(() => {
          next({ name: 'InactiveStatus' }); // redirect to inactive status page on error
        });
    } else {
      next();
    }
  } else {
    next(); // allow to enter the route
  }
});



export default router
