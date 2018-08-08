// function takes in googleUser as an argument
var userEmail;
function onSignIn(googleUser) {
    // fetch profile info of the logged in user
    var profile = googleUser.getBasicProfile();
    // Using JQuery, target g-signin2 class, and change display property to none
    // after signing in, don't want to be able to click sign in button again
    $(".g-signin2").css("display", "none");
    // target data class and set display property to block
    // user can see personal data
    $(".data").css("display", "block");
    // target pic id and fetch image from profile
    $("#pic").attr('src', profile.getImageUrl());
    // target email id and get email
    $("#email").text(profile.getEmail());
    userEmail = profile.getEmail();


    // token authentication for backend
    var id_token = googleUser.getAuthResponse().id_token;
    // send ID token to server with an HTTPS POST request
    var xhr = new XMLHttpRequest();
    // send to webservlet
    xhr.open('POST', 'http://localhost:8080/login');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
      console.log('Signed in as: ' + xhr.responseText);
    };
    xhr.send('id_token=' + id_token);

    // send variable id_token to redirect url, which is localhost:8080/login
    // the LoginServlet will run and use id_token to authenticate user
    /**
    var redirectUrl = '/login';

             //using jquery to post data dynamically
             var form = $('<form action="' + redirectUrl + '" method="post">' +
                              '<input type="text" name="id_token" value="' +
                               googleUser.getAuthResponse().id_token + '" />' +
                                                                    '</form>');
             $('body').append(form);
             form.submit();
    */
}

function signOut() {
    // sign out using google api
    // TODO: invalidate session on sign out
    // https://stackoverflow.com/questions/27400747/session-removeattribute-doesnt-work-in-java
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function() {
        alert("Successfully signed out");
        // make sign in button visible again
        $(".g-signin2").css("display", "block");
        // make data class invisible again
        $(".data").css("display", "none");
        // communicate backend to end session, send email to endsession
        //session.setAttribute("userName", email);
        var xhr = new XMLHttpRequest();
        // send to webservlet
        xhr.open('POST', 'http://localhost:8080/logout');
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.send('email=' + userEmail);

    })
}