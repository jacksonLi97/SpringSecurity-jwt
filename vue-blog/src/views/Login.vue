<template>
    <div class="login">
        <div>
            <input type="text" v-model="data.username">
        </div>
        <div>
            <input type="password" v-model="data.password">
        </div>
        <div>
            <button @click="login">登录</button>
        </div>
    </div>
</template>

<script>

    export default {
        name: 'Login',
        data() {
            return {
                data: {
                    username: '',
                    password: ''
                }
            }
        },
        methods: {
            login: function () {
                if(this.data.username == '' || this.data.password == ''){
                    this.$message({
                        message : '账号或密码为空！',
                        type : 'error'
                    })
                    return;
                }
                this.$axios.post('api/auth/login', {
                    username: this.data.username,
                    password: this.data.password
                }).then(res => {
                        console.log(res);
                        localStorage.setItem("token", res.data);
                        this.$router.push('/blog')
                }).catch((error) => {
                        console.log(error);
                });
                // this.$axios.get('api/index').then(res => {
                //         console.log(res)
                //     }).catch((error) => {
                //     console.log(error)
                // })

            }
        }



    }
</script>

<style scoped>
    button{
        margin-top: 50px;
        width: 300px;
        height: 50px;
    }
    input{
        margin-top: 30px;
        border: red 1px solid;
        width: 500px;
        height: 50px;
        text-align: center;
    }
</style>