<template>
  <div id="enrolCourse">
    <button @click="showDialog = true">Enrol in course</button>
    <EnrolCourseDialog v-if="showDialog" @close="showDialog = false" />
  </div>


</template>

<script>
  import EnrolCourseDialog from './EnrolCourseDialog.vue'
  export default{
    name: 'EnrolCourseBtn',
    data() {
      return {
        showDialog: false
      }
    },
    
    components: {
      EnrolCourseDialog
    }
  }
  
  
  
  
</script>

<style scoped="">
   #enrolCourse{
   font-size: 15px;
    font-weight: bold;
    background-color: #033457;
    width: 130px;
    height: 40px;
    line-height:40px;
    color: white
  }
  
  
</style>