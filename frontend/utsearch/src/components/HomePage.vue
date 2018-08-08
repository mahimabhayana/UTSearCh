<template>
  <div id ="home">
    <Toolbar />
    <h2 id="search"> {{msg}} </h2>
    <div id="searchBar"> <SearchBarHomepage /> </div>
    <div id="mOptions"><MatchOptions /> </div>
    <div id="qOptions"> <QueryOptions /> </div>
    <div id="buttons1">
      <div id="uploadBtn"> <UploadBtn /> </div>
      <div id="crawlerBtn"> <CrawlerButton /> </div>
    </div>
    <div id="buttons2">
      <div id="addCoursesBtn">
        <AddCourseBtn />
      </div>
      <div id="enrolCourseBtn">
        <EnrolCourseBtn />
      </div>
    </div>
    <h2 id ="courses"> {{msg2}} </h2>
    <h2 id ="documents"><a style="color: #033457" href="/#/documents">{{msg3}}</a></h2>
    <div id="courseList"> <CourseListHomepage /> </div>
    <div id="documentList"> <DocumentListHomepage /> </div>
    
   
  </div>
</template>

<script>
  import Toolbar from './Toolbar.vue'
  import SearchBarHomepage from './SearchBarHomepage.vue'
  import QueryOptions from './QueryOptions.vue'
  import UploadBtn from './UploadBtn.vue'
  import CrawlerButton from './CrawlerButton.vue'
  import MatchOptions from './MatchOptions.vue'
  import CourseListHomepage from './CourseListHomePage.vue'
  import DocumentListHomepage from './DocumentListHomepage.vue'
  import AddCourseBtn from './AddCourseBtn.vue'
  import EnrolCourseBtn from './EnrolCourseBtn.vue'


  export default {
  name: 'HomePage',
  data () {
    return {
      msg: "Search",
      msg2: "My Courses",
      msg3: "My Documents",
      msg4: "Recent Searches"
      
    }
  },
  components: {
    Toolbar,
    SearchBarHomepage,
    QueryOptions,
    MatchOptions,
    UploadBtn,
    CrawlerButton,
    CourseListHomepage,
    DocumentListHomepage,
    AddCourseBtn,
    EnrolCourseBtn   
  },
  methods: {
    signOut () {
      console.log("SIGNING OUT")
      // the view model
      var vm = this
      // call google's api to sign out
      var auth2 = gapi.auth2.getAuthInstance()
      auth2.signOut().then(function () {
        alert('Successfully signed out')
        // send webservlet to delete session in database
        var xhr = vm.XMLRequest('POST', '/logout', 'application/x-www-form-urlencoded')
        xhr.send('token=' + vm.token)
        // delete cookies client side
        vm.$cookies.remove("User")
        vm.$cookies.remove("Name")
        vm.$cookies.remove("Picture")
        vm.$cookies.remove("Token")
        // redirect to landing page
        vm.$router.push("/")
      })
    }
  },
  mounted() {
  },
  created() {
    // if no Token, send back to landing page
    if(this.$cookies.get('Token') == null) {
      this.$router.push("/")
    } else {
      // remove background images
      document.body.style.background = "#EAEAEA";
      
      // has a token, validate token by calling java servlet
      var xhr = new XMLHttpRequest();
      xhr.open('POST', '/validateToken')
      xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
      xhr.send('token=' + this.$cookies.get('Token'))
      xhr.onload = function() {
        var rt = xhr.responseText
        console.log('Is Valid? : ' + rt)
        if(rt != "valid") {
          // Server says not valid, so sign out and redirect to homepage
          this.signOut()
          this.$router.push("/")
        }
      }
    }
  }
  };

</script>

<style scoped>
  body {
    background-color: #EAEAEA;
  }

  #home {
  position: absolute;
  top: 0px;
  width:100%;
  color: #033457;
  }

  #search {
  font-size: 50px;
  color: #033457;
  position: absolute;
  left: 200px;
  top: 100px;
  }

  #courses {
  font-size: 30px;
  position: absolute;
  left: 200px;
  top: 270px;
  }

  #documents {
  font-size: 30px;
  position: absolute;
  left: 600px;
  top: 270px;
  }

  #qOptions {
  position: absolute;
  left: 300px;
  top: 220px;
  }

  #mOptions {
  position: absolute;
  left: 200px;
  top: 240px;
  }

  #uploadBtn{
    margin: 5px 0;
    margin-left: 25px;
    display: inline-block;

  }
  #crawlerBtn{
    margin: 5px 0;
    margin-left: 25px;
    display: inline-block;
  }
  #addCoursesBtn{
    margin: 5px 0;
    margin-left: 25px;
    display: inline-block;
  }
  
  #enrolCourseBtn {
     margin: 5px 0;
    margin-left: 25px;
    display: inline-block;
    
  }

  #courseList{
  position: absolute;
  left: 200px;
  top: 320px;
  }

  #documentList{
  position: absolute;
  left: 600px;
  top: 320px;
  }

  #qOptions input[type="checkbox"] {
    height: 30px;
  }

  #buttons1 {
    position: absolute;
    border-left: solid 2px;
    border-color: rgba(0, 0, 0, 0.4);
    height: 130px;
    width: 150px;
    left: 830px;
    top: 90px;
    text-align: center;

  }

  #buttons2 {
    position: absolute;
   // border-left: solid 2px;
    border-color: rgba(0, 0, 0, 0.4);
    height: 130px;
    width: 150px;
    left: 980px;
    top: 90px;
    text-align: center;

  }
 

</style>
