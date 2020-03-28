// graceUI/components/graceSelectImgAndUpload.js
Component({
  properties: {
    maxFileNumber: {
      type: Number,
      value: 9
    },
    btnName: {
      type: String,
      value: "添加照片"
    },
    items: {
      type: Array,
      value: []
    },
    closeBtnColor: {
      type: String,
      value: "#666666"
    },
    uploadServerUrl: {
      type: String,
      value: ''
    },
    progressSize: {
      type: Number,
      value: 1
    },
    progressColor: {
      type: String,
      value: '#27BD81'
    },
    progressBGColor: {
      type: String,
      value: '#F8F8F8'
    },
    fileName: { type: String, value: 'img'},
    formData: { type: Object, value: {}}
  },
  data: {
    imgLists: [],
    updatting: false
  },
  methods: {
    addImg: function () {
      var num = this.data.maxFileNumber - this.data.imgLists.length;
      if (num < 1) { return false; }
      wx.showLoading({ title: "" });
      wx.chooseImage({
        count: num,
        sizeType: ['compressed'],
        success: (res) => {
          for (let i = 0; i < res.tempFilePaths.length; i++) {
            this.data.imgLists.push({ url: res.tempFilePaths[i], progress: 0, error: false });
          }
          this.setData({ imgLists: this.data.imgLists});
          this.triggerEvent('change', this.data.imgLists);
          wx.hideLoading();
        },
        complete: function () {
          wx.hideLoading();
        },
        fail: function () {
          wx.hideLoading();
        }
      });
    },
    removeImg: function (e) {
      var index = e.currentTarget.id.replace('grace-items-img-', '');
      this.data.imgLists.splice(index, 1);
      this.setData({ imgLists: this.data.imgLists });
      this.triggerEvent('change', this.data.imgLists);
    },
    showImgs: function (e) {
      var currentImg = e.currentTarget.dataset.imgurl;
      var imgs = [];
      for (let i = 0; i < this.data.imgLists.length; i++) {
        imgs.push(this.data.imgLists[i].url);
      }
      wx.previewImage({
        urls: imgs,
        current: currentImg
      })
    },
    upload: function (index) {
      if (this.data.updatting) { return; }
      this.data.updatting = true;
      if (!index) { index = 0; }
      wx.showLoading({ title: "图片上传中" });
      this.uploadBase(index);
    },
    retry: function (e) {
      var index = e.currentTarget.dataset.index;
      this.upload(index);
    },
    uploadBase: function (index) {
      // 全部上传完成
      if (index > (this.data.imgLists.length - 1)) {
        wx.hideLoading();
        this.setData({ updatting: false });
        this.triggerEvent('uploaded', this.data.imgLists);
        return;
      }
      // 验证后端
      if (this.data.uploadServerUrl == '') {
        wx.showToast({ title: "请设置上传服务器地址", icon: "none" });
        return;
      }
      // 检查是否是默认值
      if (this.data.imgLists[index].progress >= 1) {
        this.uploadBase(index + 1);
        return;
      }
      this.data.imgLists[index].error = false;
      this.setData({ imgLists: this.data.imgLists });
      // 创建上传对象
      const task = wx.uploadFile({
        url: this.data.uploadServerUrl,
        filePath: this.data.imgLists[index].url,
        name: this.data.fileName,
        formData: this.data.formData,
        success: (uploadRes) => {
          uploadRes = JSON.parse(uploadRes.data);
          if (uploadRes.status != 'ok') {
            wx.showToast({ title: "上传失败 : " + uploadRes.data, icon: "none" });
            this.error(index);
          } else {
            //上传图片成功
            this.data.imgLists[index].url      = uploadRes.data;
            this.data.imgLists[index].progress = 100;
            this.data.imgLists[index].result   = uploadRes;
            this.setData({imgLists : this.data.imgLists});
            this.uploadBase(index + 1);
          }
        },
        fail: (e) => {
          wx.showToast({ title: "上传失败，请点击图片重试", icon: "none" });
          this.error(index);

        }
      });
      task.onProgressUpdate((res) => {
        if (res.progress > 0) {
          this.data.imgLists[index].progress = res.progress;
          this.setData({ imgLists: this.data.imgLists });
        }
      });
    },
    // 上传错误
    error: function (index) {
      setTimeout(() => {
        this.data.updatting = false;
        this.data.imgLists[index].progress = 0;
        this.data.imgLists[index].error = true;
        this.triggerEvent('uploaderror');
        this.setData({ imgLists: this.data.imgLists, updatting : false });
      }, 500);
    },
    // 设置默认值
    setItems: function (items) {
      for (let i = 0; i < items.length; i++) {
        this.data.imgLists.push({ url: items[i], progress: 100, error: false });
        this.setData({ imgLists: this.data.imgLists });
      }
      this.triggerEvent('change', this.data.imgLists);
    },
    clearAllImgs : function () {
      this.setData({imgLists : []});
    },
  }
})
