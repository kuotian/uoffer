<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询条件-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-form-item label="类型">
            <el-select
              v-model="queryParams.type"
              placeholder="请选择类型"
              style="width: 240px"
              @change="$forceUpdate()"
            >
              <el-option
                v-for="item in typeOptions"
                :key="item.id"
                :label="item.lable"
                :value="item.id"
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
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['bus:focus:del']"
            >删除</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="29" align="center" />
          <el-table-column label="序号" align="center" prop="userId" width="80">
            <template slot-scope="scope">{{scope.$index+1}}</template>
          </el-table-column>
          <el-table-column label="类型" align="center" prop="type" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.type==1" type="success">招聘</el-tag>
              <el-tag v-if="scope.row.type==2">宣讲会</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            label="类型Id"
            align="center"
            prop="typeId"
            width="90"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="微信用户昵称" align="center" prop="wxNickname" />
          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            width="200"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="250"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['bus:focus:del']"
              >删除</el-button>
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
  </div>
</template>

<script>
import { list, getById, delById, addUser, update } from "@/api/focus";
import { listRole } from "@/api/system/role";
import { checkUsername } from "@/api/common";
import { getToken } from "@/utils/auth";

export default {
  name: "click",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
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
      addOpen: false,
      updateOpen: false,
      userInfo: false,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      typeOptions: undefined,
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
        username: undefined,
        mobile: undefined
      },
      typeOptions: [
        {
          id: 1,
          lable: "招聘"
        },
        {
          id: 2,
          lable: "宣讲会"
        }
      ]
    };
  },
  watch: {},
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      list(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
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
      this.userInfo = false;
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
      this.ids = selection.map(item => item.id);
      this.userIds = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    handleGetInfo(row) {
      const userId = row.userId;
      getById(userId).then(response => {
        this.form = response.data;
        this.form.roleIds = response.data.roleIds;
      });
      this.userInfo = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userId = row.userId;
      getByid(userId).then(response => {
        this.form = response.data;
        this.form.roleIds = response.data.roleIds;
      });
      this.title = "修改用户";
      this.updateOpen = true;
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            update(this.form).then(response => {
              if (response.status === 200) {
                this.msgSuccess("修改成功");
                this.updateOpen = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addUser(this.form).then(response => {
              if (response.status === 200) {
                this.msgSuccess(response.data);
                this.addOpen = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm("确认删除所选数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delById(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    }
  }
};
</script>