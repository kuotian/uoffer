import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/penint";

// 查询列表
export function list(query) {
  return request({
    url: '/resume',
    method: 'get',
    params: query
  })
}

// 通过id查看
export function getById(id) {
  return request({
    url: '/resume/' + id,
    method: 'get'
  })
}

// 通过id删除
export function delById(id) {
  return request({
    url: '/resume/' + id,
    method: 'delete'
  })
}

// 修改
export function update(data) {
  return request({
    url: '/resume',
    method: 'put',
    data: data
  })
}