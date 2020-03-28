import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/penint";

// 查询未审核企业列表
export function listNotReview(query) {
  return request({
    url: '/enterprise/notReview',
    method: 'get',
    params: query
  })
}

// 管理员审核
export function reviewById(id) {
  return request({
    url: '/enterprise/review/' + id,
    method: 'put'
  })
}

// 查询已审核企业列表
export function listReview(query) {
  return request({
    url: '/enterprise/review',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getById(id) {
  return request({
    url: '/enterprise/' + praseStrEmpty(id),
    method: 'get'
  })
}

// 删除
export function delById(id) {
  return request({
    url: '/enterprise/' + praseStrEmpty(id),
    method: 'delete'
  })
}

// 新增
export function addEnterprise(data) {
  return request({
    url: '/enterprise',
    method: 'post',
    data: data
  })
}

// 禁止/不禁止企业发布消息
export function changePublish(id, isPublish) {
  const data = {
    id,
    isPublish
  }
  return request({
    url: '/enterprise/changePublish',
    method: 'put',
    data: data
  })
}

// 附件上传
export function uploadAccessory(data) {
  return request({
    url: '/enterprise/uploadAccessory',
    method: 'post',
    data: data
  })
}
