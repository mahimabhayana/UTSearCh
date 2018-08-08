<template>
  <div>
    <GoogleSignIn id="sign-in" />
    <div class="main">
      <h1 id="title">UTSearCh</h1>
      <Search id="search-bar" />
      <QueryOptions id="queryOptions"/>
      <MatchOptions id="matchOptions"/>
    </div>
  </div>
</template>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js" type="text/javascript" ></script>
<script >
import Search from './SearchBar.vue'
import MatchOptions from './MatchOptions.vue'
import QueryOptions from './QueryOptions.vue'
import GoogleSignIn from './GoogleSignIn.vue'


export default {
  name: 'Landing',
  components: {
    GoogleSignIn,
    Search,
    QueryOptions,
    MatchOptions
  },
  mounted() {
    var images = [
    'https://media.blogto.com/articles/2017210-university-toronto.jpg?cmd=resize_then_crop&quality=70&w=2048&height=1365',
    'https://cdnarchitect.s3.amazonaws.com/wp-content/uploads/2016/02/U-of-T-Scarborough-Environmental-Science-and-Chemistry-Building-Photo-Michael-Muraz.jpg',
    'https://media.blogto.com/articles/2017314-city.jpg?cmd=resize_then_crop&quality=70&w=2048&height=1365',
    'https://upload.wikimedia.org/wikipedia/commons/a/aa/Toronto_university_0001.JPG',
    'https://lazyphotog.files.wordpress.com/2010/03/sundaydwntwn-217-edit2.jpg',
    'http://2.bp.blogspot.com/-g6wy2xKG0Eo/Umfh1JcC0fI/AAAAAAAAAIU/wSccZembCLM/s1600/Fall+campus+photos+024.JPG',
    'https://utsc.calendar.utoronto.ca/sites/public/utsc/files/pictures/Front%20Cover%202018_crop.jpg',
    'http://heritage.utoronto.ca/fedora/repository/default:14511/OBJ/FULL_SIZE.jpg'];

    document.body.style.backgroundImage = 'url('+ images[Math.floor(Math.random() * images.length)] + ')';
    document.body.style.backgroundSize = "cover";
  },

  beforeDestroyed() {
    document.body.style.backgroundImage = null;
  }
};

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  @import url('https://fonts.googleapis.com/css?family=Varela+Round');
  body {
    width: 100%;
    height: 100%;
    /*background-image:url(https://media.blogto.com/articles/2017314-city.jpg?cmd=resize_then_crop&quality=70&w=2048&height=1365);*/
    background-size: cover;
    background-attachment:fixed;
    background-size:cover;
    background-repeat:no-repeat;
    margin: 0;
    padding: 0;
  }

  #main {
    font-family: 'Varela Round', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: white;
    margin-top: 60px;
  }

  .main {
    background-color: rgba(0, 0, 0, 0.5);
    margin: 20vh 20vw;
    padding: 30px;
  }

  #search-bar {
    margin: 20px;
  }

  #sign-in {
    margin: 20px;
    position: absolute;
    top: 0;
    right: 0;
    margin-top: 20px;
    float: right;
  }

  #class{
    margin-top: 100px;
  }

  h3 {
    margin: 40px 0 0;
  }

  input {
    background-color: #fff;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 20px;
  }

  a {
    color: #FFFFFF;
  }

</style>
