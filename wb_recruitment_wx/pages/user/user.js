var app=getApp()
Page({
  data: {},
  //我的简历
  jianliFunc:function(){
    if(wx.getStorageSync('token')){
      wx.navigateTo({
        url: 'jianli/jianli'
      })
    }else{
      wx.navigateTo({
        url: '/pages/login/login'
      })
    }
  },
  jinduFunc: function () {
    if(wx.getStorageSync('token')){
      wx.navigateTo({
        url: 'jindu/jindu'
      })
    }else{
      wx.navigateTo({
        url: '/pages/login/login'
      })
    }
  },
  mianshiFunc: function () {
    if(wx.getStorageSync('token')){
      wx.navigateTo({
        url: 'mianshi/mianshi'
      })
    }else{
      wx.navigateTo({
        url: '/pages/login/login'
      })
    }
  },
  collectFunc: function () {
    if(wx.getStorageSync('token')){
      wx.navigateTo({
        url: 'collect/index'
      })
    }else{
      wx.navigateTo({
        url: '/pages/login/login'
      })
    }
  },
  todoFunc: function () {
    if(wx.getStorageSync('token')){
      wx.navigateTo({
        url: 'todo/index'
      })
    }else{
      wx.navigateTo({
        url: '/pages/login/login'
      })
    }
  },
  handleInfo:function(){
	  if(wx.getStorageSync('token')){
		  wx.navigateTo({url:'/pages/user/information/information'})
	  }else{
		  wx.navigateTo({url:'/pages/login/login'})
	  }
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