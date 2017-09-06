import Vue from 'vue'
import VueStash from 'vue-stash'
import App from './App.vue'

Vue.use(VueStash)

new Vue({
  el: '#app',
  data: {
    store: {
      tos: [],
      ccs: [],
      bccs: []
    }
  },
  render: h => h(App)
})
