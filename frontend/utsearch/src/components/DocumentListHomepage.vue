<template>
  <div id="boxDocs">
    <div id = "docs">
    <button id="sub" v-for="document in documents" @click=redirect(document.path)> {{document.name}} </button>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'DocumentListHomepage',
      data () {
        return {
          // variables
          toggle: false,
          // documents
          document: '',
          documents: []
        }
    },
    methods: {
      displayDocuments() {
        var btnViewDocs = document.getElementById("showDocuments");
        btnViewDocs.innerHTML = "";
        btnViewDocs.setDisabled = true;
        btnViewDocs.style.visibility = "hidden";
        btnViewDocs.style.display = "none";

        var vm = this
          if (vm.documents.length == 0) {
            // no documents in list, push "No Uploaded Documents" to display
            vm.documents.push({ name: "No Uploaded Documents", path: null })
          }
          vm.toggle = !vm.toggle
      },
      redirect(filename) {
        if(filename != null) {
            // redirect to java servlet to download the file with filename
            var s = '/display?file=' + filename
            window.location.href = s
        }
      },
      getDocumentsData() {
        axios.get('/userdocuments', {
          params: {
            token: this.$cookies.get('Token')
          }
        }).then(response => this.documents = response.data.results);
      }
    }, 
    created() {
      this.getDocumentsData()
    }
};

</script>

<style scoped="">
  #showDocuments{
    font-size: 20px;
    background-color: #FAFAFA;
    width: 400px;
    height: 40px;
    color: black;
  }
  
  #boxDocs {
    background-color: rgba(150, 150, 150, 0.15);
    height: 400px;
    width: 410px;
    text-align: left;
    overflow-y: scroll;
  }

  #sub {
    font-size: 17px;
    width: 400px;
    height: 30px;
    margin: 5px;
    color:black;
    background-color: #FAFAFA;
  }

#docs {
  position: inherit;
  width:0px;
  display: inline-block;
}

</style>