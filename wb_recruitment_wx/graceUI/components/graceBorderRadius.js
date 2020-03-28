// graceUI/components/graceBorderRadius.js
Component({
  options: {
    multipleSlots: true // 在组件定义时的选项中启用多slot支持
  },
  properties: {
    radius: {type: Array, value: ['10px', '10px', '10px', '10px']},
    background: { type: String, value: "" }
  }
})