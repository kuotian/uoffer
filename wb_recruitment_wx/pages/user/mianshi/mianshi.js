var app = getApp();
var fun = require('../../../utils/fun.js');
Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		listCat: 0,
		companyArr: [{
			name: '合肥三物信息技术有限公司',
			tlitle: '10k-15k',
			type: 'java开发工程师',
			address: '安徽省合肥市蜀山区'
		}],
	},
	detailsFunc: function(e) {
		wx.navigateTo({
			url: 'details/details?mid='+e.currentTarget.dataset.mid,
		})
	},
	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function(options) {
		
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
function l_loadData(pg,type) {
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/schedule/3',
		method: 'get'
	};
	fun.loadData(pg, opts).then(res => {
		pg.setData({
			companyArr: res,
			isNull:res.length
		})
	})
}
