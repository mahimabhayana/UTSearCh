<template>
<div style="text-align: center;">
  <Toolbar />
  <div id="profile">
    <v-app id="inspire"><!-- 
      <v-container fluid grid-list-md> -->
      <v-layout row wrap>
        <v-flex xs12 sm12 md8>
          <v-card>
            <v-card-title primary-title>
          <div style="width: 90%; margin-left: 30px;">
            <img id="profile-picture" :src='pictureurl'/>
            <h1 style="font-size: 40px; margin-top: -5px; padding: 20px 0">{{ name }}</h1>
        </div>
        </v-card-title>
          </v-card>
            <v-flex xs12 row style="margin-top: 15px">
                <v-card>
                    <v-card-title primary-title style="padding: 10px 15px;">
                        <h2 id="MyFav"> File Types Uploaded </h2>
                    </v-card-title>
                    <GChart
                        type="PieChart"
                        :data="ftChartData"
                        :options="ftChartOptions"
                    />
                </v-card>
              </v-flex>
            <v-flex xs12 style="margin-top: 15px">
                <v-card>
                    <v-card-title primary-title style="padding: 10px 15px;">
                    <h2 id="MyFav"> Document Types Uploaded </h2>
                    </v-card-title>
                    <GChart
                        type="PieChart"
                        :data="dtChartData"
                        :options="dtChartOptions"
                    />
                </v-card>
              </v-flex>
        </v-flex>
        <v-flex xs12 sm12 md4>
          <v-layout row wrap>
            <v-flex xs12 class="card">
              <v-card height=246px color="white">
                <v-card-title class="card-title">
                  Courses
                </v-card-title>

                <div class="scroll" style="width: 90%; margin: auto;">
              
              <v-card-text style="padding: 0px">
                <ul id="documents">
                  <li v-for="course in courses">
                   • <a :href="'/#/course/' + course">{{ course }}</a>
                  </li>
                </ul>
              </v-card-text>

              </div>
              </v-card>
            </v-flex>
            <v-flex xs12 class="card">
              <v-card height=246px color="white">
                <v-card-title class="card-title">
                  <a style="color: #033457" href="/#/documents">Documents</a>
                </v-card-title>

                <div class="scroll" style="width: 90%; margin: auto;">
              
              <v-card-text style="padding: 0px">
                <ul id="documents">
                  <li v-for="document in sideDocuments" class="document">
                   • <a :href="'/display?file=' + document.filename">{{ document.name }}</a>
                  </li>
                </ul>
              </v-card-text>

              </div>
              </v-card>
            </v-flex>
              </v-layout>
            </v-flex>
          </v-layout>
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
    name: 'UserProfile',
        data () {
            return {
                sideDocuments: [],
                // variables
                toggle: this.$cookies.isKey('Name'),
                pictureurl: this.$cookies.get('Picture'),
                // %40 is @
                userEmail: this.$cookies.get('User'),
                // %20 is space
                name: this.$cookies.get('Name'),
                // courses
                course: '',
                courses: [{
                    name: "Intro to Software Engineering",
                    code: "CSCC01"
                }]
                ,
                // documents
                document: '',
                documents: [],

                // google charts
                ftChartData: [],
                ftChartOptions: {
                    chart:{
                        title: 'File Types Uploaded',
                        subtitle: 'HTML, PDF, and TXT'
                    }
                },
                dtChartData: [],
                dtChartOptions: {
                    chart:{
                        title: 'Document Types Uploaded',
                        subtitle: 'HTML, PDF, and TXT'
                    }
                },
            
    }
    },
    components: {
    Toolbar,
    },
   methods: {
        getData() {
            // get documents
            this.getDocuments()
            // get courses
            this.getCourses()
            console.log("called")
            var vm = this
            this.courses = []
            this.documents = []
            var xhr = new XMLHttpRequest();
            xhr.open('POST', 'http://localhost:8081/usercharts')
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
            xhr.send("token=" + this.$cookies.get('Token'))
            xhr.onload = function() {
            console.log('Response Text: ' + xhr.responseText)
            var rt = xhr.responseText
            //TXT:1,PDF:6,HTML:1;Assignment/Exercise:1,Past exam:2,Notes:5
            // split into filetype (ft) and doctype(dt)
            var splitter = rt.split(';')
            var ft = splitter[0].split(',')
            var dt = splitter[1].split(',')

            // creating data for file types uploaded
            var ftColumns = ["File Type", "Amount"]
            vm.ftChartData.push(ftColumns)
            for (var i = 0; i < ft.length; i++) {
                var split = ft[i].split(':')
                var temp = []
                temp.push(split[0])
                temp.push(parseInt(split[1]))
                vm.ftChartData.push(temp)
              }
              console.log(vm.ftChartData)

            // creating data for doc types uploaded
            var dtColumns = ["Doc Type", "Amount"]
            vm.dtChartData.push(dtColumns)
            for (var i = 0; i < dt.length; i++) {
                var split = dt[i].split(':')
                var temp = []
                temp.push(split[0])
                temp.push(parseInt(split[1]))
                vm.dtChartData.push(temp)
              }
              console.log(vm.dtChartData)
            }
    },

    getDocumentsData() {
        console.log('resp', 'skfsdk')

      axios.get('/userData', {
          params: {
            user: this.$cookies.get('Name')
          }
        }).then((response) => {
        console.log('her')
          this.sideDocuments = response.data.Docs;
          this.courses = response.data.CoursesTaken;
        });
      }, 
        getDocuments() {
            var vm = this
            this.documents = []
            var xhr = new XMLHttpRequest();
            xhr.open('GET', '/userdocuments')
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
            xhr.send("token=" + this.$cookies.get('Token'))
            console.log("Getting document data for " + vm.userEmail)
            xhr.onload = function() {
                console.log('User Documents: ' + xhr.responseText)
                var rt = xhr.responseText
                //parse data, data is in form
                //document1name,document2name;document1link,document2link
                // split data into two pieces, documents and document urls
                var splitter = rt.split(";")
                var documents = splitter[0]
                var documentUrls = splitter[1]
                // split each even further down into individual documents / documenturls
                if (documents.length > 0) {
                    var eachDocument = documents.split(",")
                    var eachDocumentUrl = documentUrls.split(",")
                    
                    // get document
                    for (var j = 0; j < eachDocument.length; j++) {
                        vm.documents.push({ document: eachDocument[j], filename:eachDocumentUrl[j] })
                    }
                }
            }
    },
        getCourses() {
            console.log("called")
            var vm = this
            vm.courses = []
            var xhr = new XMLHttpRequest();
            xhr.open('GET', '/usercourses')
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
            xhr.send("token=" + this.$cookies.get('Token'))
            xhr.onload = function() {
            console.log('User Courses: ' + xhr.responseText)
                var courses = xhr.responseText
                // split each even further down into individual course
                var eachCourse = courses.split(",")
                // push each course into course array
                for (var i = 0; i< eachCourse.length; i++) {
                vm.courses.push({ course: eachCourse[i] })
                }
            }
        }
    },
    beforeCreated() {
        document.body.style.backgroundImage = null;
        document.body.style.background = "#EAEAEA";

    },
    created() {
        this.getData()
        this.getDocumentsData()
    }
};
</script>

<style scoped>

#profile {
    color: #033457;
    width: 70vw;
    text-align: left;
    background-color: transparent;
    display: inline-block;
  }

#profile-picture {
    position: relative;
    float:left;
    height:100px;
    width: 100px;
    border-radius:50%;
    margin-right: 20px;
}

.parent {
    width: 100%;
    margin: 0 30vw;
}





</style>
