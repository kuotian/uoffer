// graceUI/components/graceNumberBox.js
Component({
  properties: {
		value : {
			type : Number,
			value : 0 
		},
		disabled : {
			type : Boolean,
			value : false
		},
		maxNum:{
			type: Number,
			value: 20
		},
		minNum: {
			type: Number,
			value: 1
		},
		index :{
			type: String,
			value: 0
		},
    datas: {
      type: String,
      value: ""
    },
    btnSize: {
      type: String,
      value: '60rpx'
    },
    btnFontSize: {
      type: String,
      value: '36rpx'
    },
    btnColr: {
      type: String,
      value: '#666666'
    },
    inputHeight: {
      type: String,
      value: '30rpx'
    },
    inputFontSize: {
      type: String,
      value: '26rpx'
    },
    inputColor: {
      type: String,
      value: '#333333'
    },
    inputBG: {
      type: String,
      value: '#F6F7F8'
    },
    inputPadding: {
      type: String,
      value: '10rpx'
    },
    inputBorderRadius: {
      type: String,
      value: '8rpx'
    },
    width: {
      type: String,
      value: '200rpx'
    }
  },
  data: {
		
  },
  methods: {
		add : function(){
			var newVal = this.data.value + 1;
			if (newVal > this.data.maxNum) { return; }
			this.setData({ value: newVal });
			this.numberChangeDo(newVal);
		},
		reduce: function () {
			var newVal = this.data.value - 1;
			if (newVal < this.data.minNum){return ;}
			this.setData({ value: newVal});
			this.numberChangeDo(newVal);
		},
		blur : function(e){
			var newVal = e.detail.value;
			if (newVal > this.data.maxNum) { newVal = this.data.maxNum;}
			if (newVal < this.data.minNum) { newVal = this.data.minNum;}
			this.setData({ value: newVal });
			this.numberChangeDo(newVal);
		},
		numberChangeDo : function(val){
			this.triggerEvent('change', [val, this.data.index, this.data.datas]);
		}
  }
})
