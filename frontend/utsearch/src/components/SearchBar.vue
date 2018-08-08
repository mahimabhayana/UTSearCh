<template>
  <div class="search">
    <input ref="query" type="text" color="black" size="50" @keyup.enter="search" class="q" placeholder="Type your query and hit enter"/>
  </div>
</template>

<script>
  //import axios from "axios"
  import MatchOptions from './MatchOptions.vue'
  import { Bus } from '../main.js'
  import axios from 'axios'

  export default {
    name: 'Search',
    components: {
      MatchOptions
    },
    data() {
      return {
        match: '',
        checkedBox: []
      }
    },
    created() {
      Bus.$on('matchChanged', (match) => {
        this.match = match
      }),
      Bus.$on('checkedChanged', (box) => {
        this.checkedBox = box
      })
          },

    methods: {
      search() {
        var href = "/results?query=" + this.$refs.query.value + "&match=" + this.match + "&checkedBox="
        for (var i = 0; i < this.checkedBox.length; i++) {
          href += this.checkedBox[i]
        }
        this.$router.push(href)
      }
    }
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  input[type="text"] {
    background-color : #ffffff;
    color: #000000;
  }

  .q {
    width: 35vw;
    height: 40px;
  }
  
</style>
