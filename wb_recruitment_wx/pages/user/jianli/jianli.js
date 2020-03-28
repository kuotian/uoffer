var app = getApp();
var fun = require('../../../utils/fun.js');
Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		pageData: [],
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
	// 选中简历
	xuanzeFunc: function(e) {
		var i = e.currentTarget.dataset.i;
		this.setData({
			['pageData[' + i + '].ifhidden']: !this.data.pageData[i].ifhidden,
		})
	},
	// 修改
	xiugaiFunc: function(e) {
		wx.navigateTo({
			url: 'xiugai/xiugai?jid=' + e.currentTarget.dataset.jid,
		})
	},
	// 默认
	isdefaultFunc: function(e) {
		c_isdefaultFunc(this, e)
	},
	// 隐藏
	ishideFunc: function(e) {
		c_ishideFunc(this, e)
	},
	// 更多
	gengduoFunc: function(e) {
		var that = this
		wx.showActionSheet({
			itemList: ['删除'],
			success: function(res) {
				c_delete(that, e)
			},
			fail: function(res) {
				debugger
				console.log(res.errMsg)
			}
		})
	},
	// 去找好工作
	selectFunc: function() {
		wx.switchTab({
			url: '../../index/index/frm'
		})
	},
	// 添加简历
	addFunc: function() {
		wx.navigateTo({
			url: 'xiugai/xiugai',
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
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/resume',
		method: 'get'
	};
	fun.loadData(pg, opts).then(res => {
		res.forEach(function(item, index, array) {
			item.ifhidden = false
		})
		pg.setData({
			pageData: res
		})
	})
}
// 设置默认
function c_isdefaultFunc(pg, e) {
	var id = e.currentTarget.dataset.jid;
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/resume/idDefault/' + id,
		method: 'put'
	};
	fun.loadData(pg, opts).then(res => {
		wx.showToast({
			icon: 'success',
			title: '设置默认成功',
			duration: 3000
		})
		var time = setTimeout(function() {
			l_loadData(pg)
			clearTimeout(time)
			wx.hideToast()
		}, 1000)

	})
}
// 设置隐藏，显示
function c_ishideFunc(pg, e) {
	var id = e.currentTarget.dataset.jid;
	var i = e.currentTarget.dataset.i;
	var isHide = pg.data.pageData[i].isHide;
	var data = {
		id: id,
		isHide: !isHide
	}
	var opts = {
		values: data,
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/resume/isHide',
		method: 'put'
	};
	fun.loadData(pg, opts).then(res => {
		wx.showToast({
			icon: 'success',
			title: isHide ? '公开成功' : '隐藏成功',
			duration: 3000
		})
		var time = setTimeout(function() {
			pg.setData({
				['pageData[' + i + '].isHide']: !isHide
			})
			wx.hideToast()
			clearTimeout(time)
		}, 1000)

	})
}
// 删除
function c_delete(pg, e) {
	wx.showModal({
		title: '提示',
		content: '确认删除？',
		success(res) {
			if (res.confirm) {
				var id = e.currentTarget.dataset.jid;
				var i = e.currentTarget.dataset.i;
				var opts = {
					values: {},
					url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/resume/'+id,
					method: 'delete'
				};
				fun.loadData(pg, opts).then(res => {
					wx.showToast({
						icon: 'success',
						title: '删除成功',
						duration: 3000
					})
					var time = setTimeout(function() {
						var data=pg.data.pageData;
						data.splice(i,1)
						pg.setData({
							pageData:data
						})
						wx.hideToast()
						clearTimeout(time)
					}, 1000)
				
				})
			}
		}
	})

}
