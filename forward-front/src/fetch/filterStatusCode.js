/* eslint-disable */
import {
  Message
} from 'element-ui'

import router from '@/router'

const duration = 5000;

// 提示信息策略
const MESSAGE_STRATEGY = {
  '404': {
    msg: '请求错误，未找到该资源',
    type: 'error'
  },

  '500': {
    msg: '服务器出错',
    type: 'error',
  },

  '401': function () {
    let msg = '请您先登录！';
    let type = 'warning';
    return {
      msg,
      type
    };
  },

  '403': function () {
    let msg = '产品未激活，拒绝访问！';
    let type = 'warning';
    return {
      msg,
      type
    };
  },
};

const filterStatusCode = error => {
  if (error && error.response) {
    let statusCode = error.response.status;

    let messageInfo = MESSAGE_STRATEGY[statusCode];
    if (typeof messageInfo === 'function') {
      messageInfo = messageInfo();
    }
    if (messageInfo) {
      Message({
        message: messageInfo['msg'],
        type: messageInfo['type'],
        duration: duration
      });
    }
    switch (statusCode) {
      case 401:
      case 403:
        router.replace('/');
        break;
      default:
        break;
    }

  } else {
    Message({
      message: '请求失败',
      type: 'error',
      duration: duration
    });
  }
}

export default filterStatusCode
