<template>
<div class = 'black-container'>
    <form id="submit-form">
        <h1> Registration </h1>
        <br>
        <br>
        <!-- <p> "test"
            {{sub}}
            {{gmail}}
            {{name}}
            {{id_token}}
            </p> -->
        <!-- text -->
        <p>
            {{msg1}}
            <input type="text" v-model="email">
            <br>
            <br>
            {{msg4}}
            <input type="text" v-model="email_confirm">
        </p>
        <!-- radio buttons -->
        <p>
            {{msg2}}
            <input type="radio" name="Instructor" value="Instructor" v-model="picked_type">
            {{msg3}}
            <input type="radio" name="Student" value="Student" v-model="picked_type">
            <br>
        </p>
        <br>
        <br>
        <p> You are a {{picked_type}} with the following email {{email}}</p>
        <!-- select
        <p>
            <select v-model="selected">
            <option>one</option>
            <option>two</option>
            </select>
            {{selected}}
        </p> -->
    </form>
    <v-btn color="blue" v-on:click.native = "submit" >Submit</v-btn>

</div>
</template>

<script>
import { Bus } from '../main.js'
export default {
    name: 'Register',
    data () {
        return {
            msg1: "Enter UofT Email",
            email: '',
            email_confirm: '',
            msg2: "Instructor",
            msg3: "Student",
            picked_type: '',
            msg4: "Confirm Email",
            gmail: '',
            id_token: ''
        }
    },
    methods: {
        submit () {
            // confirm non-empty email field
            if (this.email == '' || this.email_confirm == '') {
                alert('Please fill in both email fields')
            } else if (!this.email.includes('utoronto.ca' || !this.email_confirm.includes('utoronto.ca'))) {
                alert('Please use a utoronto email')
            } else if (this.email != this.email_confirm) {
                alert('Email does not match')
            } else if (this.picked_type == '') {
                alert('Please choose either an Instructor or Student account')
            } else {
                // valid, submit data to servlet
                var xhr = new XMLHttpRequest();
                xhr.open('POST', 'http://localhost:8081/register')
                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
                xhr.send('email=' + this.email + '&type=' + this.picked_type + '&id_token=' + this.$cookies.get("ID_TOKEN"))
                xhr.onload = function() {
                    var rt = xhr.responseText
                    if (rt == '-1') {
                        // account with gmail exists
                        alert('gmail is already in use')
                    } else if(rt == '-2') {
                        // account with uoft mail exists
                        alert('UofT email is already in use')
                    } else {
                        // valid
                        alert('Successfully created account, please check your UTOR email for confirmation mail')
                        // redirect to main page
                        this.$router.push("/")
                    }
                }
            }
        }
    }
}
</script>

<style scoped>
  .black-container {
    background-color: rgba(0, 0, 0, 0.5);
    margin: 20vh 20vw;
    padding: 30px;
  }
</style>
