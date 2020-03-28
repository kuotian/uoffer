import request from '@/utils/request'

// 查询在线用户列表
export function list(query) {
  return request({
    url: '/auth/online',
    method: 'get',
    params: query
  })
}

// 强退用户
export function forcedAccountOut(username) {
  return request({
    url: '/auth/forcedAccountOut/' + username,
    method: 'delete'
  })
}


// 判断具体哪条为当前登录用户
export function getIsMe() {
  return request({
    url: '/auth/getIsMe',
    method: 'get'
  })
}