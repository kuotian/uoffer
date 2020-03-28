//app.js
App({
  onLaunch: function () {
    var that=this
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              that.globalData.userInfo = res.userInfo
              wx.login({
                success: function (res) {
                  wx.request({
                    url: that.globalData.urlPath + '/'+that.globalData.mainMDL+'/auth/doLogin',
                    method: 'POST',
                    header: {
                      "Content-Type": "application/json"
                    },
                    data: {
                      code: res.code,
                      userInfo: that.globalData.userInfo
                    },
                    success: function (res) {
                      wx.setStorageSync('openId',res.data.data.openId)
                      wx.setStorageSync('token',res.data.data.token)
                      that.globalData.openId = res.data.data.openId;
                      that.globalData.token = res.data.data.token;
                    },
                    fail: function (err) {},
                    complete: function () {}
                  })
                }
              });
              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }
      }
    })
  },
  //全局变量
  globalData: {
    openId: null,
    userInfo: null,
    token: null,
    urlPath: 'http://121.199.49.84:9000',
    mainMDL: 'wxApi'
  }
})