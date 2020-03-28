<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询条件-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-form-item label="所属用户" prop="username">
            <el-input
              v-model="queryParams.username"
              placeholder="请输入用户名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
              <el-option
                v-for="dict in statusOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="29" align="center" />
          <el-table-column label="序号" align="center" prop="userId" width="80">
            <template slot-scope="scope">
              {{scope.$index+1}}
            </template>
          </el-table-column>
          <el-table-column label="企业名称" align="center" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="法人姓名" align="center" prop="leaderName" :show-overflow-tooltip="true" />
          <el-table-column label="法人身份证号" align="center" prop="idCard" :show-overflow-tooltip="true" width="200"/>
          <el-table-column label="统一社会信用代码" align="center" prop="uscCode" :show-overflow-tooltip="true" width="200"/>
          <el-table-column label="所属用户" align="center" prop="username" :show-overflow-tooltip="true" />
          <el-table-column label="创建时间" align="center" prop="createTime" width="160" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="审核状态" align="center" width="80">
            <template slot-scope="scope">
          <el-tag v-if="scope.row.reviewStatus==true" type="success">审核成功</el-tag>
          <el-tag v-if="scope.row.reviewStatus==false" type="danger">未审核</el-tag>
        </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="140"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleGetUserInfo(scope.row)"
                v-hasPermi="['bus:notReviewEnt:query']"
              >查看</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleReview(scope.row)"
                v-hasPermi="['bus:notReviewEnt:review']"
              >审核通过</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

<!-- 查看未审核企业详情对话框 -->
    <el-dialog title="企业详情" :visible.sync="userInfo" width="800px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="企业名称" prop="name">
              <el-input v-model="form.name" placeholder="" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="法人姓名" prop="leaderName">
              <el-input v-model="form.leaderName" placeholder="" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="信用代码" prop="uscCode">
              <el-input v-model="form.uscCode" placeholder="" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对应用户" prop="username">
              <el-input v-model="form.username" placeholder="" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核时间">
              <el-input v-model="form.reviewTime" placeholder="" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间">
              <el-input v-model="form.createTime" placeholder="" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button type="primary" @click="submitForm">确 定</el-button> -->
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { checkUsername } from "@/api/common"
import { getToken } from "@/utils/auth";
import { listNotReview, reviewById, getById } from "@/api/enterprise";

export default {
  name: "User",
  // components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      usernames: [],
       // 选中数组
      userIds: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      // deptOptions: undefined,
      // 是否显示弹出层
      addOpen: false,
      updateOpen: false,
      userInfo: false,
      // 部门名称
      // deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      statusOptions: [],
      // 性别状态字典
      sexOptions: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: undefined
      }
    };
  },
  watch: {},
  created() {
    this.getList();
  },
  methods: {
    /** 查询未审核企业列表 */
    getList() {
      this.loading = true;
      listNotReview(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.data.records;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.getList();
    },
    // 取消按钮
    cancel() {
      this.addOpen = false;
      this.updateOpen = false;
      this.userInfo=false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        username: undefined,
        nickName: undefined,
        // password: undefined,
        mobile: undefined,
        email: undefined,
        status: 0,
        description: undefined,
        roleIds: []
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.usernames = selection.map(item => item.username);
      this.userIds = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    handleGetUserInfo(row){
      getById(row.id).then(response => {
        this.form = response.data;
        this.form.roleIds = response.data.roleIds;
      });
      this.userInfo = true;
    },
    /** 审核通过按钮 */
    handleReview(row) {
      this.$alert('确定审核通过？', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(() => {
          reviewById(row.id).then(response => {
            if (response.status === 200) {
              this.msgSuccess("审核通过");
              this.getList();
            } else {
              this.msgError(response.msg);
            }
          });
        }).catch(() => {});
    }
  }
};
</script>