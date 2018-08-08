<template>
<div>
  <Toolbar />
  <div id="results">
    <h1>Documents related to course: <i>{{ this.$route.params.course }}</i></h1>
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
          <td class="text-xs-left">{{ props.item.doctype }}</td>
          <td class="text-xs-left">{{ props.item.uploadertype }}</td>
          <td class="text-xs-left">{{ props.item.uploader }}</td>
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
  import axios from 'axios'
  import Toolbar from './Toolbar.vue'

  export default {
    name: 'CourseDocuments',
     data () {
    return {
      documents: [],
      responseDocuments: [],
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
        { text: 'Document type', value: 'doctype', sortable: true },
        { text: 'Uploader type', value: 'uploadertype', sortable: true },
        { text: 'Uploader', value: 'uploader', sortable: true },
        { text: 'Upload date', value: 'datetime', sortable: true },
      ]
    }
  },
  watch: {
    pagination: {
      handler () {
        this.getCourse()
      },
      deep: true
    }
  },
  mounted () {
    this.getCourse()
  },
  methods: {
    redirect(filename) {
      if(filename != null) {
          // redirect to java servlet to download the file with filename
          var s = '/display?file=' + filename
          window.location.href = s
      }
    },
    getCourse() {
      console.log('route!', this.$route.params.course)
    axios.get('/courseData', {
        params: {
            course: this.$route.params.course
        }
    }).then(response => {
        this.responseDocuments = response.data.InstructorDocs.concat(response.data.StudentDocs)
        this.getDataFromApi().then(data => {
          this.documents = data.items;
          this.totalDocuments = data.total;
        })
    });
  },
    getDataFromApi () {
      this.loading = true
      return new Promise((resolve, reject) => {
        const { sortBy, descending, page, rowsPerPage } = this.pagination

        let items = this.responseDocuments
        const total = items.length

        if (this.pagination.sortBy) {
          items = items.sort((a, b) => {
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
          items = items.slice((page - 1) * rowsPerPage, page * rowsPerPage)
        }

        setTimeout(() => {
          this.loading = false
          resolve({
            items,
            total
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
