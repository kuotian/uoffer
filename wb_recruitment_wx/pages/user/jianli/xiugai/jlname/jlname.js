// pages/user/jianli/xiugai/jlname/jlname.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    vaput:""
  },
  inputFunc: function (e) {
    this.setData({
      vaput: e.detail.value
    })
  },
  delFunc:function(){
    this.setData({
      vaput: ''
    })
  },
  submitFunc:function(e){
    console.log(e)
    var vaput = this.data.vaput;
    console.log(vaput)
    if (vaput==''){
      wx.showToast({
        title: '请填写简历名称',
        icon: 'none',
        duration: 2000
      })
      return
    }
    setTimeout(function (lazy) {
      wx.navigateBack({

      })
    }, 500)
    
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