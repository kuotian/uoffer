import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/penint";

// 查询列表
export function list(query) {
  return request({
    url: '/resumeSend',
    method: 'get',
    params: query
  })
}

// 通过id查看
export function getById(id) {
  return request({
    url: '/resumeSend/' + id,
    method: 'get'
  })
}

// 发送邀请
export function invite(id,status,interviewTime) {
  const data = {
    id,
    status,
    interviewTime
  }
  return request({
    url: '/resumeSend/invite',
    method: 'put',
    data: data
  })
}

// 简历不合适按钮
export function inadequacy(id,status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/resumeSend/inadequacy',
    method: 'put',
    data: data
  })
}