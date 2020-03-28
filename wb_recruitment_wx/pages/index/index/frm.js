var app = getApp()
var fun = require('../../../utils/fun.js');
Page({
	data: {
		recruitmentInfoList: '',
		careerTalkList: '',
		motto: 'Uoffer',
		userInfo: {},
		hasUserInfo: false,
		canIUse: wx.canIUse('button.open-type.getUserInfo'),
		bannerArr: [{
				file: '../../../images/timg.jpg',
			},
			{
				file: '../../../images/timg1.jpg',
			}
		],
		gonggaoArr: [{
				header: '中国人才招聘网...',
			},
			{
				header: '找工作上智联...',
			},
			{
				header: 'BOSS直聘网...',
			}
		],
		companyArr: [{
				id: '1',
				ent_name: '合肥三物信息技术有限公司',
				job_requirements: '8K-10K',
				position: '平面设计',
				linkman: '田然',
				linkman_mobile: '13225756520',
				ent_address: '运城市盐湖区'
			},
			{
				id: '2',
				ent_name: '合肥三物信息技术有限公司',
				job_requirements: '10K-15K',
				position: 'java开发工程师',
				linkman: '田然',
				linkman_mobile: '13225756520',
				ent_address: '运城市盐湖区'
			}
		],
		listCat: 0,
	},
	onLoad: function() {
		l_loadData(this)
	},
	tuiTabFunc: function() {
		this.setData({
			listCat: 0,
		})
	},
	reTabFunc: function() {
		this.setData({
			listCat: 1,
		})
	},
	//查看招聘信息详情
	detailFunc: function(e) {
		c_jobDetail(this, e)
	}
})
// 加载初始数据
function l_loadData(pg) {
	fun.loadingShow()
	if (wx.getStorageSync('token')) {
		var header = {
			"Content-Type": "application/json",
			'WXAuthorization': wx.getStorageSync('token')
		}
	} else {
		var header = {
			"Content-Type": "application/json",
		}
	}
	wx.request({
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/index',
		data: {},
		header: header,
		method: "get",
		success: function(res) {
			pg.setData({
				// 招聘信息
				recruitmentInfoList: res.data.data.recruitmentInfoList,
				// 宣讲会信息
				careerTalkList: res.data.data.careerTalkList
			})
		},
		fail: function(err) {
			wx.showModal({
				title: '友情提示',
				content: '出错啦，请稍后重试',
				showCancel: false
			})
		},
		complete: function() {
			fun.loadingHide()
		}
	})
}
// 事件处理
// 查看招聘信息详情
function c_jobDetail(pg, e) {
	var id = e.currentTarget.dataset.id
	var type = e.currentTarget.dataset.type
	if (wx.getStorageSync('token')) {
		wx.navigateTo({
			url: '/pages/index/details/frm?type=' + type + '&id=' + id
		})
	} else {
		wx.navigateTo({
			url: '/pages/login/login'
		})
	}
}
