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
          <el-form-item label="企业名称" prop="name">
            <el-input
              v-model="formData.name"
              placeholder="请输入企业名称"
              :maxlength="11"
              show-word-limit
              clearable
              prefix-icon="el-icon-mobile"
              :style="{width: '100%'}"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法人姓名" prop="leaderName">
            <el-input
              v-model="formData.leaderName"
              placeholder="请输入法人姓名"
              clearable
              :style="{width: '100%'}"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="身份证号" prop="idCard">
            <el-input
              v-model="formData.idCard"
              placeholder="请输入身份证号"
              clearable
              :style="{width: '100%'}"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="社会信用代码" prop="uscCode">
            <el-input
              v-model="formData.uscCode"
              placeholder="请输入社会信用代码"
              clearable
              :style="{width: '100%'}"
            ></el-input>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label-width="-26px" label="上传" prop="businessLicense" required>
            <el-upload ref="businessLicense" :file-list="businessLicensefileList"
              :action="businessLicenseAction" :before-upload="businessLicenseBeforeUpload"
              list-type="picture-card" accept="image/*">
              <i class="el-icon-plus"></i>
              <div slot="tip" class="el-upload__tip">只能上传不超过 2MB 的image/*文件</div>
            </el-upload>
          </el-form-item>
        </el-col>-->
        <el-col :span="24">
          <el-form-item size="large">
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
  </div>
</template>
<script>
import { addEnterprise } from "@/api/enterprise";
export default {
  components: {},
  props: [],
  data() {
    return {
      formData: {
        name: "",
        leaderName: undefined,
        idCard: "",
        uscCode: undefined,
        businessLicense: null
      },
      rules: {
        name: [
          {
            required: true,
            message: "请输入企业名称",
            trigger: "blur"
          }
        ],
        leaderName: [
          {
            required: true,
            message: "请输入法人姓名",
            trigger: "blur"
          }
        ],
        idCard: [
          {
            required: true,
            message: "请输入身份证号",
            trigger: "blur"
          },
          {
            pattern: /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/,
            message: "身份证号错误",
            trigger: "blur"
          }
        ],
        uscCode: [
          {
            required: true,
            message: "请输入社会信用代码",
            trigger: "blur"
          }
        ]
      },
      businessLicenseAction: "https://jsonplaceholder.typicode.com/posts/",
      businessLicensefileList: []
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    // 表单重置
    reset() {
      this.formData = {
        name: "",
        leaderName: undefined,
        idCard: "",
        uscCode: undefined,
        businessLicense: null
      };
    },
    submitForm() {
      this.$refs["elForm"].validate(valid => {
        if (!valid) return;
        // TODO 提交表单
        addEnterprise(this.formData).then(res => {
          if (res.status === 200) {
            this.msgSuccess(
              "企业认证信息填写完成，待审核完成后可发布招聘消息等"
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
    },
    businessLicenseBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 2;
      if (!isRightSize) {
        this.$message.error("文件大小超过 2MB");
      }
      let isAccept = new RegExp("image/*").test(file.type);
      if (!isAccept) {
        this.$message.error("应该选择image/*类型的文件");
      }
      return isRightSize && isAccept;
    }
  }
};
</script>
<style>
.el-upload__tip {
  line-height: 1.2;
}
</style>
