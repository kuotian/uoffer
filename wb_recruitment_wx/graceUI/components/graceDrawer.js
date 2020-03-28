// graceUI/components/graceDrawer.js
Component({
  properties: {
    show: {
      type: Boolean,
      value: false
    },
    direction: {
      type: String,
      value: 'left'
    },
    width: {
      type: String,
      value: '60%'
    },
    background: {
      type: String,
      value: 'rgba(0, 0, 0, 0.5)'
    },
    padding: {
      type: String,
      value: '30rpx'
    },
	zIndex : {
		type : Number,
		value : 1
	}
  },
  data: {

  },
  methods: {
    closeDrawer: function () {
      this.triggerEvent('closeDrawer');
    },
    stopFun: function(){}
  }
})
