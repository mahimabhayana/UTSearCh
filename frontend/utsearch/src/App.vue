<template>
  <div id="app">
    
    <router-view></router-view>
    <br>
    
  </div>
</template>

<script>
//import Landing from './components/Landing.vue'
export default {
  name: 'app',
  components: {
    //Landing
    
  }
};
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #fff;
  }

  #title {
    font-size: 80px;
    margin-bottom: -20px;
  }

  a {
    color:white;
  }

</style>
