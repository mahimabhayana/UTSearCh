<template>
  <div id="boxSearches">
    <button id="showSearches" @click="displaySearches()">View my Searches</button>
    <div id = "container">
    <button id="sub" v-show='toggle' v-for="(data, index) in searches.slice(0, numSearches)" :key='index' @click=redirect(data.link)> {{data.name}} </button>
    </div>
  </div>
</template>

<script>
  export default {
  name: 'RecentListHomepage',
  data() {
  return{
  toggle:false,
  name: '',
  searches: [],
  numSearches: 5
  }
  },
  components: {
  },
  methods: {

    getRecentSearchesData() {
        var vm = this
        // get recent searches from usersearches servlet
        if (this.$cookies.isKey('User')) {
          var xhr = new XMLHttpRequest();
          xhr.open('POST', '/usersearch')
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
          xhr.send('token=' + this.$cookies.get('Token') + '&action=get')
          xhr.onload = function() {
            var rt = xhr.responseText
            // parse response text of the following format
            // query1;query2;query3
            var splitter = rt.split(";")
            // loop through all documents and add
            for (var i = 0; i< splitter.length; i++) {
              // remove unecessary url part
              var nem = splitter[i].replace('/search?query=', '')
              nem = nem.substring(0, nem.indexOf('&match='))
              // push only the user written part into list
                vm.searches.push({name: nem, link:splitter[i]})
            }
          }
        }
    },
    redirect(link) {
      if (link != null) {
        // when user clicks on a button, will redirect to their most recent searches
        window.location.href = link;
      }
    },
    displaySearches() {
      var btnViewSearches = document.getElementById("showSearches");
      btnViewSearches.innerHTML = "";
      btnViewSearches.setDisabled = true;
      btnViewSearches.style.visibility = "hidden";
      btnViewSearches.style.display = "none";
      var vm = this
      // will display most recent searches
      if (vm.searches.length > 0) {
        if (vm.searches[0].name.length == 0) {
          vm.searches[0].name = "No Recent Searches"
          vm.searches[0].link = null
        }
        vm.toggle = !vm.toggle
      }
    }
  
  },
      created() {
          this.getRecentSearchesData()
      }
  };

</script>

<style scoped>
  #showSearches{
    font-size: 20px;
    background-color: #FAFAFA;
    width: 200px;
    height: 40px;
    color: black
  }

  #sub {
    font-size: 20px;
    width: 200px;
    height: 70px;
    margin: 10px;
    color:black;
    background-color: #FAFAFA;
  }

  #container {
    position: relative;
    width:0px;
  }




</style>