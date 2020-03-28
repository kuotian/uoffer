// graceUI/components/graceTags.js
Component({
  properties: {
    fontSize: { type: Number, value: 26 },
    color: { type: String, value: '#333333' },
    bgColor: { type: String, value: '#F6F7F8' },
    borderRadius: { type: String, value: '6rpx' },
    items: { type: Array, value: [] },
    margin: { type: Number, value: 20 }
  },
  data: {

  },
  methods: {
    removeTag: function (e) {
      this.triggerEvent('removeTag', e.currentTarget.dataset.index);
    }
  }
})
