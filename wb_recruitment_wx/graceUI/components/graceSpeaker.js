// graceUI/components/graceSpeaker.js
Component({
  properties: {
    msgs: {
      type: Array,
      value: function () { return []; }
    },
    iconClass: {
      type: String,
      value: ""
    },
    iconColor: {
      type: String,
      value: "#3688FF"
    },
    interval: {
      type: Number,
      value: 3000
    },
    vertical: {
      type: Boolean,
      value: true
    },
    fontSize: {
      type: String,
      value: "26rpx"
    },
    fontColor: {
      type: String,
      value: "#333333"
    },
    fontWeight: {
      type: String,
      value: ""
    },
    height: {
      type: String,
      value: "60rpx"
    }
  }
})
