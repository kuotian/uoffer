import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/penint";

// 查询宣讲会列表
export function list(query) {
  return request({
    url: '/careerTalk',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getById(id) {
  return request({
    url: '/careerTalk/' + praseStrEmpty(id),
    method: 'get'
  })
}

// 发布宣讲会信息
export function publish(data) {
  return request({
    url: '/careerTalk',
    method: 'post',
    data: data
  })
}

// 通过id删除
export function delById(id) {
  return request({
    url: '/careerTalk/' + id,
    method: 'delete'
  })
}

// 修改
export function update(data) {
  return request({
    url: '/careerTalk',
    method: 'put',
    data: data
  })
}

// 通过宣讲会id查看评论
export function getCareerTalkComment(id) {
  return request({
    url: '/comment/getCareerTalkComment/' + id,
    method: 'get'
  })
}

// 通过宣讲会id查看点赞
export function getCareerTalkClick(id) {
  return request({
    url: '/click/getCareerTalkClick/' + id,
    method: 'get'
  })
}

// 通过宣讲会id查看关注
export function getCareerTalkFocus(id) {
  return request({
    url: '/focus/getCareerTalkFocus/' + id,
    method: 'get'
  })
}