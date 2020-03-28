var app = getApp();
var fun = require('../../../../utils/fun.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
  
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
	fun.pageParam(this, options)
	l_loadData(this)
  },
  accept:function(){
	  c_accept(this)
  },
  tapPhone:function(){
	var tel=this.data.pageData.linkmanMobile
	wx.makePhoneCall({
	  phoneNumber:tel  //仅为示例，并非真实的电话号码
	})  
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
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/getInfo/' + pg.data.pageParam.mid,
		method: 'get'
	};
	fun.loadData(pg, opts).then(res => {
		var data=res;
		pg.setData({
			pageData: data
		})
	})
}
function c_accept(pg){
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/accept/'+pg.data.pageParam.mid,
		method: 'put'
	};
	fun.loadData(pg, opts).then(res => {
		wx.showToast({icon: 'success',title:'已接受邀请',duration: 3000})
		var time=setTimeout(function(){
			wx.navigateBack()
			clearTimeout(time)
			wx.hideToast()
		},1000)
	})
}