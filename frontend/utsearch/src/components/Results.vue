<template>
<div>
  <Toolbar />
  <div id="results">
    <h1>Results for : "{{ query }}"</h1>
    <v-app id="inspire"><!-- 
      <v-container fluid grid-list-md> -->
      <v-layout row wrap>
        <v-flex xs12 sm12 md12>
          <v-data-table
        :headers="headers"
        :items="documents"
        :pagination.sync="pagination"
        :total-items="totaldocuments"
        :loading="loading"
        :rows-per-page-items="[8,10,25]"
        class="document-table"
      >
        <template slot="items" slot-scope="props">
            <td style="{height: 10px}"><a :href="'/display?file=' + props.item.title">{{ props.item.name }}</a></td>
            <td class="text-xs-left">{{ props.item.uploader }}</td>
            <td class="text-xs-left">{{ props.item.doc_type }}</td>
            <td class="text-xs-left">{{ props.item.doc_upload_time.split(" ")[0] }}</td>
          <!-- <td class="text-xs-left">{{ props.item.rating }}</td> -->
        </template>
      </v-data-table>
        </v-flex>
          </v-layout>
  </v-app>
  </div>
</div>
</template>

<script>
  import Toolbar from './Toolbar.vue'
  import axios from 'axios';

  export default {
    name: 'Results',
    props: ['query'],
     data () {
    return {
      documents: [],
      totaldocuments: 0,
      desserts: [],
      loading: true,
      pagination: {},
      query: '',
      headers: [
        {
          text: 'Document title',
          align: 'left',
          sortable: true,
          value: 'name'
        },
        { text: 'Uploaded by', value: 'uploader', sortable: true },
        { text: 'Document type', value: 'doc_type', sortable: true },
        { text: 'Upload date', value: 'doc_upload_time', sortable: true },
        // { text: 'Rating', value: 'rating', sortable: true },

      ]
    }
  },
  watch: {
    pagination: {
      handler () {
        this.getDocuments()
      },
      deep: true
    }
  },
  mounted () {
    // remove background images
    document.body.style.background = "#EAEAEA";
    this.getdocuments();
  },
  methods: {
    downloadFile(path) {
      window.location.href = '/display?file=' + path;
    },
    getDataFromApi () {
      this.loading = true
      return new Promise((resolve, reject) => {
        const { sortBy, descending, page, rowsPerPage } = this.pagination

        let documents = this.documents
        const total = documents.length

        if (this.pagination.sortBy) {
          documents = documents.sort((a, b) => {
            const sortA = a[sortBy]
            const sortB = b[sortBy]

            if (descending) {
              if (sortA < sortB) return 1
              if (sortA > sortB) return -1
              return 0
            } else {
              if (sortA < sortB) return -1
              if (sortA > sortB) return 1
              return 0
            }
          })
        }

        if (rowsPerPage > 0) {
          documents = documents.slice((page - 1) * rowsPerPage, page * rowsPerPage)
        }

        setTimeout(() => {
          this.loading = false
          resolve({
            documents,
            total
          })
        }, 1000)
      })
    },
    getdocuments() {
      const url = '/search' + this.$route.fullPath.replace("/results", "");
      axios.get(url).then((response) => {
          this.query = response.data.query
          console.log(response.data);
                    this.documents = response.data.results;
          this.getDataFromApi()
          .then(data => {
            this.totaldocuments = data.total
            this.documents = data.documents
          });
        });
    }
  },
    components: {
      Toolbar
    }
  };

</script>

<style>
  body {
    background-color: #EAEAEA;
  }

  #results {
    color: #033457;
    margin: 30px 8vw;
    text-align: left;
    background-color: transparent;
  }

  #results > h1 {
    font-size: 30px;
  }

  #inspire {
    background-color: transparent;
    margin-top: 15px;
  }

  .document-table {
    margin-bottom: 20px;
  }

  #inspire h4 {
    font-size: 20px;
  }

  #inspire > div {
    padding: 0 !important;
  }

  #documents {
    font-size: 20px;
    padding-left: 10px;
    display: flex;
    flex-direction: column;
  }

  #documents ul {
    padding-left: 3px;
    list-style: disc;
  }

  .card {
    margin: 0 8px 8px 8px;
    font-size: 20px;
  }

  .card a {
    color: green;
    text-decoration: none;
    font-weight: 450;
  }

  .card a:hover {
    text-decoration: underline;
  }

  .card-title {
    font-weight: bold;
    color: #033457;
  }

  .scroll {
    overflow-y: auto;
    height: 140px;
  }

  .document {
    position: relative;
  }

  .document .document-title {
    visibility: hidden;
    line-height: 9px;
    font-size: 10px;
    margin: -3px 0 0 5px;
    max-width: 100px;
    background-color: rgba(0, 0, 0, 0.4);
    color: #fff;
    text-align: center;
    padding: 8px 10px;
    border-radius: 2px;

    /* Position the tooltip */
    position: absolute;
}

  .document:hover .document-title {
    visibility: visible;
  }

  .documents li {
    z-index: 1;
  }

</style>
