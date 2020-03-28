var Promise = require('./es6-promise.js');


// @@ 
// :: 获取 storage
function getStorage(key) {
    //console.log(key);
    return new Promise((resolve, reject) => {
        wx.getStorage({
            key: key,
            success: function(res) {
                //console.log(res);
                resolve(res);
            },
            fail: function(err) {
                console.log(err);
                //reject(err);
                resolve({ status: 300, message: "err" });
            }
        })
    })
}

// :: 设置 storage
function setStorage(key, value) {
    //console.log(value);
    return new Promise((resolve, reject) => {
        wx.setStorage({
            key: key,
            data: value
        });
        resolve(true);
    })
}

// :: 删除 storage
function removeStorage(key) {
    //console.log(value);
    return new Promise((resolve, reject) => {
        wx.removeStorage({
            key: key,
            success: function(res) {
                resolve(res);
            }
        });
    })
}

// :: 批量设置 storage
function setStorageAll(keyArr, valArr) {
    return new Promise((resolve, reject) => {
        var promiseArr = new Array();
        for (let i = 0; i < keyArr.length; i++) {
            let key = keyArr[i];
            let val = valArr[i];
            promiseArr.push(setStorage(key, val));
        }
        resolve(Promise.all(promiseArr));
    })
}

// :: 批量获取 storage
function getStorageAll(keyArr) {
    //console.log(keyArr);
    return new Promise((resolve, reject) => {
        var promiseArr = new Array();
        for (let i = 0; i < keyArr.length; i++) {
            let key = keyArr[i];
            promiseArr.push(getStorage(key));
        }
        resolve(Promise.all(promiseArr));
    })
}

// @@ 无论promise对象最后状态如何都会执行
Promise.prototype.finally = function(callback) {
    let P = this.constructor;
    return this.then(
        value => P.resolve(callback()).then(() => value),
        reason => P.resolve(callback()).then(() => { throw reason })
    );
};

// @@ 上传头像
function chooseImage(opts) {
    return new Promise((resolve, reject) => {
        // :: 选择图片
        if (opts) {
            wx.chooseImage({
                count: opts.count,
                sizeType: opts.sizeType,
                sourceType: opts.sourceType,
                success: function(res) {
                    //console.log(res);
                    resolve(res);
                },
                fail: function(err) {
                    reject(err);
                }
            });
        } else {
            wx.chooseImage({
                count: 1, // 默认9
                sizeType: ['original', 'compressed'],
                sourceType: ['album', 'camera'],
                success: function(res) {
                    //console.log(res);
                    resolve(res);
                },
                fail: function(err) {
                    reject(err);
                }
            });
        }
    })
}
// @@ 上传图片
function uploadFile(opts) {
    return new Promise((resolve, reject) => {
        //console.log(opts);
        if (!opts.name) opts.name = "uploader";
        wx.uploadFile({
            url: opts.url,
            filePath: opts.filePath,
            name: opts.name,
            formData: opts.formData,
            success: res => {
                //console.log(res);
                resolve(res);
            },
            fail: err => {
                //console.log(err);
                reject(err);
            }
        })
    })
}

function uploadFileAll(arr, opts) {
    //console.log(arr);
    if (!arr) {
        return false;
    }
    return new Promise((resolve, reject) => {
        var promiseArr = new Array();
        for (let i = 0; i < arr.length; i++) {
            let v = arr[i];
            opts.filePath = v;
            promiseArr.push(uploadFile(opts));
        }
        resolve(Promise.all(promiseArr));
    })
}

// @@  请求
function getRequest(url, values) {
    //console.log(values);
    return new Promise((resolve, reject) => {
        wx.request({
            url: url,
            data: values,
            success: function(res) {
                resolve(res);
            },
            fail: function(err) {
                console.log(err);
                resolve("err");
            }
        })
    })
}
// :: 请求
function postRequest(url, values) {
    //console.log(values);
    return new Promise((resolve, reject) => {
        wx.request({
            url: url,
            method: "POST",
            data: values,
            success: function(res) {
                resolve(res);
            },
            fail: function(err) {
                console.log(err);
                resolve("err");
            }
        })
    })
}


// @@  请求
function wxlogin() {
    //console.log(values);
    return new Promise((resolve, reject) => {
        wx.login({
            success: function(res) {
                resolve(res);
            },
            fail: function(res) {
                resolve("err");
            }
        })
    })
}

// @@  请求
function getUserInfo() {
    //console.log(values);
    return new Promise((resolve, reject) => {
        wx.getUserInfo({
            success: function(res) {
                resolve(res);
            },
            fail: function(res) {
                resolve("err");
            }
        })
    })
}

// @@  请求支付
function requestPayment(opts) {
    console.log(opts);
    return new Promise((resolve, reject) => {
        wx.requestPayment({
            'timeStamp': opts.timeStamp,
            'nonceStr': opts.nonceStr,
            'package': opts.package,
            'signType': 'MD5',
            'paySign': opts.paySign,
            success: function(res) {
                console.log(res);
                resolve(res);
            },
            fail: function(err) {
                console.log(err);
                //resolve("err");
                reject(err);
            }
        })
    })
}



// @@ 模态弹框
function confirm(opts) {
    //console.log(opts);
    return new Promise((resolve, reject) => {
        wx.showModal({
            title: opts.title,
            content: opts.content,
            confirmText: opts.confirmText,
            cancelText: opts.cancelText,
            success: function(res) {
                resolve(res);
            },
            fail: function(err) {
                resolve("err");
            }
        })
    })
}

// @@  请求
function getAddress() {
    //console.log(values);
    return new Promise((resolve, reject) => {
        wx.chooseAddress({
            success: function(res) {
                resolve(res);
            },
            fail: function(res) {
                resolve("err");
            }
        })
    })
}

// @@  请求
function wxActionSheet(itemList) {
    //console.log(itemList);
    return new Promise((resolve, reject) => {
        wx.showActionSheet({
            itemList: itemList,
            success: function(res) {
                resolve(res);
            },
            fail: function(res) {
                console.log(res.errMsg)
                resolve("err");
            }
        })
    })
}


// @@
function promise(api) {
    return new Promise((resolve, reject) => {
        try {
            api({
                success: function(res) {
                    resolve(res);
                },
                fail: function(err) {
                    console.log(err)
                    resolve("err");
                }
            })
        } catch (e) {
            console.log(e);
        }
    })
}

module.exports = {
    getStorage: getStorage,
    setStorage: setStorage,
    getStorageAll: getStorageAll,
    setStorageAll: setStorageAll,
    chooseImage: chooseImage,
    uploadFile: uploadFile,
    uploadFileAll: uploadFileAll,
    getRequest: getRequest,
    postRequest: postRequest,
    getUserInfo: getUserInfo,
    wxlogin: wxlogin,
    confirm: confirm,
    getAddress: getAddress,
    requestPayment: requestPayment,
    wxActionSheet: wxActionSheet,
    promise: promise,
    removeStorage: removeStorage
}