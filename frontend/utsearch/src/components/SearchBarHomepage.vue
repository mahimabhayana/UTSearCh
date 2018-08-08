<template>
  <div class="search">
    <input type ="text"
           ref="query"
           class="s"
           v-model="userQuery"
           @click="displayRecentSearches()"
           v-text="selectedItem ? selectedItem['name'] : ''"
           @keyup.enter="search"
           placeholder="Type your query and hit enter"></input>
  <div class="results-container" v-click-outside="hide" @click="toggle">
    <button id="sub" v-show='showSearches' v-for="(data, index) in searches.slice(0, numSearches)" :key='index' @click=redirect(data.link)> {{data.name}} </button>
    </div>
    
  </div>
</template>

<script>

  import MatchOptions from './MatchOptions.vue'
  import { Bus } from '../main.js'
  import ClickOutside from 'vue-click-outside'

  export default{
    name: 'SearchBarHomepage',
    data() {
      return{
        userQuery: '',
        filterby: 'name',
        selected: 0,
        selectedItem: null,
        toggle:false,
        showSearches:false,
        name: '',
        searches: [],
        numSearches: 5,
        autocomplete: [],
        matchQuery: '',
        clicked: false,
        checkedBox: [],
        opened: false
      }
    },
    components: {
      MatchOptions
    },
    mounted () {
      // prevent click outside event with popupItem.
      this.popupItem = this.$el
    },
    methods :{
      onClickOutside (event) {
          console.log('Clicked outside. Event: ', event)
        },
        toggle () {
        this.opened = true
      },

      hide () {
        this.showSearches = false
      },
  
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
              console.log('rt', rt)
              // parse response text of the following format
              // query1;query2;query3
              var splitter = rt.split(";")
              // loop through all documents and add
              // create set
              var mySet = new Set()
              for (var i = 0; i< splitter.length; i++) {
                // remove unecessary url part
                var nem = splitter[i].replace('/#/results?query=', '')
                nem = nem.substring(0, nem.indexOf('&match='))
                // push only the user written part into list
                  vm.searches.push({name: nem, link:splitter[i]})
                  // if not in set, add it to autocomplete
                  if (!mySet.has(nem)) {
                    // add to set
                    mySet.add(nem)
                    // add to unique array
                    vm.autocomplete.push({name: nem, link:splitter[i]})
                  }
              }
            }
          }
      },
      search() {
          var href = "/results?query=" + this.$refs.query.value + "&match=" + this.matchQuery
          + "&checkedBox="
          for (var i = 0; i < this.checkedBox.length; i++) {
            href += this.checkedBox[i]
          }
          console.log('url', href)
          // send query data to back-end to store in database for most recent searches bar
          if (this.$cookies.isKey('Token')) {
            var xhr = new XMLHttpRequest();
            xhr.open('POST', '/usersearch')
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
            xhr.send('token=' + this.$cookies.get('Token') + '&queryUrl=/#' + href + '&action=set')
            xhr.onload = function() {
              console.log('Response Text: ' + xhr.responseText)
            }
          }
          this.$router.push(href)        
      },
      searchClicked(index) {
        console.log("clicked item" + index);
        this.selected = index;
        this.selectItem();
      },
      selectItem() {
        this.selectedItem = this.matches[this.selected];
        console.log(this.selectedItem);
        this.redirect(this.selectedItem['link']);
      },
      redirect(link) {
        if (link != null) {
         window.location.href = link;
        }
      },
      displayRecentSearches() {
        this.clicked = true;
        this.showSearches = true;
        var vm = this
        if (vm.searches.length > 0) {
          console.log('searcjes', vm.searches)
          if (vm.searches[0].name.length == 0) {
            vm.searches[0].name = "No Recent Searches"
            vm.searches[0].link = null
          }
        }
      }
    },
    created() {
     // console.log("created called");
      this.showSearches = false;
      this.getRecentSearchesData();
      // console.log(this.searches[0]);
      Bus.$on('matchChanged', (matchQuery) => {
        this.matchQuery = matchQuery
      }),
      Bus.$on('checkedChanged', (box) => {
        this.checkedBox = box
    })
      
  },

  directives: {
    ClickOutside
  },


  computed: {
    matches() {
   
      if(this.userQuery == '') {
        if(this.clicked) {
          this.showSearches = true;
          }
        return [];
      }

      else {
       this.showSearches = false;
       return this.autocomplete.filter((autocomplete) => autocomplete['name'].toLowerCase().includes(this.userQuery.toLowerCase()));
      }

      }
    }

  };

</script>


<style>
  input[type="text"] {
  background-color : #ffffff;
  color: #000000;
  padding: 5px;
  }

  .s {
  width: 600px;
  height: 40px;
  position: absolute;
  left: 200px;
  top: 170px;
  z-index: 2;

  }

  .options {
  position: absolute;
  left: 156px;
  top: 210px;
  z-index: 1;
  max-width: 600px;


  }

  .options ul {
  width: 600px;

  }

  .options ul li {

  width: 600px;
  height: 40px;
  cursor: pointer;
  background-color : #ffffff;
  color: 	#9932CC;
  text-align: left;
  
  }

  .options ul li:hover {
    background-color: 	#C0C0C0;
  }

  #sub {
    width: 600px;
    height: 40px;
    cursor: pointer;
    background-color : rgba(240, 240, 240, 1);
    color: 	#9932CC;
    text-align: left;
    padding: 5px 10px;
  }
  
  #sub:hover {
    background-color: 	#C0C0C0;
  }

  .results-container {
    position: absolute;
    left: 200px;
    top: 210px;
    z-index: 1;
    max-width: 600px;
    max-height: 200px;
    padding: 0;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 3px;
  }

</style>