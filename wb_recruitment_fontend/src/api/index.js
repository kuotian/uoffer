import request from '@/utils/request'

// 获取首页count
export function count() {
  return request({
    url: '/index',
    method: 'get'
  })
}

// 获取图表
export function chart() {
  return request({
    url: '/index/chart',
    method: 'get'
  })
}