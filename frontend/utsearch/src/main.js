import Vue from 'vue'
import App from './App.vue'
import router from './router'
import GSignInButton from 'vue-google-signin-button'
import VeeValidate from 'vee-validate'
import VueCookies from 'vue-cookies'
import Vuetify from 'vuetify'
import VueGoogleCharts from 'vue-google-charts'
import 'vuetify/dist/vuetify.min.css'

Vue.use(Vuetify, {
  theme: {
    primary: "#033457",
  }
});

Vue.use(GSignInButton)
Vue.use(VeeValidate);
Vue.use(VueCookies)
Vue.use(VueGoogleCharts)

Vue.config.productionTip = false
export const Bus = new Vue({})
// data stored in session will persist between tabs and browser instances

var options = {
    persist: true
}

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
