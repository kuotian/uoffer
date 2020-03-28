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
          <el-form-item label="公司名" prop="entName">
            <el-input
              v-model="formData.entName"
              placeholder="请输入公司名"
              clearable
              :style="{ width: '100%' }"
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
              :style="{ width: '100%' }"
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
          <el-form-item label="职位信息" prop="position">
            <el-input
              v-model="formData.position"
              type="textarea"
              placeholder="请输入职位信息"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :style="{ width: '100%' }"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="任职要求" prop="jobRequirements">
            <el-input
              v-model="formData.jobRequirements"
              type="textarea"
              placeholder="请输入任职要求"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :style="{ width: '100%' }"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="linkman">
            <el-input
              v-model="formData.linkman"
              placeholder="请输入联系人"
              :maxlength="4"
              clearable
              prefix-icon="el-icon-user-solid"
              :style="{ width: '100%' }"
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
              :style="{ width: '100%' }"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="公司信息" prop="entInfo">
            <el-input
              v-model="formData.entInfo"
              type="textarea"
              placeholder="请输入公司信息"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :style="{ width: '100%' }"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="公司地址" prop="entAddress">
            <el-input
              v-model="formData.entAddress"
              placeholder="请输入公司地址"
              clearable
              :style="{ width: '100%' }"
            ></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="薪资是否面议" prop="isNegotiable" required>
            <el-switch
              v-model="formData.isNegotiable"
              active-text="面议"
              inactive-text="不面议"
            ></el-switch>
          </el-form-item>
        </el-col>

        <el-col :span="24" v-if="formData.isNegotiable === false">
            <el-form-item label="薪资范围" prop="salaryUp">
              <el-input-number
                v-model="formData.salaryUp"
                placeholder="最低薪资"
                step-strictly
                controls-position="right"
              ></el-input-number>
            </el-form-item>
            <el-form-item prop="salaryDown">
              <el-input-number
                v-model="formData.salaryDown"
                placeholder="最高薪资"
                step-strictly
                controls-position="right"
              ></el-input-number>
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
import { publish } from "@/api/recruitment";
export default {
  components: {},
  props: [],
  data() {
    return {
      schoolOptions: undefined,
      formData: {
        entName: undefined,
        position: undefined,
        jobRequirements: undefined,
        linkman: undefined,
        linkmanEmail: undefined,
        entInfo: undefined,
        entAddress: undefined,
        salaryUp: undefined,
        salaryDown: undefined,
        salary: undefined,
        isNegotiable: false
      },
      rules: {
        entName: [
          {
            required: true,
            message: "请输入公司名",
            trigger: "blur"
          }
        ],
        position: [
          {
            required: true,
            message: "请输入职位信息",
            trigger: "blur"
          }
        ],
        jobRequirements: [
          {
            required: true,
            message: "请输入任职要求",
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
        entInfo: [
          {
            required: true,
            message: "请输入公司信息",
            trigger: "blur"
          }
        ],
        entAddress: [
          {
            required: true,
            message: "请输入公司地址",
            trigger: "blur"
          }
        ],
        salaryUp: [
          {
            required: true,
            message: "必填",
            trigger: "blur"
          }
        ],
        salaryDown: [
          {
            required: true,
            message: "必填",
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
            this.msgSuccess("招聘信息发布完成，可在列表查看");
            this.reset();
          } else {
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
<style></style>
