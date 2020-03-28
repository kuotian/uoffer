//index.js
var app = getApp()
//获取应用实例
Page({
  data: {
    motto: '',
    userInfo: [],
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    scenceData: '',
  },
  onLoad: function (options) {

  },
  cancel: function (e) {
    wx.navigateBack()
  },
  getUserInfo: function (e) {
    if (e.detail.userInfo) {
      wx.showLoading({title:'登陆中'})
      //插入登录的用户的相关信息到数据库
      app.globalData.userInfo = e.detail.userInfo;
      wx.login({
        success: function (res) {
          wx.request({
            url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/auth/doLogin',
            method: 'POST',
            header: {
              'content-type': 'application/json'
            },
            data: {
              code: res.code,
              userInfo: app.globalData.userInfo
            },
            success: function (res) {
              wx.setStorageSync('openId', res.data.data.openId)
              wx.setStorageSync('token', res.data.data.token)
              app.globalData.openId = res.data.data.openId;
              app.globalData.token = res.data.data.token;
              wx.navigateBack()
            },
            fail: function (err) {
              wx.showToast({
                title: '登录请求失败',
                icon: 'none',
                duration: 2000
              })
            },
            complete: function () {
              wx.hideLoading()
            }
          })
        }
      });
    } else {
      //用户按了拒绝按钮
      wx.showModal({
        title: '友情提示',
        content: '您拒绝了登陆，将无法使用部分功能!',
        showCancel: false,
        confirmText: '返回授权'
      })
    }
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
})