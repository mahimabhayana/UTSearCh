<template>
  <div id="addCourse">
    <button @click="showDialog = true">Add course</button>
    <AddCourseDialog v-if="showDialog" @close="showDialog = false" />
  </div>
  

</template>

<script>
  import AddCourseDialog from './AddCourseDialog.vue'
  export default {
    name: 'AddCourseBtn',
    data() {
      return {
        showDialog: false
      }
    },
    
    components: {
      AddCourseDialog
    }
  }

</script>


<style scoped="">
  #addCourse{
  font-size: 20px;
  font-weight: bold;
  background-color: #008000;
  width: 130px;
  height: 40px;
  line-height:40px;
  color: white
  }


</style>