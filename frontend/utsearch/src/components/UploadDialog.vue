<script>
  import axios from 'axios'
  import VeeValidate from 'vee-validate'

  export default {
    name: 'UploadDialog',
      data() {
        return {
          file: '',
          docCourse: '',
          docTitle: '',
          docType: '',
          courses: []
        }
      },
    methods: {
      close() {
        this.$emit('close');
      },
      handleSubmit(e) {
        e.preventDefault();

        this.file = this.$refs.file.files[0];

        this.$validator.validateAll();

        if (!(this.file && this.docCourse && this.docType && this.docTitle)) {
          return;
        }

        let formData = new FormData();
        formData.append('file', this.file);
        formData.append('course', this.docCourse);
        formData.append('title', this.docTitle);
        formData.append('document-type', this.docType);
        formData.append('user',this.$cookies.get('Name'));
        formData.append('token', this.$cookies.get('Token'));

        axios.post('http://localhost:8081/upload',
          formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }
        )
        .then(() => {
          alert(`File "${this.file.name}" uploaded!`);
          this.close();
        })
        .catch(() => alert('There was an error uploading the file. Please ensure your file is of type html, txt, or pdf and try again.'));

      }
    },
    created() {
      // get all courses in the database
      axios.get('/courses').then(response => this.courses = response.data);
    }
  };
</script>

<template>
  <div class="dialog-backdrop">
    <div class="dialog">
      <header class="dialog-header">
        <slot name="header">
          Upload
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
            Document title: <input type="text" name="title" v-model="docTitle" v-validate="'required'">
            <p class="validation-error" v-if="true">{{ errors.first('title') }}</p>
          </div>
          <div class="field">
            <!-- TODO: field verification; must be one of the courses offered at UTSC -->
            Course code:
            <select name="course" v-model="docCourse" v-validate="'required'">
            <option disabled value="">Please select one</option>
              <option v-for="course in courses">
                {{ course }}
              </option>
            </select>
            <p class="validation-error" v-if="true">{{ errors.first('course') }}</p>
          </div>
          <div class="field">
            Document type:
            <select name="type" v-model="docType" v-validate="'required'">
              <option disabled value="">Please select one</option>
              <option>Notes</option>
              <option>Past exam</option>
              <option>Assignment/Exercise</option>
            </select>
            <p class="validation-error" v-if="true">{{ errors.first('type') }}</p>
          </div>
          <div class="field">
            Document: <input type="file" name='file' ref='file' v-validate="'required'"/>
            <p class="validation-error" v-if="true">{{ errors.first('file') }}</p>
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

<style>
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

  #s1 {
    font-size: 20px;
    border: 2px solid #003358;
  }

</style>

