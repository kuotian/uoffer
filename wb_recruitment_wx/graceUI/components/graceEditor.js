var systemInfo = require('../jsTools/systemInfo.js');
Component({
  data: {
    article: { title: '', contents: [] },
    ipxHeight: 0
  },
  ready: function () {
    var system = systemInfo.info();
    this.setData({ ipxHeight: system.iphonexbottomheightrpx + 'rpx'});
  },
  methods: {
    titleInput : function(e){
      this.data.article.title = e.detail.value;
      this.setData({ article: this.data.article });
    },
    graceEditorAddItem: function (e) {
      var type = e.currentTarget.dataset.type;
      if (type == 'img') {
        wx.chooseImage({
          success: (e) => {
            var imgs = e.tempFilePaths;
            for (let i = 0; i < imgs.length; i++) {
              this.data.article.contents.push({ type: type, content: imgs[i] });
            }
            this.setData({ article : this.data.article});
            this.returnArt();
          }
        });
      } else {
        this.data.article.contents.push({ type: type, content: '' });
        this.setData({ article: this.data.article });
      }
    },
    graceEditorInput: function (e) {
      var index = e.currentTarget.dataset.index;
      var val = e.detail.value;
      if (val == '') {
        this.data.article.contents.splice(index, 1);
      } else {
        this.data.article.contents[index].content = val;
      }
      this.setData({ article: this.data.article });
      this.returnArt();
    },
    deleteItem: function (e) {
      var index = e.currentTarget.dataset.index;
      wx.showModal({
        title: "提示",
        content: "确定要删除项目吗?",
        success: (e) => {
          if (e.confirm) { 
            this.data.article.contents.splice(index, 1);
            this.setData({ article: this.data.article });
            this.returnArt();
          }
        }
      })
    },
    returnArt: function () {
      this.triggerEvent('change', this.data.article);
    },
    setError: function (index) {
      this.data.article.contents[index].error = true;
      this.data.article.contents.splice(index, 1, this.data.article.contents[index]);
      this.setData({ article: this.data.article });
    },
    setDefault: function (article) {
      this.setData({ article: article });
      this.returnArt();
    }
  }
})
