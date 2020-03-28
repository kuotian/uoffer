import request from '@/utils/request'

// 表单校验 新增用户时验证库里是否有重复用户名
export function checkUsername(username) {
  return request({
    url: '/common/checkUsername/' + username,
    method: 'get'
  })
}

// 表单校验 修改密码时验证旧密码是否正确
export function checkPassword(password) {
  return request({
    url: '/common/checkPassword/' + password,
    method: 'get'
  })
}

export function getSchoolList(){
  return request({
    url: '/common/getSchoolList',
    method: 'get'
  })
}