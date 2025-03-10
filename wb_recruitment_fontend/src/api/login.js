import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/auth/doLogin',
    method: 'post',
    params: data
  })
}

// 注册方法
export function register(query) {
  return request({
    url: '/auth/register',
    method: 'post',
    params: query
  })
}


// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/auth/userInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get'
  })
}