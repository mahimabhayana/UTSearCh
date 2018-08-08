import Vue from 'vue'
import Router from 'vue-router'
import Landing from './components/Landing.vue'
import About from "./components/About.vue"
import UploadDialog from './components/UploadDialog'
import UploadPage from './components/UploadPage'
import UserProfile from './components/UserProfile.vue'
import Results from './components/Results.vue'
import CourseDocuments from './components/CourseDocuments.vue'
import ProfDocuments from './components/ProfDocuments.vue'
import Documents from './components/Documents.vue'
import SearchBarHomepage from './components/SearchBarHomepage.vue'
import Register from './components/Register.vue'
import Course from './components/Course.vue'
import Professor from './components/Professor.vue'

import HomePage from "./components/HomePage.vue"

Vue.use(Router)

export default new Router({
    //mode: 'history',
    routes: [
        {
            // goto just / it will display Landing component
            path: '/',
            name: 'Landing',
            component: Landing
        },
        {
            path: '/home',
            name: 'HomePage',
            component: HomePage
        },
        {
            path: '/about',
            name: 'About',
            component: About
        },
        {
            path: '/userprofile',
            name: 'UserProfile',
            component: UserProfile
        },
        {
            path: '/results',
            name: 'Results',
            component: Results,
            props: (route) => ({ query: route.query.q })
        },
        {
            path: '/courseDocuments/:course',
            name: 'Course Documents',
            component: CourseDocuments,
            props: true
        },
        {
            path: '/profDocuments/:professor',
            name: 'Professor Documents',
            component: ProfDocuments,
            props: true
        },
        {
            path: '/documents',
            name: 'My Documents',
            component: Documents
        },
        {
            path: '/searchBarHomepage',
            name: 'Search Bar Homepage',
            component: SearchBarHomepage
        },
        {
            path: '/course/:code',
            name: 'Course',
            component: Course,
            props: true
        },
        {
            path: '/professor/:professor',
            name: 'Professor',
            component: Professor,
            props: true
        },
        {
            path: '/register',
            name: 'Register',
            component: Register,
            // 'sub', 'email', 'firstName', 'lastName'
        }
        
    ]
})
