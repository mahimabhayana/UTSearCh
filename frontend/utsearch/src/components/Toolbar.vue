<template>
  <div class="toolbar">
    <v-toolbar color="primary" style="background-color: #033457">
     <v-btn v-on:click.native = "homepage" depressed> <v-toolbar-title class="title">UTSearCh</v-toolbar-title></v-btn>
      <v-spacer></v-spacer>
      <v-toolbar-items v-if="name" class="hidden-sm-and-down">
        <v-btn v-on:click.native = "profile" flat="" color="white">{{name}}</v-btn>
      </v-toolbar-items>
       <v-toolbar-side-icon v-if="name" v-on:click.native = "profile" color="white">
        <img :src='pictureurl' style="width:45px"/>
      </v-toolbar-side-icon>
      <v-btn v-if="name" v-on:click.native = "signOut" color="red">Sign out</v-btn>
      <GoogleSignIn v-else id="sign-in" />
    </v-toolbar>
  </div>
</template>

<script>
  import { Bus } from '../main.js'
  import './client.js'
  import GoogleSignIn from './GoogleSignIn.vue'

  export default {
    name: 'Toolbar',
      data() {
        return{
          googleSignInParams: {
            client_id: '95665543481-fm3mpb7kie302hbshb8lg7ssjvctvq7r.apps.googleusercontent.com'
          },
          pictureurl: this.$cookies.get('Picture'),
          // %40 is @
          userEmail: this.$cookies.get('User'),
          // %20 is space
          name: this.$cookies.get('Name') || "",
          color: '#033457'
        }
      },
      methods: {
        XMLRequest (method, url, type) {
        var xhr = new XMLHttpRequest();
        xhr.open( method, url)
        xhr.setRequestHeader('Content-Type', type)
        xhr.onload = function() {
        console.log('Response Text: ' + xhr.responseText)
        }
        return xhr
      },
        signOut () {
          console.log("SIGNING OUT")
          // the view model
          var vm = this
          var auth2 = gapi.auth2.getAuthInstance()
          auth2.signOut().then(function () {
            alert('Successfully signed out')
            // send webservlet the logout request to delete session in server database
            var xhr = vm.XMLRequest('POST', '/logout', 'application/x-www-form-urlencoded')
            xhr.send('token=' + vm.$cookies.get("Token") + "&email=" + vm.userEmail)
            // delete cookies
            vm.$cookies.remove("User")
            vm.$cookies.remove("Name")
            vm.$cookies.remove("Picture")
            vm.$cookies.remove("Token")
            // redirect to landing page
            vm.$router.push("/")
          })
        },
        profile() {
          // will redirect to user profile
          this.$router.push("/userprofile")
        },
        homepage() {
          // will redirect to homepage
          this.$router.push("/home")
        }
      },
      beforeCreated() {
        this.$vuetify.theme.primary = '#033457';
      },
      mounted() {
        // on page load, load google identity api to generate log out button
        this.$vuetify.theme.primary = '#033457';
        var vm = this
        gapi.load('auth2', function() {
        gapi.auth2.init(vm.googleSignInParams);
      });
        },
      components: {
        GoogleSignIn
      },
  };

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .title{
    color: #fff;
    text-transform: none !important;
  }
  img {
    border-radius: 50%;
  }
</style>
