const path = require('path');

function resolve(dir) {
  return path.join(__dirname, dir);
}

module.exports = {
  lintOnSave: true,
  productionSourceMap: false,
  // 文件名添加hash
  // filenameHashing: false,
  baseUrl: './', // 生成的目录
  outputDir: resolve('static'),
  // 代理
  devServer: {
    port: 8000,
    https: false,
    open: true,
    proxy: {
      '/rest': {
        // target: 'http://192.168.2.4:3000/api',
        // target: 'http://192.168.2.4:7000/gsh/p1/api',
        target: 'http://127.0.0.1:8080/',
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '^/rest': '/'
        }
      },
    },
  },
  /* chainWebpack: config => {

    if (process.env.NODE_ENV === 'production') {
      // 为生产环境修改配置...
    } else {
      // 为开发环境修改配置...
    }
  }, */
}
