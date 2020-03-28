Array.prototype.indexOf = function (val) {
  for (var i = 0; i < this.length; i++) { if (this[i] == val) return i; }
  return -1;
};
Component({
  properties: {
    itemWidth: { type: String, value: "200rpx" },
    type: { type: String, value : "" },
    items: { type: Array, value : [] },
    fontSize: { type: String, value: "26rpx" },
    selectedColor : {type : String, value : "#3688FF"}
  },
  data: {
    tagsData: []
  },
  ready : function(){
    this.setData({tagsData : this.data.items});
  },
  methods: {
    graceSelectChange: function (index) {
      if (index.currentTarget) { index = index.currentTarget.dataset.index;}
      if (this.data.type == 'radio') {
        for (var i = 0; i < this.data.tagsData.length; i++) { this.data.tagsData[i].checked = false; }
        this.data.tagsData[index].checked = true;
        this.setData({ tagsData: this.data.tagsData });
        this.triggerEvent("change", this.data.tagsData[index].value);
      } else {
        if (this.data.tagsData[index].checked) {
          this.data.tagsData[index].checked = false;
        } else {
          this.data.tagsData[index].checked = true;
        }
        this.setData({ tagsData: this.data.tagsData });
        var sedRes = [];
        for (var i = 0; i < this.data.tagsData.length; i++) {
          if (this.data.tagsData[i].checked) {
            sedRes.push(this.data.tagsData[i].value);
          }
        }
        this.triggerEvent("change", sedRes);
      }
    },
	setItems : function(items){
		this.setData({tagsData : items});
	}
  }
})
