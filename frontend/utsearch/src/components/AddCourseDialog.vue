<template>
  <div class="dialog-backdrop">
    <div class="addCourse-dialog">
      <header class="dialog-header">
        <slot name="header">
          Add a course
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
            <p>Enter the course details</p>
          </div>
          <div class="field">
            Course code: <input type="text" name="code" v-model="courseCode">
              <p class="validation-error" v-if="true"></p>
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

  import VeeValidate from 'vee-validate'

  export default {
    name: 'AddCourseDialog',
      data() {
        return {
          cCode: '',
          courseInstructor: ''
        }
      },
    methods: {
      close() {
        this.$emit('close');
      },
      handleSubmit(e) {
        e.preventDefault();
        // If the user has entered a course code 
        if (this.courseCode) {
            var token = this.$cookies.get('Token')
            var xhr = new XMLHttpRequest()
            xhr.open('POST', '/courses')
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
            // send the course code entered and the token of the user
            xhr.send("course="+this.courseCode+"&token="+token)
            xhr.onload = function() {
              var response = xhr.responseText
              // Display the appropriate message to the user
              if(response === "access denied"){
                alert("Access denied! You have to be an instructor to add a course.")
              } else if(response == "something went wrong") {
                alert("Could not add course. Make sure the course code is unique")
              } else {
                alert("Course added!")
              }
            
            }
            xhr.error = function() {
              alert("Something went wrong. Please try again")
            }
            
            
        }
        // If the user clicked submit without entering the course code
        if (!this.courseCode) {
          alert('Please enter the course code.');
          return;
        }
      }
    },
    created() {
  
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
  
   .addCourse-dialog {
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

  select {
    font-size: 14px;
  }
</style>