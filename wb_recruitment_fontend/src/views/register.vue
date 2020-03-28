<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">招聘小程序后台管理-注册</h3>
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>

      <el-form-item prop="mobile">
        <el-input v-model="loginForm.mobile" type="text" auto-complete="off" placeholder="手机号">
          <svg-icon slot="prefix" icon-class="phone" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>

      <el-form-item prop="email">
        <el-input v-model="loginForm.email" type="text" auto-complete="off" placeholder="邮箱">
          <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>

      <el-link type="primary" style="margin:0px 0px 25px 280px;">
        <a href="./login">返回登录</a>
      </el-link>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handlerRegister"
        >
          <span v-if="!loading">注 册</span>
          <span v-else>注 册 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>
        Copyright © 2018-2020
        <a href="https://hxxzt.com" target="_blank">南风知我意</a> All Rights Reserved.
      </span>
    </div>
  </div>
</template>

<script>
import { register } from "@/api/login";
import { encrypt, decrypt } from "@/utils/jsencrypt";
import { checkUsername } from "@/api/common";

export default {
  name: "Register",
  data() {
    var valideteUsername = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("用户名称不能为空"));
      }
      checkUsername(value).then(response => {
        if (response.data) {
          callback(new Error("用户名已经存在，请更换用户名重试"));
        } else {
          callback();
        }
      });
    };
    return {
      loginForm: {
        username: "",
        password: "",
        email: "",
        mobile: ""
      },
      loginRules: {
        username: [
          { required: true, validator: valideteUsername, trigger: "blur" }
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" }
        ],
        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        mobile: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      },
      loading: false,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {},
  methods: {
    handlerRegister() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          register(this.loginForm).then(response => {
            if(response.status === 200){
              this.msgSuccess("注册成功，即将跳转到登录界面");
              this.loading=false;
              setTimeout(() => {
                 window.open('./login')
                }, 3000);
             
            }else{
              this.msgError(response.msg);
            }
          });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/image/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>