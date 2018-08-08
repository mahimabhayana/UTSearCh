<template>
  <div class="dialog-backdrop">
    <div class="enrolCourse-dialog">
      <header class="dialog-header">
        <slot name="header">
          Enrol in a course
          <button
            type="button"
            class="btn-close"
            @click="close()">
            x
          </button>
        </slot>
      </header>
      <section class="dialog-body">
        <form @submit.prevent="handleSubmit">
          <div class="field">
            <p>Select the course code for the course you want to enrol in</p>
          </div>
          <div class="field">
            Course code:
            <select name="type" v-model="course" v-validate="'required'">
              <option disabled value="">Select a course</option>
              <option v-for="course in courses" :key='course'> 
                  {{ course }}
              </option>
            </select>
            <p class="validation-error" v-if="true">{{ errors.first('type') }}</p>
          </div>
          <button
              class="btn-green submit is-primary"
              type="submit">
            Submit
          </button>
        </form>
      </section>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'EnrolCourseDialog',
    data() {
      return {
        courses: [],
        course: ''
      }
    }, 
    
    methods: {
      close() {
        this.$emit('close');
      },
      handleSubmit(e) {
        e.preventDefault();
        // If the user has entered a course code 
        if (this.course) {
            // Call the backend to add this course to the user's list of courses
            var token = this.$cookies.get('Token')
            var xhr = new XMLHttpRequest()
            xhr.open('POST', '/subscribeCourse')
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
            xhr.send("course="+this.course+"&token="+token)
            xhr.onload = function() {
              var status = xhr.status
              // Display message to the user
              if(status === 200) {
                alert('Enrolment to the course was successful')
                this.$emit('close')
              } else if(status === 500){
                alert('Something went wrong! Please try again')
              } else {
                alert('You are already enrolled in this course!')
              }
            }
        }
        // If the user clicked submit without entering the course code
        if (!this.course) {
          alert('Please select a course code.');
          return;
        }
      }
    },
    created() {
      // get all courses in the database
      axios.get('/courses').then(response => this.courses = response.data);      
    }
  };
  
</script>

<style scoped="">
  .dialog-backdrop {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 30;
  }

  .enrolCourse-dialog {
  width: 500px;
  height: 300px;
  background: #FFFFFF;
  overflow-x: auto;
  display: flex;
  flex-direction: column;
  color: black;
  }

  .dialog {
  width: 500px;
  height: 450px;
  background: #FFFFFF;
  overflow-x: auto;
  display: flex;
  flex-direction: column;
  color: black;
  }

  .dialog-header {
  padding: 20px;
  display: flex;
  font-size: 25px;
  background-color: #E3E3E3;
  border-bottom: 1px solid #eeeeee;
  color: #003358;
  justify-content: space-between;
  }

  .dialog-body {
  position: relative;
  padding: 20px 20px;
  height: 300px;
  display: inline-block;
  text-align: left;
  }

  .field {
  font-weight: bold;
  font-size: 20px;
  margin: 5px 2px;
  }

  input[type="text"] {
  border: 0;
  outline: 0;
  border-bottom: 1px solid rgba(0, 0, 0, .2);
  font-size: 15px;
  }

  .btn-close {
  border: none;
  font-size: 20px;
  line-height: 0.6;
  cursor: pointer;
  font-weight: bold;
  color: #003358;
  background: transparent;
  }

  .btn-green {
  height: 35px;
  color: white;
  font-size:15px;
  line-height: 1.9;
  background: #003358;
  border: 4px solid #003358;
  border-radius: 2px;
  }

  .validation-error {
  font-weight: lighter;
  font-size: 14px;
  }

  .submit {
  float: right;
  width: 80px;
  }


</style>