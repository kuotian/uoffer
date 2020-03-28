Component({
  properties: {
    items: {
      type: Array,
      value: []
    },
    show: {
      type: Boolean,
      value: false
    },
    height: {
      type: Number,
      value: 300
    },
    color: {
      type: String,
      value: "#333333"
    },
    activeColor: {
      type: String,
      value: "#3688FF"
    },
    selectIndex: {
      type: Number,
      value: 0
    },
    currentIndex : {
      type: Number,
      value: 0
    },
	padding:{
		type : String,
		value : "0 20rpx"
	}
  },
  data: {
    top: 0
  },
  ready: function () {
    this.currentIndex = this.selectIndex;
    watch(this, {
      selectIndex: function (newVal) {
        this.setData({currentIndex : newVal});
      }
    });
  },
  methods: {
    showMenu: function () {
      var _self = this;
      wx.createSelectorQuery().in(this).select('#menuMain').fields(
        { rect: true }, function (res) {
          _self.setData({top : res.top});
        }
      ).exec();
      this.triggerEvent('showMenu');
    },
    close: function () {
      this.triggerEvent('close');
    },
    select: function (e) {
      var index = Number(e.currentTarget.dataset.index);
      this.setData({ currentIndex: index });
      this.triggerEvent('select', index);
      this.close();
    }
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