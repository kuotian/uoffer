var app = getApp();
var fun = require('../../../utils/fun.js');
Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		pageData:[]
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {

	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function () {

	},

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow: function () {
		l_loadData(this)
	},
	//查看招聘信息详情
	detailFunc: function(e) {
		c_jobDetail(this, e)
	},
	/**
	 * 生命周期函数--监听页面隐藏
	 */
	onHide: function () {

	},

	/**
	 * 生命周期函数--监听页面卸载
	 */
	onUnload: function () {

	},

	/**
	 * 页面相关事件处理函数--监听用户下拉动作
	 */
	onPullDownRefresh: function () {

	},

	/**
	 * 页面上拉触底事件的处理函数
	 */
	onReachBottom: function () {

	},

	/**
	 * 用户点击右上角分享
	 */
	onShareAppMessage: function () {

	}
})
// 加载数据
function l_loadData(pg) {
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/exchange/focus',
		method: 'get'
	};
	fun.loadData(pg, opts).then(res => {
		pg.setData({
			pageData: res,
			isNull:res.length
		})
	})
}
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