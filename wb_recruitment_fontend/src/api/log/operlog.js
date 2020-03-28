import request from '@/utils/request'

// 查询操作日志列表
export function list(query) {
  return request({
    url: '/sysOperationLog/list',
    method: 'get',
    params: query
  })
}

// 删除操作日志
export function delOperlog(operId) {
  return request({
    url: '/sysOperationLog/' + operId,
    method: 'delete'
  })
}

// 清空操作日志
export function cleanOperlog() {
  return request({
    url: '/sysOperationLog/clean',
    method: 'delete'
  })
}

// 导出操作日志
export function exportOperlog(query) {
  return request({
    url: '/sysOperationLog/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

// 获取模块下拉框
export function getModule() {
  return request({
    url: '/sysOperationLog/getModule',
    method: 'get'
  })
}
