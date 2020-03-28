// pages/todos/create.js
var app = getApp();
var fun = require('../../../utils/fun.js');
var graceChecker = require("../../../graceUI/jsTools/graceChecker.js");
var dateTimePicker = require('../../../utils/dateTimePicker.js');

Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		dateTimeArray: null,
		dateTime: null,
		todo: {
			content: '',
			address: ''
		},
		startYear: 2000,
		endYear: 2050,
		rule: [{
				name: "content",
				checkType: "notnull",
				errorMsg: "待办事项不能为空"
		}]
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function(options) {
		fun.pageParam(this, options)
		if (this.data.pageParam.tid) {
			wx.setNavigationBarTitle({
				title: '更新任务'
			})
			l_loadData(this)
		} else {
			wx.setNavigationBarTitle({
				title: '新建任务'
			})
			// 获取完整的年月日 时分秒，以及默认显示的数组
			var obj = dateTimePicker.dateTimePicker(this.data.startYear, this.data.endYear);
			this.setData({
				dateTime: obj.dateTime,
				dateTimeArray: obj.dateTimeArray
			});
		}

	},
	changeDateTime(e) {
		this.setData({
			dateTime: e.detail.value
		});
	},
	changeDateTimeColumn(e) {
		var arr = this.data.dateTime,
			dateArr = this.data.dateTimeArray;

		arr[e.detail.column] = e.detail.value;
		dateArr[2] = dateTimePicker.getMonthDay(dateArr[0][arr[0]], dateArr[1][arr[1]]);

		this.setData({
			dateTimeArray: dateArr,
			dateTime: arr
		});
	},
	/**
	 * 分享
	 */
	onShareAppMessage: function(options) {

	},

	handleContentChange(e) {
		this.setData({
			'todo.content': e.detail.value
		})
	},
	/**
	 * 描述输入事件
	 */
	handleAddressChange(e) {
		this.setData({
			'todo.address': e.detail.value
		})
	},

	/**
	 * 取消按钮点击事件
	 */
	handleCancelTap(e) {
		wx.navigateBack()
	},

	/**
	 * 保存按钮点击事件
	 */
	handleSaveTap() {
		var that=this;
		var formData = this.data.todo;
		formData.time1=this.data.dateTimeArray[0][this.data.dateTime[0]] + '-' + this.data.dateTimeArray[1][this.data.dateTime[
			1]] + '-' + this.data.dateTimeArray[2][this.data.dateTime[2]];
		formData.time2=this.data.dateTimeArray[3][this.data.dateTime[
			3]] + ':' + this.data.dateTimeArray[4][this.data.dateTime[4]] + ':' + this.data.dateTimeArray[5][this.data.dateTime[
			5]];
		var checkRes = graceChecker.check(formData, this.data.rule);
		if (checkRes) {
			if (that.data.pageParam.tid) {
				c_formUpdate(that, formData)
			} else {
				c_formAdd(that, formData)
			}
		} else {
			wx.showToast({
				title: graceChecker.error,
				icon: "none"
			});
		}
	}
})
// 加载数据
function l_loadData(pg) {
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/todo/get/'+pg.data.pageParam.tid,
		method: 'get'
	};
	fun.loadData(pg, opts).then(res => {
		pg.setData({
			todo: res
		})
		var obj = dateTimePicker.dateTimePicker(pg.data.startYear, pg.data.endYear,res.time);
		pg.setData({
			dateTime: obj.dateTime,
			dateTimeArray: obj.dateTimeArray
		});
	})
}
// 添加简历
function c_formAdd(pg, data) {
	var opts = {
		values: data,
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/todo',
		method: 'post'
	};
	fun.loadData(pg, opts).then(res => {
		wx.showToast({
			icon: 'success',
			title: '新建成功',
			duration: 3000
		})
		var time = setTimeout(function() {
			wx.navigateBack()
			clearTimeout(time)
			wx.hideToast()
		}, 1000)
	})
}
// 修改简历
function c_formUpdate(pg, data) {
	data.id = pg.data.pageParam.tid;
	var opts = {
		values: data,
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/todo',
		method: 'put'
	};
	fun.loadData(pg, opts).then(res => {
		wx.showToast({
			icon: 'success',
			title: '更新成功',
			duration: 3000
		})
		var time = setTimeout(function() {
			wx.navigateBack()
			clearTimeout(time)
			wx.hideToast()
		}, 1000)
	})
}
