// graceUI/components/graceBottomDialog.js
Component({
  properties: {
    show: {
      type: Boolean,
      value: false
    },
    background: {
      type: String,
      value: 'rgba(0, 0, 0, 0.5)'
    },
    borderRadius: {
      type: String,
      value: '0rpx'
    },
    zIndex:{
      type: Number,
      value: 9
    }
  },
  data: {
    realShow: false,
    isIpx: false,
    isOut: false
  },
  methods: {
    closeDialog: function(){
      this.triggerEvent('closeDialog');
    },
    stopFun: function(){}
  },
  options: {
    multipleSlots: true
  },
  ready: function () {
    var _self = this;
    wx.getSystemInfo({
      success: function (res) {
        var model = res.model
        if (model.search('iPhone X') != -1) {
          _self.setData({ isIpx: true });
        }
      }
    });
    watch(this, {
      show: function (vn, vo) {
        if (vn) {
          this.setData({realShow : vn});
        } else {
          this.setData({ isOut: true });
          setTimeout(() => { this.setData({ realShow: false });}, 120);
          setTimeout(() => { this.setData({ isOut: false }); }, 150);
        }
      }
    });
  }
});
function defineReactive(data, key, val, fn) {
  Object.defineProperty(data, key, {
    configurable: true,
    enumerable: true,
    get: function () {
      return val
    },
    set: function (newVal) {
      if (newVal === val) return
      fn && fn(newVal)
      val = newVal
    },
  })
}

function watch(ctx, obj) {
  Object.keys(obj).forEach(key => {
    defineReactive(ctx.data, key, ctx.data[key], function (value) {
      obj[key].call(ctx, value)
    })
  })
}
