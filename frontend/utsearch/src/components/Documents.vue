<template>
<div>
  <Toolbar />
  <div id="results">
    <h1>Documents uploaded by <i>{{ this.$cookies.get('Name') }}</i></h1>
    <v-app id="inspire"><!-- 
      <v-container fluid grid-list-md> -->
      <v-layout row wrap>
        <v-flex xs12 sm12 md12>
          <v-data-table
        :headers="headers"
        :items="documents"
        :pagination.sync="pagination"
        :total-items="totalDocuments"
        :loading="loading"
        :rows-per-page-items="[8,10,25]"
        class="document-table"
      >
        <template slot="items" slot-scope="props">
          <td style="{height: 10px}"><a :href="'/display?file=' + props.item.path">{{ props.item.name }}</a></td>
          <td class="text-xs-left">{{ props.item.doc_type }}</td>
          <td class="text-xs-left">{{ props.item.doc_upload_time }}</td>
          <td class="text-xs-left">
          <v-icon
            small
            @click="deleteDocument(props.item.path)"
          >
            delete
          </v-icon>
        </td>
        </template>
      </v-data-table>
        </v-flex>
          </v-layout>
        </v-flex>
      </v-layout>
  </v-app>
  </div>
</div>
</template>

<script>
  import axios from 'axios';
  import Toolbar from './Toolbar.vue'

  export default {
    name: 'Documents',
     data () {
    return {
      responseDocs: [],
      documents: [],
      totalDocuments: 0,
      loading: true,
      pagination: {},
      headers: [
        {
          text: 'Document title',
          align: 'left',
          sortable: true,
          value: 'name'
        },
        { text: 'Document type', value: 'doc_type', sortable: true },
        { text: 'Upload date', value: 'doc_upload_time', sortable: true },
        { text: '', value: 'actions', sortable: false }
      ]
    }
  },
  watch: {
    pagination: {
      handler () {
        this.getDocumentsData()
      },
      deep: true
    }
  },
  mounted () {
    this.getDocumentsData();

  },
  methods: {
    deleteDocument(fileName) {
      if (confirm('Are you sure you want to delete the file "' + fileName + '"?' )) {
        axios.post('/deleteDocument?token=' + 
          this.$cookies.get('Token') + '&filename=' + fileName)
        .then(this.getDocumentsData());
      }
    },
    getDocumentsData() {
      axios.get('/userdocuments', {
          params: {
            token: this.$cookies.get('Token')
          }
        }).then((response) => {
          this.responseDocs = response.data.results;
          this.getDataFromApi()
          .then(data => {
            this.totalDocuments = data.totalDocuments
            this.documents = data.documents
          });
        });
      }, 
    getDataFromApi () {
      this.loading = true
      return new Promise((resolve, reject) => {
        const { sortBy, descending, page, rowsPerPage } = this.pagination

        let documents = this.responseDocs
        const totalDocuments = documents.length

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
            totalDocuments
          })
        }, 1000)
      })
    },
  },
    components: {
      Toolbar
    }
  };

</script>

<style>

</style>
