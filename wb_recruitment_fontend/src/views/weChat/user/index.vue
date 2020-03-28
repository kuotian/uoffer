<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询条件-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-form-item label="微信昵称" prop="username">
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
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['bus:wxUser:del']"
            >删除</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="29" align="center" />
          <el-table-column label="序号" align="center" prop="userId" width="80">
            <template slot-scope="scope">
              {{scope.$index+1}}
            </template>
          </el-table-column>
          <el-table-column label="微信昵称" align="center" prop="nickName" :show-overflow-tooltip="true"/>
          <el-table-column label="头像" width="50">
                <template slot-scope="scope">
                    <img :src="scope.row.avatar" width="20"/>
                </template>
            </el-table-column>
          <!-- <el-table-column label="微信openId" align="center" prop="openId" :show-overflow-tooltip="true"  width="270"/> -->
          <!-- <el-table-column label="微信号" align="center" prop="wxId" :show-overflow-tooltip="true" width="200"/> -->
          <!-- <el-table-column label="手机号" align="center" prop="uscCode" :show-overflow-tooltip="true" width="200"/> -->
          <el-table-column label="性别" align="center"  :show-overflow-tooltip="true">
          <template slot-scope="scope">
          <el-tag v-if="scope.row.gender==1" type="success">男</el-tag>
          <el-tag v-if="scope.row.gender==2">女</el-tag>
          <el-tag v-if="scope.row.gender==0" type="warning">未知</el-tag>
        </template>
          </el-table-column>
          <el-table-column label="最近登录IP" align="center" prop="lastLoginIp" :show-overflow-tooltip="true"/>
          <el-table-column label="最近登录时间" align="center" prop="lastLoginTime" width="160" :show-overflow-tooltip="true" />
          <el-table-column label="创建时间" align="center" prop="createTime" width="160" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <!-- <el-table-column label="审核状态" align="center" width="80">
            <template slot-scope="scope">
          <el-tag v-if="scope.row.reviewStatus==true" type="success">审核成功</el-tag>
          <el-tag v-if="scope.row.reviewStatus==false" type="danger">未审核</el-tag>
        </template>
          </el-table-column> -->
          <el-table-column label="是否禁言" align="center" width="100">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.isSpeak"
                :active-value=true
                :inactive-value=false
                @change="handleChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="180"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleGetInfo(scope.row)"
                v-hasPermi="['bus:wxUser:query']"
              >查看</el-button>
              <!-- <el-button
                v-if="scope.row.userId !== 44"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:user:edit']"
              >修改</el-button> -->
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['bus:wxUser:del']"
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

<!-- 查看详情对话框 -->
    <el-dialog title="微信用户详情" :visible.sync="userInfo" width="800px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="openId" prop="openId">
              <el-input v-model="form.openId" placeholder="" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="微信号" prop="wxId">
              <el-input v-model="form.wxId" placeholder="" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="mobile">
              <el-input v-model="form.mobile" placeholder="" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-tag v-if="form.gender === 1" type="success">男</el-tag>
              <el-tag v-else-if="form.gender === 2">女</el-tag>
              <el-tag v-else-if="form.gender === 0" type="warning">未知</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录时间">
              <el-input v-model="form.lastLoginTime" placeholder="" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录IP">
              <el-input v-model="form.lastLoginIp" placeholder="" disabled></el-input>
            </el-form-item>
          </el-col>
           <el-col :span="12">
            <el-form-item label="创建时间">
              <el-input v-model="form.createTime" placeholder="" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
           <el-form-item label="是否禁言">
              <el-tag v-if="form.isSpeak === true" type="danger">已禁言</el-tag>
              <el-tag v-else-if="form.isSpeak === false" type="success">未禁言</el-tag>
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
import { list, getById, delById, update, changeIsSpeak } from "@/api/wxuser";

export default {
  name: "User",
  // components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      nickNames: [],
       // 选中数组
      ids: [],
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
      list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
      this.nickNames = selection.map(item => item.nickName);
      this.ids = selection.map(item => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    handleGetInfo(row){
      getById(row.id).then(response => {
        this.form = response.data;
      });
      this.userInfo = true;
    },
     /** 删除按钮操作 */
    handleDelete(row) {
      const nickNames = row.nickName || this.nickNames;
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除用户名为"' + nickNames + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delById(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    handleChange(row) {
      let text = row.isSpeak === true ? "禁止" : "不禁止";
      this.$confirm('确认要"' + text + '""' + row.nickName + '"发布评论吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return changeIsSpeak(row.id, row.isSpeak);
        }).then(() => {
          this.msgSuccess(text + "成功");
        }).catch(function() {
          row.isSpeak = row.isSpeak === false ? true : false;
        });
    },
  }
};
</script>