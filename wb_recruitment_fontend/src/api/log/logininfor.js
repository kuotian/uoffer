import request from '@/utils/request'

// 查询登录日志列表
export function list(query) {
  return request({
    url: '/loginLog/list',
    method: 'get',
    params: query
  })
}

// 删除登录日志
export function delLogininfor(infoId) {
  return request({
    url: '/loginLog/' + infoId,
    method: 'delete'
  })
}

// 清空登录日志
export function cleanLogininfor() {
  return request({
    url: '/loginLog/clean',
    method: 'delete'
  })
}

// 导出登录日志
export function exportLogininfor() {
  return request({
    url: '/loginLog/export',
    method: 'get',
    responseType: 'blob'
  })
}
