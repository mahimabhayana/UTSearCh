<template>
<div>
  <Toolbar />
  <div id="results">
    <h1>Documents uploaded by <i>{{ this.$route.params.professor }}</i></h1>
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
          <td style="{height: 10px}"><a :href="'/display?file=' + props.item.filename">{{ props.item.name }}</a></td>
          <td class="text-xs-left">{{ props.item.course }}</td>
          <td class="text-xs-left">{{ props.item.doctype }}</td>
          <td class="text-xs-left">{{ props.item.datetime }}</td>
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
      documents: [],
      profDocuments: [],
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
        { text: 'Course', value: 'course', sortable: true },
        { text: 'Document type', value: 'doctype', sortable: true },
        { text: 'Upload date', value: 'datetime', sortable: true },
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
    getDocumentsData() {
      axios.get('/userData', {
        params: {
          user: this.$route.params.professor
        }
      }).then(response => {
        console.log(response.data.docs)
        this.profDocuments = response.data.Docs;
        if (response.data.Type === "Instructor") {
          this.getDataFromApi().then(data => {
            this.documents = data.documents;
            this.totalDocuments = data.totalDocuments;
          })
        }
        
      });
    }, 
    getDataFromApi () {
      this.loading = true
      return new Promise((resolve, reject) => {
        const { sortBy, descending, page, rowsPerPage } = this.pagination

        let documents = this.profDocuments
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
