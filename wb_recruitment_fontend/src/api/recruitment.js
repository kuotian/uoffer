import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/penint";

// 查询招聘列表
export function list(query) {
  return request({
    url: '/recruitmentInfo',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getById(id) {
  return request({
    url: '/recruitmentInfo/' + praseStrEmpty(id),
    method: 'get'
  })
}

// 发布招聘信息
export function publish(data) {
  return request({
    url: '/recruitmentInfo',
    method: 'post',
    data: data
  })
}

// 通过id删除
export function delById(id) {
  return request({
    url: '/recruitmentInfo/' + id,
    method: 'delete'
  })
}

// 修改
export function update(data) {
  return request({
    url: '/recruitmentInfo',
    method: 'put',
    data: data
  })
}

// 通过招聘id查看评论
export function getRecruitmentInfoComment(id) {
  return request({
    url: '/comment/getRecruitmentInfoComment/' + id,
    method: 'get'
  })
}

// 通过招聘id查看点赞
export function getRecruitmentInfoClick(id) {
  return request({
    url: '/click/getRecruitmentInfoClick/' + id,
    method: 'get'
  })
}

// 通过招聘id查看关注
export function getRecruitmentInfoFocus(id) {
  return request({
    url: '/focus/getRecruitmentInfoFocus/' + id,
    method: 'get'
  })
}