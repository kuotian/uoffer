var app = getApp();
var fun = require('../../../utils/fun.js')

Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		form: {
			wxId: '',
			mobile: '',
			school: '',
			isStudent: true
		}
	},
	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function(options) {

	},
	submitFunc: function(e) {
		c_submitFunc(this, e)
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
		l_loadData(this)
	},
	switchChange: function(e) {
		this.setData({
			'form.isStudent': e.detail.value
		})
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
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/getWxUser',
		method: 'get'
	};
	fun.loadData(pg, opts).then(res => {
		pg.setData({
			form: res
		})
	})
}

function c_submitFunc(pg, e) {
	if (e.detail.value.isStudent) {
		if (e.detail.value.school === '') {
			return wx.showToast({
				title: '请填写所属学校',
				icon:'none'
			})
		}
	}
	var opts = {
		values: e.detail.value,
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/dataEntry',
		method: 'put'
	};
	fun.loadData(pg, opts).then(res => {
		wx.showToast({
			icon: 'success',
			title: '保存成功',
			duration: 3000
		})
		var time = setTimeout(function() {
			clearTimeout(time)
			wx.hideToast()
		}, 1000)
	})
}
