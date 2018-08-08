<template>
    <div id="upload">
        <button @click="showDialog = true">Crawler</button>
        <CrawlerDialog v-if="showDialog" @close="showDialog = false" />
    </div>
</template>

<script>
    import CrawlerDialog from './CrawlerDialog.vue'

    export default {
        name: 'CrawlerButton',
        data() {
            return { showDialog: false }
        },
        components: {
          CrawlerDialog
        }
    };

</script>

<style scoped>
  #upload{
    font-size: 20px;
    font-weight: bold;
    background-color: #033457;
    width: 130px;
    height: 40px;
    line-height:40px;
    color: white
  }
</style>