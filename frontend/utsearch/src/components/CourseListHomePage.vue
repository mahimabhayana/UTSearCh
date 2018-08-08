<template>
  <div id="boxCourses">
    <div id = "course-container">
    <button v-if="courses.length > 0" id="sub" v-for="course in courses" @click=redirect(course)> {{ course }}</button>
    </div>
  </div>

</template>

<script>
  import axios from 'axios'

  export default {
    name: 'CourseListHomepage',
      data () {
        return {
          // courses
          course: '',
          courses: []
        }
      },
    methods: {
      displayCourses() {
        var numCourses = this.courses.length;

        var btnViewCourses = document.getElementById("showCourses");
        btnViewCourses.innerHTML = "";
        btnViewCourses.setDisabled = true;
        btnViewCourses.style.visibility = "hidden";
        btnViewCourses.style.display = "none";

        var vm = this
          // if course array is not empty display courses
          if (vm.courses.length > 0 && vm.courses[0].course.length > 0) {
          } else {
            // else display 'No Courses'
            vm.courses[0].course = "No Courses"
          }
          vm.toggle = !vm.toggle
      },
     redirect(link) {
        if (link != "No Courses") {
          const route = "/course/" + link;
          this.$router.push(route);

        }
      },
      getCourseData() {
        axios.get('/usercourses', {
            params: {
              token: this.$cookies.get('Token')
            }
          }).then(response => this.courses = response.data.courses);
      }
    },
    created() {
      // on page load get data
      this.getCourseData()
    }
  };
</script>

<style scoped="">
  #showCourses{
    font-size: 20px;
    background-color: #FAFAFA;
    width: 370px;
    height: 40px;
    color: black;
  }
  
  #boxCourses {
    background-color: rgba(150, 150, 150, 0.15);
    height: 400px;
    width: 380px;
    text-align: left;
    overflow-y: scroll;
  }

  #sub {
    font-size: 17px;
    width: 370px;
    height: 30px;
    margin: 5px;
    color:black;
    background-color: #FAFAFA;
  }


</style>