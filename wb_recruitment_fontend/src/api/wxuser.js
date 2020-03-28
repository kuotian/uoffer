import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/penint";

// 查询微信用户列表
export function list(query) {
  return request({
    url: '/wxUser',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getById(id) {
  return request({
    url: '/wxUser/' + praseStrEmpty(id),
    method: 'get'
  })
}

// 通过id删除
export function delById(id) {
  return request({
    url: '/wxUser/' + id,
    method: 'delete'
  })
}

// 修改
export function update(data) {
  return request({
    url: '/wxUser',
    method: 'put',
    data: data
  })
}

// 禁止/不禁止用户发布评论
export function changeIsSpeak(id, isSpeak) {
  const data = {
    id,
    isSpeak
  }
  return request({
    url: '/wxUser/changeIsSpeak',
    method: 'put',
    data: data
  })
}