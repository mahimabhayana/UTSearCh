<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>UTSearCh Sign In</title>
    <!-- /opt/lampp/htdocs/SignIn -->
    <!-- Bootstrap:Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <!-- App client ID, note: this client id is set to localhost, not webserver -->
    <meta name="google-signin-client_id" content="95665543481-fm3mpb7kie302hbshb8lg7ssjvctvq7r.apps.googleusercontent.com">
    <!-- The google platform library -->
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <!-- Jquery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src = "GoogleSignIn.js"></script>


    <!-- class data has property display:none to hide profile info before signing in -->
    <style>
        .g-signin2{
        width:100%;
        }
        .g-signin2 > div{
          margin: 0 auto;
        }
      body {
        background:#1e65d8 !important;
      }
      .jumbopad
      {
        padding-top: 300px;
        padding-bottom: 300px;
        padding-left:600px;
        padding-right:600px;
      }
      .data{
          display:none;
      }
    </style>


</head>
<body>

    <div class="container-fluid jumbopad">
        <div class="jumbotron center-bloc">
          <div class="container text-center">
          <h1>UTSearCh</h1>
          <div class = "data">
            <p>Profile Details</p>
            <img id = "pic" class="img-circle" width="100" height="100"/>
            <p>Email Address</p>
            <p id="email" class="alert alert-danger"></p>
           <!-- On click run "signOut()" function -->
           <button onclick="signOut()" class="btn btn-danger">SignOut</button>
          </div>
              <!-- On success it will run "onSignIn" function from GoogleSignIn.js -->
              <div class = "g-signin2" data-onsuccess="onSignIn"></div>
          </div>
        </div>
    </div>

    </div>
</body>
</html>