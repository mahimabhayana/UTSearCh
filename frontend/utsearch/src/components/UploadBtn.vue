<template>
    <div id="upload">
        <button @click="showDialog = true">Upload</button>
        <UploadDialog v-if="showDialog" @close="showDialog = false" />
    </div>
</template>

<script>
    import UploadDialog from './UploadDialog.vue'

    export default {
        name: 'UploadBtn',
        data() {
            return { showDialog: false }
        },
        components: {
            UploadDialog
        }
    };

</script>

<style scoped>
  #upload{
    font-size: 20px;
    font-weight: bold;
    background-color: #008000;
    width: 130px;
    height: 40px;
    line-height:40px;
    color: white;
  }



</style>