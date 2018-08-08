<template>
    <div>
        <button @click="showDialog = true">Upload document</button>
        <UploadDialog v-if="showDialog" @close="showDialog = false" />
    </div>
</template>

<script>
    import UploadDialog from './UploadDialog.vue'

    export default {
        name: 'UploadPage',
        data() {
            return { showDialog: false }
        },
        components: {
            UploadDialog
        }
    };

</script>