//获取应用实例
const app = getApp()
var fun = require('../../../utils/fun.js')

Page({
  data: {
    // todos
    todos: [],
	today:[],
	tomorrow:[],
	date:'搜索待办事项',
	showGoods: false,
	searchResult: [],
	todayCount:true,
	tomorrowCount:true,
    // todo 计数
    uncompletedCount: 0,
    completedCount: 0,

    // 是否动画延迟
    delay: true
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow () {
    // 为了新建后列表能更新，此逻辑必须写在 onShow 事件
	l_loadData(this)
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide () {
	l_loadData(this)
  },

  /**
   * 分享
   */
  onShareAppMessage: function (options) {

  },
  /**
   * Todo 长按事件
   */
  handleTodoLongTap(e) {
	let e1=e;
    wx.showModal({
      title: '删除提示',
      content: '确定要删除这项任务吗？',
      success: (e) => {
        if (e.confirm) {
		  c_handleTodoDel(this,e1)
        }
      }
    })
  },
  /**
   * 新建事件
   */
  handleAddTodo (e) {
    wx.navigateTo({
      url: '../todo/create'
    })
  },
  handleTodoUplate (e) {
    wx.navigateTo({
      url: '../todo/create?tid='+e.currentTarget.dataset.toid
    })
  },
  // 取消搜索
  clearFunc: function() {
  	var searchRecord = wx.getStorageSync("searchRecord") || [];
  	this.setData({
  		searchResult: [],
  		showGoods: false,
  		date: "搜索待办事项"
  	})
  },
  // 搜索关键字 
  bindDateChange: function(e) {
  	this.setData({
  		date: e.detail.value
  	})
	c_searchTodo(this,e)
	
  }
})
// 加载数据
function l_loadData(pg) {
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/todo',
		method: 'get'
	};
	fun.loadData(pg, opts).then(res => {
		pg.setData({
			today: res.today,
			todayCount:res.today.length,
			tomorrow:res.tomorrow,
			tomorrowCount:res.tomorrow.length
		})
		let title = ['TodoList（今日待办: ', res.today.length, ', 明日待办: ', res.tomorrow.length, '）'].join('')
		wx.setTopBarText({ text: title })
		// 动画结束后取消动画队列延迟
		setTimeout(() => {
			pg.setData({delay: false})
		}, 2000)
	})
}
// 删除
function c_handleTodoDel(pg,e){
	let id = e.currentTarget.dataset.toid;
	let index = e.currentTarget.dataset.index;
	let type=e.currentTarget.dataset.type;
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/todo/'+id,
		method: 'delete'
	};
	fun.loadData(pg, opts).then(res => {
		wx.showToast({
			icon: 'success',
			title: '删除成功',
			duration: 3000
		})
		var time = setTimeout(function() {
			wx.hideToast()
			l_loadData(pg)
			clearTimeout(time)
		}, 1000)
	})
}
// 加载数据
function c_searchTodo(pg,e) {
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/todo/'+e.detail.value,
		method: 'get'
	};
	fun.loadData(pg, opts).then(res => {
		pg.setData({
			searchResult: res,
			showGoods: true
		})
	})
}