// pages/index/search/search.js
const utils = require('../../utils/util.js')
var app = getApp();
var fun = require('../../utils/fun.js');

Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		searchRecord: [],
		searchResult: [],
		showGoods: false,
		recordTouchStart_time: 0,
		recordTouchEnd_time: 0,
		searchValue: ""
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function(options) {
		// 搜索记录
		var searchRecord = wx.getStorageSync("searchRecord") || [];
		this.setData({
			searchRecord: searchRecord
		})
	},
	// 搜索记录 手指触摸动作开始
	recordTouchStart: function(e) {
		let that = this;
		that.setData({
			recordTouchStart_time: e.timeStamp
		})
	},
	// 搜索记录 手指触摸动作结束
	recordTouchEnd: function(e) {
		let that = this;
		that.setData({
			recordTouchEnd_time: e.timeStamp
		})
	},
	// 搜索记录 点击
	recordFunc: function(e) {
		let that = this;
		//触摸时间距离页面打开的毫秒数  
		var touchTime = that.data.recordTouchEnd_time - that.data.recordTouchStart_time;
		console.log(touchTime);
		//如果按下时间大于350为长按  
		if (touchTime > 350) {
			//长按删除

		} else {
			//点击
			var tag = e.currentTarget.dataset.idx;
			var searchRecord = wx.getStorageSync("searchRecord") || [];
			var str = searchRecord[tag];
			this.setData({
				searchValue: str,
			})
			this.loadData(str);
		}

	},
	// 删除搜索记录
	deleFunc: function(tag) {
		var that = this;
		wx.showModal({
			title: '温馨提示',
			content: '您将删除该搜索记录！',
			success: function(res) {
				if (res.confirm) {
					var searchRecord = wx.getStorageSync("searchRecord") || [];
					searchRecord.splice(tag, 1);
					wx.setStorageSync("searchRecord", searchRecord);
					that.setData({
						searchRecord: searchRecord
					})
				}
			}
		})
	},
	// 搜索记录 长按
	recordDelFunc: function(e) {
		var tag = e.currentTarget.dataset.idx;

		this.deleFunc(tag);

	},
	// 取消搜索
	clearFunc: function() {
		var searchRecord = wx.getStorageSync("searchRecord") || [];
		this.setData({
			searchRecord: searchRecord,
			searchResult: [],
			showGoods: false,
			searchValue: ""
		})
	},
	// 搜索关键字 
	infoInput: function(e) {
		this.setData({
			info: e.detail.value
		})
	},
	// 搜索职位
	searchFunc: function(e) {
		var str = this.data.info;
		if (str == "") {
			return;
		}
		var searchRecord = wx.getStorageSync("searchRecord") || [];
		for (var index = 0; index < searchRecord.length; index++) {
			if (searchRecord[index] == str) {
				this.loadData(str);
				return;
			}
		}
		searchRecord.push(str);
		wx.setStorageSync("searchRecord", searchRecord);
		this.loadData(str);
		this.setData({
			searchRecord: searchRecord
		})
	},
	// 搜索数据
	loadData: function(key) {
		var that = this;
		var opts = {
			values: {
				position: key
			},
			url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/index/search',
			method: 'get'
		};
		fun.loadData(that, opts).then(res => {
			wx.stopPullDownRefresh();
			that.setData({
				searchResult: res,
				showGoods: true
			})
		})
	},
	apply:function(e){
		c_apply(this,e)
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
		if (!this.data.searchValue) {
			wx.stopPullDownRefresh();
			return;
		}
		this.loadData(this.data.searchValue);
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

	},
	//查看招聘信息详情
	detailFunc: function(e) {
		c_jobDetail(this, e)
	}
})
function c_apply(pg,e){
	var id=e.currentTarget.dataset.pid
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
// 查看招聘信息详情
function c_jobDetail(pg, e) {
	var id = e.currentTarget.dataset.id
	if (wx.getStorageSync('token')) {
		wx.navigateTo({
			url: '/pages/index/details/frm?type=job&id=' + id
		})
	} else {
		wx.navigateTo({
			url: '/pages/login/login'
		})
	}
}
