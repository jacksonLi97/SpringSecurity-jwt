<template>
    <h1>BLOG!</h1>
</template>

<script>
    export default {
        name: "Blog",
        data() {
            return {
                blog: {
                },
                list:[]
            }
        },
        created() {
            this.getBlog();
        },
        methods: {
            getBlog: function () {
                this.$axios.get('api/blog')
                    .then(res => {
                        this.blog = res.data
                    })
                    .catch(error => {
                        console.log(error)
                    })
            }
        }
    }
</script>

<style scoped>

</style>