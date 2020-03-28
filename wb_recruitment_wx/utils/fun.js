var api = require('./api.js');
// @@ 页面参数
function fun_pageParam(pg, options) {
    console.log('参数：' + JSON.stringify(options));
    if (options.pageParam) {
        var params = JSON.parse(options.pageParam);
        pg.setData({
            pageParam: params
        });
    } else {
        if (options) pg.setData({
            pageParam: options
        });
    };
}

/***
 * wxAutoImageCal 计算宽高
 * 
 * 参数 e: iamge load函数的取得的值
 * 返回计算后的图片宽高
 * {
 *  imageWidth: 100px;
 *  imageHeight: 100px;
 * }
 */
function fun_AutoImageCal(e) {
    //console.dir(e);
    //获取图片的原始长宽
    var originalWidth = e.detail.width;
    var originalHeight = e.detail.height;
    var windowWidth = 0,
        windowHeight = 0;
    var autoWidth = 0,
        autoHeight = 0;
    var results = {};
    wx.getSystemInfo({
        success: function (res) {
            //console.dir(res);
            windowWidth = res.windowWidth;
            windowHeight = res.windowHeight;
            //判断按照那种方式进行缩放
            //console.log("windowWidth"+windowWidth);
            if (originalWidth > windowWidth) { //在图片width大于手机屏幕width时候
                autoWidth = windowWidth;
                //console.log("autoWidth"+autoWidth);
                autoHeight = (autoWidth * originalHeight) / originalWidth;
                //console.log("autoHeight"+autoHeight);
                results.imageWidth = autoWidth;
                results.imageheight = autoHeight;
            } else { //否则展示原来的数据
                results.imageWidth = originalWidth;
                results.imageheight = originalHeight;
            }
        }
    })
    return results;
}

function fun_objLen(obj) {
    var len = 0;
    if (!obj) return len;
    for (var k in obj) {
        len++;
    }
    return len;
}


// @@ ==============================
// @@ 对象操作 v2016-7-18
// @@ ==============================
// ## 合并
function fun_mergeArr(o1, o2) {
    if (o1) {
        //console.log(typeof o1);
        //console.log(typeof o2);
        var len = 0;
        for (var i = 0; i < o1.length; i++) {
            len++;
        }
        if (o2) {
            //console.log(">>");
            //fun_consoleLog(o2);
            //len++;
            for (var i = 0; i < o2.length; i++) {
                //console.log(o2[key]);
                o1[len] = (o2[i]);
                len++;
            }
        }
    } else {
        console.log("error for o1");
    }
    return o1;
}
// ## 合并
function fun_mergeObj(o1, o2) {
    if (o1) {
        //console.log(typeof o1);
        //console.log(typeof o2);
        var len = 0;
        for (var key in o1) {
            len++;
        }
        if (o2) {
            //console.log(">>");
            //fun_consoleLog(o2);
            len++
            for (var key in o2) {
                //console.log(o2[key]);
                o1[key] = (o2[key]);
                len++;
            }
        }
    } else {
        console.log("error for o1");
    }
    return o1;
}
// ## 对象元素数量
function fun_objSize(obj) {
    if (!obj) return 0;
    var len = 0;
    for (var key in obj) {
        len++;
    }
    return len;
}
// ## 元素是否在对象中
function fun_inObj(str, obj) {
    var len = 0;
    for (var key in obj) {
        if (obj[key] == str) len++;
    }
    if (len > 0) {
        return true;
    } else {
        return false;
    }
}

function fun_pushArr(frmArr, frm) {
    var num = 0;
    for (var key in frmArr) {
        if (frmArr[key] == frm) num++;
    }
    if (num == 0) frmArr.push(frm);
    return frmArr;
}

// loading 处理
function fun_loadingShow() {
    wx.showToast({
        title: '加载中',
        icon: 'loading',
        duration: 3000,
        mask: true
    });
    wx.showNavigationBarLoading
}
// @@ 加载 loading
function fun_loadingHide() {
    wx.hideToast();
    wx.hideNavigationBarLoading();
}

// @@ 支付
function fun_pay(app, pg, opts) {
    let loginInfo = null;
    api.getStorage("loginInfo").then(res => {
        console.log(res);
        return new Promise((resolve, reject) => {
            loginInfo = res.data;
            let values = {
                UserID: loginInfo.UserID,
                Token: loginInfo.Token,
                // UnitID: loginInfo.UnitID
            };
            values = fun_mergeObj(values, opts.values);
            console.log(opts.values);
            console.log(values);
            let url = app.globalData.rootURL + '/' + app.globalData.mainMDL + '/Pay/start.html';
            wx.request({
                url: url,
                data: values,
                success: function (res) {
                    resolve(res);
                },
                fail: function (err) {
                    console.log(err);
                    reject("errCode: 305");
                }
            });
        });
    }).then(res => {
        console.log(res);
        return new Promise((resolve, reject) => {
            if (res.errMsg != "request:ok") {
                fun_alert(res.errMsg);
                reject({
                    errCode: 3011,
                    errMsg: res.errMsg
                });
            }
            if (res.data.status != 200) {
                console.log("errCode: 3010");
                fun_alert(res.data.message);
                reject({
                    errCode: 3010,
                    errMsg: res.data.message
                });
            }
            var myPackage = res.data.package;
            if (myPackage.length > 11) {
                wx.requestPayment({
                    'timeStamp': res.data.timeStamp,
                    'nonceStr': res.data.nonceStr,
                    'package': res.data.package,
                    'signType': 'MD5',
                    'paySign': res.data.paySign,
                    success: function (res) {
                        console.log(res);
                        resolve(res);
                    },
                    fail: function (err) {
                        console.log("errCode: 3003");
                        reject(err);
                    }
                })
            } else {
                fun_alert(res.data.message);
                reject("errCode: 3004");
            }
        });
    }).then(res => {
        console.log(res);
        return new Promise((resolve, reject) => {
            if (res.errMsg == "requestPayment:ok") {
                if (typeof (opts.callback) == "function") opts.callback(res);
                resolve("ok");
            } else {
                fun_alert("支付失败");
                reject("err");
            }
        });
    }).finally(res => {
        wx.hideToast();
    }).catch(err => {
        console.log(err);
        wx.hideToast();
        if (err.errMsg == "requestPayment:fail cancel") {
            fun_alert("用户取消支付");
        } else {
            var msg = err.errMsg;
            console.log(msg);
        }
    })
}

// @@ 数组操作
// :: 删除元素
function fun_removeByValue(arr, val) {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] == val) {
            arr.splice(i, 1);
            break;
        }
    }
}
// :: 获取数组元素值
function fun_getArrItemBy(arr, key, type) {
    var rt = null;
    for (var i = 0; i < arr.length; i++) {
        var v = arr[i];
        if (type == "idx") {
            if (v.idx == key) {
                rt = v
            }
        }
        if (type == "id") {
            if (v.id == key) {
                rt = v
            }
        }
    }
    return rt;
}
// :: 修改数组元素值
function fun_setArrItemBy(arr, key, val, type) {
    for (var i = 0; i < arr.length; i++) {
        var v = arr[i];
        if (type == "idx") {
            if (v.idx == key) {
                arr[i] = val
            }
        }
        if (type == "id") {
            if (v.id == key) {
                arr[i] = val
            }
        }
    }
    return arr;
}

// @@ 数据加载
function ajax_LoadData(pg, opts) {
    return new Promise((resolve, reject) => {
        fun_loadingShow()
        wx.request({
            url: opts.url,
            data: opts.values,
            method:opts.method,
            header: {
                'WXAuthorization': wx.getStorageSync('token') // 默认值
            },
            success: function (res) {
                if(res.data.status==200){
                    resolve(res.data.data);
                }else{
                    wx.showModal({title:'友情提示',content:res.data.msg,showCancel:false})
                }
            },
            fail: function (err) {
                wx.showToast({title:'出错了，请重试'})
                var index=setTimeout(function(){
                    wx.hideToast()
                    clearTimeout(index)
                },2000)
                reject({
                    "errCode": 3001
                });
            },
            complete:function(){
                fun_loadingHide();
            }
        })
    })

}

// @@ 加载翻页数据
function ajax_LoadPages(pg, opts) {
    console.log(opts);
    let mode = opts.mode;
    // :: loading
    wx.showNavigationBarLoading();
    // :: 参数

    let loginInfo = null;
    // :: 请求数据
    api.getStorage("loginInfo").then(res => {
        console.log(res);
        return new Promise(function (resolve, reject) {
            loginInfo = res.data;
            if (!loginInfo) loginInfo = {};
            let params = {
                UserID: loginInfo.UserID,
                token: loginInfo.token,
                TrueName: loginInfo.TrueName,
                CurPage: pg.data.CurPage,
                PerPage: pg.data.PerPage
            };
            let values = fun_mergeObj(params, opts.values);
            console.log(values);
            let url = opts.url;
            wx.request({
                url: url,
                method: "GET",
                data: values,
                success: function (res) {
                    resolve(res);
                },
                fail: function (err) {
                    console.log(err);
                    resolve({
                        "errCode": 3001
                    });
                }
            })
        });
    }).then(res => {
        console.log(res);
        return new Promise(function (resolve, reject) {
            let data = res.data;
            if (!data) data = {};
            //console.log(data);
            if (res.errmsg) {
                fun_alert(res.errmsg);
                reject({
                    "errCode": 3002
                });
            };
            if (data.status == 300) {
                pg.setData({
                    NoData: 'show'
                });
                reject({
                    "errCode": 3003
                });
            };
            if (data.status == 301) {
                pg.setData({
                    NoNewData: 'show'
                });
                fun_noNewData(pg);
                reject({
                    "errCode": 3003
                });
            };
            if (data.status == 304) {
                wx.showModal({
                    title: "友情提示",
                    content: "身份验证过期，请重新登录",
                    showCancel: false,
                    success: function (res) {
                        if (res.confirm) {
                            wx.removeStorageSync('loginInfo');
                            wx.switchTab({
                                url: '../../../../page/home/index/index/frm'
                            })
                        }
                    }
                })
                reject({
                    "errCode": 304
                });
            }
            if (data.status != 200) {
                reject({
                    "errCode": 3004
                });
            };
            // :: 数据赋值
            var oTurnPage = pg.data.TurnPage;
            var nTurnPage = res.data.TurnPage;
            console.log(mode);
            if (mode == "infinit") {
                var TurnPage = fun_mergeArr(oTurnPage, nTurnPage);
            } else {
                var TurnPage = nTurnPage;
            }
            // :: 数据赋值
            var oDataPages = pg.data.DataPages;
            var nDataPages = res.data.DataPages;
            console.log(mode);
            if (mode == "infinit") {
                var DataPages = fun_mergeArr(oDataPages, nDataPages);
            } else {
                var DataPages = nDataPages;
            }
            console.log(DataPages);
            pg.setData({
                // TurnPage: TurnPage,
                DataPages: DataPages,
                PerPage: res.data.PerPage,
                AllPage: res.data.AllPage,
                CurPage: res.data.CurPage
            });
            //console.log(pg.data.TurnPage);
            if (typeof (opts.funOk) == "function") opts.funOk(data);
            resolve("ok");
        });
    }).finally(() => {
        pg.setData({
            DataLoading: 'hide'
        })
        wx.stopPullDownRefresh();
        wx.hideNavigationBarLoading();
        wx.hideToast();
    }).catch(err => {
        console.log(err);
    })
}

// @@ 没有新数据
function fun_noNewData(pg) {
    pg.setData({
        NoNewData: "show"
    });
    setTimeout(res => {
        pg.setData({
            NoNewData: "hide"
        });
    }, 1000)
}
module.exports.autoImageCal = fun_AutoImageCal;
module.exports.objLen = fun_objLen;
module.exports.mergeObj = fun_mergeObj;
module.exports.mergeArr = fun_mergeArr;
module.exports.pageParam = fun_pageParam;
module.exports.loadingShow = fun_loadingShow;
module.exports.loadingHide = fun_loadingHide;
module.exports.pay = fun_pay;
module.exports.removeByValue = fun_removeByValue;
module.exports.getArrItemBy = fun_getArrItemBy;
module.exports.loadData = ajax_LoadData; // ::
module.exports.loadPages = ajax_LoadPages; // ::
module.exports.noNewData = fun_noNewData; // ::