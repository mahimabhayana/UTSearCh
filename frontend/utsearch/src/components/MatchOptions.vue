<template>
  <div>
    <select id="mOptions" v-model="match" v-on:change="changed">
      <option disabled value="">Match Type</option>
      <option id="option1">Word Match</option>
      <option id="option2">Exact Match</option>
      <option id="option3">No Match</option>
    </select>
  </div>
</template>

<script>
import { Bus } from '../main.js'
 export default {
  name: 'MatchOptions',
  data() {
    return {
      match: 'Word Match'
    }
  },
  created() {
    this.changeMatch()
  },
  methods: {
    changeMatch() {
      Bus.$emit("matchChanged", this.match)
    },
    changed() {
      Bus.$emit("matchChanged", this.match)
    }
  }
 };

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .queryOptions{
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    background-color: white;
    text-align: centre;
    font-size: 20px;
    margin-top: 15px;
  }
  
  #mOptions{
  font-weight: bold
  
  }
  
  #option1{
    color: #000000;
  }
  
   #option2{
    color: #000000;
  }
  
   #option3{
    color: #000000;
  }

</style>
