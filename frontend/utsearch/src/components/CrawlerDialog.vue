<script>
  import axios from 'axios'
  import VeeValidate from 'vee-validate'

  export default {
    name: 'CrawlerDialog',
      data() {
        return {
          file: '',
          docCourse: '',
          docTitle: '',
          docType: ''
        }
      },
    methods: {
      close() {
        this.$emit('close');
      },
      handleSubmit(e) {
        e.preventDefault();
        
        if (this.crawlURL) {
            if (!this.crawlURL.includes("utoronto.ca")) {
                alert("Website to crawl must be a utoronto.ca URL.");
                return;
            }

            alert("About to crawl " + this.crawlURL + ". This could take a while.\nYou will be notified when the crawl is complete.");
            axios.get('/crawler?path=' + this.crawlURL + '&user=' + this.$cookies.get('Name') + '&token=' + this.$cookies.get('Token'))
            .then(() => {
                alert("Files from " + this.crawlURL + " uploaded!");
                this.close();
            })
            .catch(() => console.log('There was an error uploading the files. Please try again.'));
            return;
        }

        if (!this.crawlURL) {
          alert('Please enter a URL to crawl.');
          return;
        }
      }
    },
  };
</script>

<template>
  <div class="dialog-backdrop">
    <div class="crawl-dialog">
      <header class="dialog-header">
        <slot name="header">
          Crawl a website
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
            <p>Enter a URL to Crawl</p>
          </div>
          <div class="field">
            <!-- TODO: field verification; must be one of the courses offered at UTSC -->
            URL: <input type="text" name="url" v-model="crawlURL">
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
    z-index: 10000;
  }

  .crawl-dialog {
    width: 500px;
    height: 300px;
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
    cursor: pointer;
    font-weight: bold;
    color: #003358;
    background: transparent;
  }

  .btn-green {
    height: 35px;
    color: white;
    font-size:15px;
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

