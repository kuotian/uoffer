var app = getApp()
var fun = require('../../../utils/fun.js')
Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		pageParam: {},
		pageData: {},
		val: '',
		active1: false,
		active2: false,
		discusslist: []
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function(options) {
		fun.pageParam(this, options)
		l_loadData(this)
	},
	valChange: function(e) {
		this.setData({
			val: e.detail.value
		})
	},
	discussTap: function() {
		c_discussTap(this)
	},
	collectTap: function() {
		c_collectTap(this)
	},
	loveTap: function() {
		c_loveTap(this)
	},
	apply:function(){
		c_apply(this)
	},
	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function() {

	},

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow: function() {

	},

	/**
	 * 生命周期函数--监听页面隐藏
	 */
	onHide: function() {

	},

	/**
	 * 生命周期函数--监听页面卸载
	 */
	onUnload: function() {

	},

	/**
	 * 页面相关事件处理函数--监听用户下拉动作
	 */
	onPullDownRefresh: function() {

	},

	/**
	 * 页面上拉触底事件的处理函数
	 */
	onReachBottom: function() {

	},

	/**
	 * 用户点击右上角分享
	 */
	onShareAppMessage: function() {

	}
})
// 加载数据
function l_loadData(pg) {
	if (pg.data.pageParam.type == 'job') {
		var url = app.globalData.urlPath + '/' + app.globalData.mainMDL + '/index/recruitment/' + pg.data.pageParam.id
		wx.setNavigationBarTitle({
			title: '招聘'
		})
	} else {
		var url = app.globalData.urlPath + '/' + app.globalData.mainMDL + '/index/careerTalk/' + pg.data.pageParam.id
		wx.setNavigationBarTitle({
			title: '宣讲会'
		})
	}
	var opts = {
		values: {},
		url: url,
		method: 'get'
	};
	fun.loadData(pg, opts).then(res => {
		pg.setData({
			pageData: res
		})
	})
}
// ======================
function c_discussTap(pg) {
	var data = {
		typeId: pg.data.pageParam.id,
		content: pg.data.val
	}
	if (pg.data.pageParam.type == 'job') {
		data.type = 1
	} else {
		data.type = 2
	}
	var opts = {
		values: data,
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/exchange/comment',
		method: 'put'
	};
	fun.loadData(pg, opts).then(res => {
		pg.setData({
			val: ''
		})
		wx.showToast({
			icon: 'success',
			title: '留言成功',
			duration: 3000
		})
		var time = setTimeout(function() {
			clearTimeout(time)
			l_loadData(pg)
			wx.hideToast()
		}, 1000)
	})
}

function c_collectTap(pg) {
	var data = {
		typeId: pg.data.pageParam.id,
	}
	if (pg.data.pageParam.type == 'job') {
		data.type = 1
	} else {
		data.type = 2
	}
	var opts = {
		values: data,
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/exchange/focus',
		method: 'put'
	};
	fun.loadData(pg, opts).then(res => {
		pg.setData({
			active1: true,
			'pageData.focusCount':pg.data.pageData.focusCount+1
		})
		wx.showToast({
			icon: 'success',
			title: '关注成功',
			duration: 3000
		})
		var time = setTimeout(function() {
			clearTimeout(time)
			wx.hideToast()
		}, 1000)
	})
}

function c_loveTap(pg) {
	var data = {
		typeId: pg.data.pageParam.id
	}
	if (pg.data.pageParam.type == 'job') {
		data.type = 1
	} else {
		data.type = 2
	}
	var opts = {
		values: data,
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/exchange/click',
		method: 'put'
	};
	fun.loadData(pg, opts).then(res => {
		pg.setData({
			active2: true,
			'pageData.clickCount':pg.data.pageData.clickCount+1
		})
		wx.showToast({
			icon: 'success',
			title: '点赞成功',
			duration: 3000
		})
		var time = setTimeout(function() {
			clearTimeout(time)
			wx.hideToast()
		}, 1000)
	})
}
function c_apply(pg){
	var id=pg.data.pageParam.id
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/index/sendResume/'+id,
		method: 'put'
	};
	fun.loadData(pg, opts).then(res => {
		wx.showToast({icon: 'success',title:'简历已投递',duration: 3000})
		var time=setTimeout(function(){
			clearTimeout(time)
			wx.hideToast()
		},1000)
	})
}
