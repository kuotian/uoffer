<template>
  <div>
    <el-row :gutter="15">
      <el-form
        ref="elForm"
        :model="formData"
        :rules="rules"
        size="medium"
        label-width="100px"
        style="margin: 100px"
      >
        <el-col :span="12">
          <el-form-item label="公司名称" prop="entName">
            <el-input
              v-model="formData.entName"
              placeholder="请输入公司名称"
              clearable
              :style="{width: '100%'}"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发布学校" prop="school">
            <el-select
              v-model="formData.school"
              placeholder="发布学校，可不选择"
              filterable
              clearable
              :style="{width: '100%'}"
            >
              <el-option
                v-for="item in schoolOptions"
                :key="item.school"
                :label="item.school"
                :value="item.school"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="宣讲会内容" prop="content">
            <el-input
              v-model="formData.content"
              type="textarea"
              placeholder="请输入宣讲会内容"
              :autosize="{minRows: 4, maxRows: 4}"
              :style="{width: '100%'}"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="linkman">
            <el-input
              v-model="formData.linkman"
              placeholder="请输入联系人"
              clearable
              :style="{width: '100%'}"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人邮箱" prop="linkmanEmail">
            <el-input
              v-model="formData.linkmanEmail"
              placeholder="请输入联系人邮箱"
              clearable
              prefix-icon="el-icon-message"
              :style="{width: '100%'}"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="宣讲会日期" prop="time">
        <el-date-picker type="datetime" v-model="formData.time" format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss" :style="{width: '100%'}" placeholder="请选择日期选择" clearable>
        </el-date-picker>
      </el-form-item>
          <!-- <el-form-item label="宣讲会日期" prop="time">
            <el-date-picker
              v-model="formData.time"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{width: '100%'}"
              placeholder="请选择宣讲会日期"
              clearable
            ></el-date-picker>
          </el-form-item> -->
        </el-col>
        <el-col :span="24">
          <el-form-item label="宣讲会地址" prop="address">
            <el-input
              v-model="formData.address"
              placeholder="请输入宣讲会地址"
              clearable
              :style="{width: '100%'}"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item size="large">
            <el-button type="primary" @click="submitForm">发布</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
  </div>
</template>
<script>
import { getSchoolList } from "@/api/common";
import { publish } from "@/api/careerTalk";
export default {
  components: {},
  props: [],
  data() {
    return {
      schoolOptions: undefined,
      formData: {
        entName: undefined,
        school: undefined,
        content: undefined,
        linkman: undefined,
        linkmanEmail: undefined,
        time: null,
        address: ""
      },
      rules: {
        entName: [
          {
            required: true,
            message: "请输入公司名称",
            trigger: "blur"
          }
        ],
        school: [],
        content: [
          {
            required: true,
            message: "请输入宣讲会内容",
            trigger: "blur"
          }
        ],
        linkman: [
          {
            required: true,
            message: "请输入联系人",
            trigger: "blur"
          }
        ],
        linkmanEmail: [
          {
            required: true,
            message: "请输入联系人邮箱",
            trigger: "blur"
          },
          {
            pattern: /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
            message: "邮箱格式错误",
            trigger: "blur"
          }
        ],
        time: [
          {
            required: true,
            message: "请选择宣讲会日期",
            trigger: "change"
          }
        ],
        address: [
          {
            required: true,
            message: "请输入宣讲会地址",
            trigger: "blur"
          }
        ]
      }
    };
  },
  computed: {},
  watch: {},
  created() {
    getSchoolList().then(response => {
      this.schoolOptions = response.data;
    });
  },
  mounted() {},
  methods: {
    submitForm() {
      this.$refs["elForm"].validate(valid => {
        if (!valid) return;
        // TODO 提交表单
        publish(this.formData).then(res => {
          if (res.status === 200) {
            this.msgSuccess(
              "宣讲会信息发布完成，可在列表查看"
            );
            this.reset();
          }else{
            this.reset();
          }
        });
      });
    },
    resetForm() {
      this.$refs["elForm"].resetFields();
    }
  }
};
</script>
<style>
</style>
