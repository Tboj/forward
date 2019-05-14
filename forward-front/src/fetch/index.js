import axios from 'axios'
import filterStatusCode from './filterStatusCode'
import Vue from 'vue'

const Axios = axios.create({
  baseURL:  process.env.NODE_ENV === 'development' ? 'rest/' : '',
  timeout: 40000,
  responseType: 'json',
})

// 请求拦截 - 头部添加token
Axios.interceptors.request.use(config => {
  return config
}, error => {
  return Promise.reject(error)
})

Axios.interceptors.response.use(response => {
  return Promise.resolve(response);
}, error => {
  filterStatusCode(error);
  return Promise.reject(error);
})

Vue.prototype.$http = Axios;

export default Axios
