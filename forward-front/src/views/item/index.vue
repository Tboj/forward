<template>
  <div class="login">
    <el-form ref="form" :model="user" class="item-form">
      <div class="title">
        <span>
          10051端口转10061端口。默认报文为A,返回为B 
        </span>
      </div>
      <el-form-item>
        <el-input v-model="port.serverPort" placeholder="服务器端口，输：10051"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="port.destPort" placeholder="目标client端口，输：10061" @keyup.enter.native="submit('form')"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit('form')" :loading="loading" style="width: 100%;">
          提交
        </el-button>
      </el-form-item>
    </el-form>
    <div style="border: 1px solid rgb(211, 19, 19); overflow: auto; min-height: 30px;  " class="item-result">
      返回：{{result}}
    </div>

  </div>
</template>

<script>

export default {
  data() {
    return {
      port: {
        serverPort: "",
        destPort: "",
      },
      result: ""
    };
  },
  methods: {
    submit() {
        let url = "forward?post="+this.port.serverPort+"&destPort="+this.port.destPort;
        this.$http
            .get(url)
            .then(({data}) => {
                if (data.status) {
                    this.result = data.result;
                } else {
                    this.result = "失败"
                }
            })

    }
  }
};
</script>

<style scoped>
  .item {
    width: 100vw;
    height: 100%;
  }
  .item-result {
    width: 360px;
    position: absolute;
    top: 68%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .item-form {
    width: 360px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .title {
    font-size: 26px;
    font-weight: 550;
    margin-bottom: 20px;
  }
</style>
