import Vue from 'vue'
import App from './App.vue'
import router from "./router";
import axios from './axios/interceptors';
import './plugins/element.js'

Vue.config.productionTip = false
Vue.prototype.$axios = axios;


new Vue({
  router,
  axios,
  render: h => h(App)
}).$mount('#app');



