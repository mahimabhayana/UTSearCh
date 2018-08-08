<template>
    <div id="holder">
    <div v-show='!toggle'>
  <g-signin-button
    :params="googleSignInParams"
    @success="onSignInSuccess"
    @error="onSignInError">
    Sign in with Google
  </g-signin-button>
    </div>
  </div>
</template>

<script>
import './client.js'
import { Bus } from '../main.js'

export default {
  name: 'GoogleSignIn',
  data () {
    return {
      /**
       * The Auth2 parameters, as seen on
       * https://developers.google.com/identity/sign-in/web/reference#gapiauth2initparams.
       * As the very least, a valid client_id must present.
       * @type {Object}
       */
      googleSignInParams: {
        client_id: '95665543481-fm3mpb7kie302hbshb8lg7ssjvctvq7r.apps.googleusercontent.com'
      },
      toggle: false,
      //'sub', 'email', 'firstName', 'lastName'
      id_token: '',
      }
  },
  methods: {
    onSignInSuccess (googleUser) {
      // the view model
      var vm = this
      // `googleUser` is the GoogleUser object that represents the just-signed-in user.
      // See https://developers.google.com/identity/sign-in/web/reference#users
      // getting google profile image
      const profile = googleUser.getBasicProfile()

      // storing name, picture url, and email
      vm.name = profile.getName()
      vm.sub = profile.get
      console.log("Hi " + vm.name)
      vm.pictureurl = profile.getImageUrl()
      vm.userEmail = profile.getEmail()
      console.log("email: " + vm.userEmail)

      // get unique authorization token
      var id_token = googleUser.getAuthResponse().id_token
      vm.id_token = id_token
      console.log("id_token: " +id_token)

      var xhr = new XMLHttpRequest();
      xhr.open('POST', 'http://localhost:8081/login')
      xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
      xhr.send('id_token=' + id_token)
      xhr.onload = function() {
        var rt = xhr.responseText
        // server will send '-1' as response if account not found in db
        if (rt == "-1") {
          // save id_token in cookie 
          if(vm.$cookies.isKey("ID_TOKEN")) {
            vm.$cookies.remove("ID_TOKEN")
          }
          vm.$cookies.set("ID_TOKEN", vm.id_token, "1d")
           // redirect to registration page
          vm.$router.push("/register")
        } else {
            vm.$cookies.set("User", vm.userEmail, "1d")
            vm.$cookies.set("Name", vm.name, "1d")
            vm.$cookies.set("Picture", vm.pictureurl, "1d")
            console.log("RESPONSE TOKEN: " + xhr.responseText)
            vm.$cookies.set("Token", xhr.responseText, "1d")
            // hide login button
            vm.toggle = true
            // redirect to homepage
            vm.$router.push("/home")
        }
      }

    },
    onSignInError (error) {
      // `error` contains any error occurred.
      console.log('Error: ', error)
    }
  },
  created() {
    if(this.$cookies.get('Name')) {
      this.$router.push("/home")
    }
  }
};
</script>

<style>
.g-signin-button {
  /* This is where you control how the button looks. Be creative! */
  display: inline-block;
  padding: 4px 8px;
  border-radius: 3px;
  background-color: #3c82f7;
  color: #fff;
  box-shadow: 0 3px 0 #0f69ff;
  cursor: pointer;
}
</style>
