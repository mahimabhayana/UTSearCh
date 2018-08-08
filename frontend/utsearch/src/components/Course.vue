<template>
<div style="text-align: center;">
  <Toolbar />
  <div id="profile">
    <v-app id="inspire">
      <v-layout row wrap>
        <v-flex xs12 sm12 md12>
          <div style="width: 80%;">
            <h1 style="font-size: 40px">{{ this.$route.params.code }}</h1>
            <div>Instructor: {{ instructor }}</div>
          </div>
          <v-layout row wrap>
            <v-flex md8 style="margin: 15px 5px 0 -5px">
                <v-card class="instructor-docs">
                    <v-card-title primary-title style="padding: 10px 15px;">
                    <h2 id="MyFav">Latest Professor Documents</h2>
                    </v-card-title>
                    
                    <div class="scroll">
                    <button id="sub" v-for="document in profDocuments">
                        <a :href="'/display?file=' + document.filename">{{ document.name }}</a>
                    </button>
                    </div>
                    <button id="more" @click="viewMore()">
                        View more
                    </button>
                </v-card>
              </v-flex>

            <v-flex md4 style="margin-top: 15px">
                <v-card class="student-docs">
                    <v-card-title primary-title style="padding: 10px 15px;">
                    <h2 id="MyFav">Student Documents</h2>
                    </v-card-title>

                    <div class="scroll">
                    <button id="student-doc" v-for="document in studentDocuments">
                        <a :href="'/display?file=' + document.filename">{{ document.name }}</a>
                    </button>
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
import axios from 'axios'
import Toolbar from './Toolbar.vue'

export default {
    name: 'Course',
    props: ['course'],
    data() {
        return {
            toggle: this.$cookies.isKey('Name'),
            pictureurl: this.$cookies.get('Picture'),
            // %40 is @
            userEmail: this.$cookies.get('User'),
            // %20 is space
            name: this.$cookies.get('Name'),
            instructor: '',
            profDocuments: []
        }
    },
    components: {
        Toolbar
    },
    methods: {
        viewMore() {
            const route = "/courseDocuments/" + this.$route.params.code;
            this.$router.push(route);
        },
        getCourse() {
            axios.get('/courseData', {
                params: {
                    course: this.$route.params.code
                }
            }).then(response => {
                this.instructor = response.data.Instructor
                this.profDocuments = response.data.InstructorDocs
                this.studentDocuments = response.data.StudentDocs
            });
        }
    },
    beforeCreated() {
        document.body.style.backgroundImage = null;
        document.body.style.background = "#EAEAEA";
    },
    created() {
        this.getCourse();
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

.instructor-docs {
    height: 400px;
    padding: 3px;
}

.student-docs {
    height: 400px;
    padding: 3px;
}

#student-doc {
    font-size: 17px;
    width: 96%;
    margin: 3px 6px;
    height: 36px;
    color:black;
    background-color: #dAdAdA;
}

#sub {
    font-size: 17px;
    width: 95%;
    margin: 3px 15px;
    height: 36px;
    color:black;
    /*background-color: #dAdAdA;*/
}

#more {
    font-size: 17px;
    width: 95%;
    margin: 3px 15px;
    height: 36px;
    color: white;
    background-color: green;
}

.scroll {
    height: 71%;
    margin-bottom: 10px;
}

</style>
