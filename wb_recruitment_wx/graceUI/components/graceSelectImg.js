Component({
  properties: {
    maxFileNumber: {
      type  : Number,
      value : 9
    },
    btnName: {
      type  : String,
      value : "添加照片"
    },
    items: {
      type: Array,
      value: []
    },
    closeBtnColor: {
      type: String,
      value: "#666666"
    }
  },
  methods: {
    addImg: function () {
      var num = this.data.maxFileNumber - this.data.items.length;
      if (num < 1) { return false; }
      wx.showLoading({ title: "" });
      wx.chooseImage({
        count: num,
        sizeType: ['compressed'],
        success: (res) => {
          this.setData({ items: this.data.items.concat(res.tempFilePaths)});
          this.triggerEvent('change', this.data.items);
          wx.hideLoading();
        },
        fail: function () {
          wx.hideLoading();
        }
      });
    },
    removeImg: function (e) {
      var index = e.currentTarget.id.replace('grace-items-img-', '');
      this.data.items.splice(index, 1)
      this.setData({ items: this.data.items});
      this.triggerEvent('change', this.data.items);
    },
    showImgs: function (e) {
      var currentImg = e.currentTarget.dataset.imgurl;
      console.log(this.data.items);
      wx.previewImage({
        urls: this.data.items,
        current: currentImg
      })
    }
  }
})
