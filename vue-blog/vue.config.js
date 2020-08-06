module.exports = {
    devServer: {
        port: "8082",
        host: '127.0.0.1',
        open: false,//项目启动时是否自动打开浏览器，我这里设置为false,不打开，true表示打开
        proxy: {
            '/api': {//代理api
                target: "http://localhost:8080",// 代理接口
                changeOrigin: true,//是否跨域
                ws: true, // proxy websockets
                pathRewrite: {//重写路径
                    "^/api": '/'//代理路径
                }
            }
        }
    }
}