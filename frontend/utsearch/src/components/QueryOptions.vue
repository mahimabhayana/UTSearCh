<template>
  <div class="queryOptions">
    <label class="query" for="option1">
    <input type="checkbox" id="option1" value="CrawlerDoc" v-model="checkedBox" v-on:change="changed">
      Crawler</label>
    <input type="checkbox" id="option2" value="Assignment/Exercise"  v-model="checkedBox" v-on:change="changed">
      <label for="option2">Assignments/Exercises    </label>
    <input type="checkbox" id="option3" value="Past exam"  v-model="checkedBox" v-on:change="changed">
      <label for="option3">Past Exams    </label>
    <input type="checkbox" id="option4" value="Notes"  v-model="checkedBox" v-on:change="changed">
      <label for="option4">Notes    </label>
    <input type="checkbox" id="option5" value="Test File"  v-model="checkedBox" v-on:change="changed">
      <label for="option5">Test    </label>
  </div>
</template>

<script>
import { Bus } from '../main.js'
 export default {
  name: 'QueryOptions',
  data(){
    return{
      checkedBox: []
    }
  },
  created() {
    this.changed()
  },
  methods: {
    changed() {
      Bus.$emit("checkedChanged", this.checkedBox)
    }
  }
 };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .queryOptions{
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    text-align: centre;
    font-size: 18px;
    font-weight: bold;
    margin-top: 15px;
  }

</style>

<style>
  input[type='checkbox'] {
    width:20px;
    height:20px;
    vertical-align: sub;
    margin: 0 5px;
}

</style>
