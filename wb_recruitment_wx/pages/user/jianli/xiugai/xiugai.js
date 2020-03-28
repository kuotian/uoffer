var app = getApp();
var fun = require('../../../../utils/fun.js')
var graceChecker = require("../../../../graceUI/jsTools/graceChecker.js");
Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		pageParam: {},
		form: {
			title: '',
			name: '',
			age: '',
			gender: '',
			birthday: '2000-11-28',
			workingHours: '2018-11-28',
			nowAddress: '',
			domicileAddress: '',
			mobile: '',
			email: '',
			jobIntention: '',
			education: '',
			schoolOfGraduation: '',
			major: '',
			workExperience: '',
			projectExperience: '',
			selfAppraisal: '',
			isHide: 0,
		},
		gender: ['请选择性别', '男', '女'],
		genderIndex: 0,
		education: ['请选择学历', '博士', '研究生', '本科', '大专', '高中', '初中', '小学'],
		educationIndex: 0,
		pageData: {}
	},
	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function(options) {
		fun.pageParam(this, options)
		if (this.data.pageParam.jid) {
			wx.setNavigationBarTitle({
			  title: '修改简历'
			})
			l_loadData(this)
		} else {
			wx.setNavigationBarTitle({
			  title: '创建新简历'
			})
		}
	},
	formSubmit: function(e) {
		var that=this;
		console.log(e.detail.value);
		//定义表单规则
		var rule = [{
				name: "title",
				checkType: "notnull",
				errorMsg: "简历名称不能为空"
			},
			{
				name: "name",
				checkType: "notnull",
				errorMsg: "姓名不能为空"
			},
			{
				name: "age",
				checkType: "notnull",
				errorMsg: "年龄不能为空"
			},
			{
				name: "gender",
				checkType: "in",
				checkRule: "1,2",
				errorMsg: "请选择性别"
			},
			{
				name: "mobile",
				checkType: "phoneno",
				errorMsg: "请填写正确的手机号码"
			},
			{
				name: "email",
				checkType: "email",
				errorMsg: "请填写正确的邮箱号"
			}
		];
		//进行表单检查 e.detail.value 内存放着表单数据
		var formData = e.detail.value;
		var checkRes = graceChecker.check(formData, rule);
		if (checkRes) {
			formData.gender=that.data.gender[that.data.genderIndex]
			formData.education=that.data.education[that.data.educationIndex]
			if (that.data.pageParam.jid) {
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
	},
	//预览简历
	yulanFunc: function() {
		var that=this
		wx.navigateTo({
			url: 'yulan/yulan?jid='+that.data.pageParam.jid,
		})
	},
	// 刷新简历
	refresh:function(){
		c_refresh(this)
	},
	bindPickerChange: function(e) {
		console.log(e);
		this.setData({
			genderIndex: e.detail.value
		});
	},
	bindDatePickerChange: function(e) {
		console.log(e);
		this.setData({
			['form.birthday']: e.detail.value
		});
	},
	bindWorkPickerChange: function(e) {
		console.log(e);
		this.setData({
			['form.workingHours']: e.detail.value
		});
	},
	bindEducationChange: function(e) {
		console.log(e);
		this.setData({
			educationIndex: e.detail.value
		});
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
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/resume/' + pg.data.pageParam.jid,
		method: 'get'
	};
	fun.loadData(pg, opts).then(res => {
		var data=res;
		for (let i = 0; i < pg.data.gender.length; i++) {
			if(data.gender==pg.data.gender[i]){
				pg.data.genderIndex=i;
				data.gender=i
			}
		}
		for (let j = 0; j < pg.data.education.length; j++) {
			if(data.education==pg.data.education[j]){
				pg.data.educationIndex=j;
				data.education=j
			}
		}
		pg.setData({
			form: data
		})
	})
}
// 刷新简历
function c_refresh(pg){
	var opts = {
		values: {},
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/resume/refresh/'+pg.data.pageParam.jid,
		method: 'put'
	};
	fun.loadData(pg, opts).then(res => {
		wx.showToast({icon: 'success',title:'简历刷新成功',duration: 3000})
		var time=setTimeout(function(){
			wx.navigateBack()
			clearTimeout(time)
			wx.hideToast()
		},1000)
	})
}
// 添加简历
function c_formAdd(pg, data) {
	var opts = {
		values: data,
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/resume',
		method: 'post'
	};
	fun.loadData(pg, opts).then(res => {
		wx.showToast({icon: 'success',title:'创建成功',duration: 3000})
		var time=setTimeout(function(){
			wx.navigateBack()
			clearTimeout(time)
			wx.hideToast()
		},1000)
	})
}
// 修改简历
function c_formUpdate(pg, data) {
	data.id = pg.data.pageParam.jid;
	var opts = {
		values: data,
		url: app.globalData.urlPath + '/' + app.globalData.mainMDL + '/center/resume',
		method: 'put'
	};
	fun.loadData(pg, opts).then(res => {
		wx.showToast({icon: 'success',title:'修改成功',duration: 3000})
		var time=setTimeout(function(){
			wx.navigateBack()
			clearTimeout(time)
			wx.hideToast()
		},1000)
	})
}
