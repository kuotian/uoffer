var app = getApp();
var fun = require('../../../utils/fun.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    listCat: 0,
    companyArr: [
      {
        name: '合肥三物信息技术有限公司',
        tlitle: '8k-10k',
        type: '平面设计',
        address: '运城市盐湖区',
        diqu: '盐湖',
      }
    ],
  },
  oneTabFunc: function (e) {
    this.setData({
      listCat: 0,
    })
	l_loadData(this,1)
  },
  twoTabFunc: function (e) {
    this.setData({
      listCat: 1,
    })
	l_loadData(this,2)
  },
  fourTabFunc: function (e) {
    this.setData({
      listCat: 3,
    })
	l_loadData(this,4)
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
	this.setData({listCat:0})
	l_loadData(this,1)
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
function l_loadData(pg,type) {
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/schedule/'+type,
		method: 'get'
	};
	fun.loadData(pg, opts).then(res => {
		pg.setData({
			companyArr: res,
			isNull:res.length
		})
	})
}