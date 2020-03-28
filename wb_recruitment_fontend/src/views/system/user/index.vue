<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <!-- <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="deptName"
            placeholder="请输入部门名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col> -->
      <!--用户数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-form-item label="用户名称" prop="username">
            <el-input
              v-model="queryParams.username"
              placeholder="请输入用户名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="mobile">
            <el-input
              v-model="queryParams.mobile"
              placeholder="请输入手机号码"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <!-- <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="用户状态"
              clearable
              size="small"
              style="width: 240px"
            > -->
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
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['system:user:add']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['system:user:remove']"
            >删除</el-button>
          </el-col>
          <!-- <el-col :span="1.5">
            <el-button
              type="info"
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
              v-hasPermi="['system:user:import']"
            >导入</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['system:user:export']"
            >导出</el-button>
          </el-col> -->
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="29" align="center" />
          <el-table-column label="序号" align="center" prop="userId" width="80">
            <template slot-scope="scope">
              {{scope.$index+1}}
            </template>
          </el-table-column>
          <el-table-column label="用户名" align="center" prop="username" :show-overflow-tooltip="true" />
          <el-table-column label="角色" align="center" prop="roleName" width="180" :show-overflow-tooltip="true" />
          <el-table-column label="手机号码" align="center" prop="mobile"/>
          <el-table-column label="邮箱" align="center" prop="email" width="150" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间" align="center" prop="createTime" width="200" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" width="60">
            <template slot-scope="scope">
              <el-switch v-if="scope.row.userId !== 44"
                v-model="scope.row.status"
                :active-value=1
                :inactive-value=0
                @change="handleStatusChange(scope.row)"
              ></el-switch>
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
                icon="el-icon-view"
                @click="handleGetUserInfo(scope.row)"
                v-hasPermi="['system:user:query']"
              >查看</el-button>
              <el-button
                v-if="scope.row.userId !== 44"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:user:edit']"
              >修改</el-button>
              <el-button
                v-if="scope.row.userId !== 44"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:user:remove']"
              >删除</el-button>
              <el-button
                v-if="scope.row.userId !== 44"
                size="mini"
                type="text"
                icon="el-icon-key"
                @click="handleResetPwd(scope.row)"
                v-hasPermi="['system:user:resetPwd']"
              >重置密码</el-button>
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

<!-- 查看用户详情对话框 -->
    <el-dialog title="用户详情" :visible.sync="userInfo" width="800px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="手机号码" prop="mobile">
              <el-input v-model="form.mobile" placeholder="" maxlength="11" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="邮箱" prop="email" disabled>
              <el-input v-model="form.email" placeholder="" maxlength="50" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用户性别">
              <el-tag v-if="form.ssex === 0" type="success">男</el-tag>
              <el-tag v-else-if="form.ssex === 1">女</el-tag>
              <el-tag v-else-if="form.ssex === 2" type="warning">未知</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否启用">
              <el-radio-group v-model="form.status" disabled>
                <el-radio :label=1>启用</el-radio>
                <el-radio :label=0>禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="角色" prop="roleName">
              <el-input v-model="form.roleName" placeholder="无角色" maxlength="11" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建时间">
              <el-input v-model="form.createTime" placeholder="" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="修改时间">
              <el-input v-model="form.modifyTime" placeholder="" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录时间">
              <el-input v-model="form.lastLoginTime" placeholder="" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录Ip">
              <el-input v-model="form.lastLoginIp" placeholder="" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.description" type="textarea" placeholder="" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button type="primary" @click="submitForm">确 定</el-button> -->
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加参数配置对话框 -->
    <el-dialog title="添加用户" :visible.sync="addOpen" width="600px">
      <el-form ref="form" :model="form" :rules="addRules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入用户昵称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="mobile">
              <el-input v-model="form.mobile" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="用户密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入用户密码" type="password" />
            </el-form-item>
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="用户性别">
              <el-select v-model="form.ssex" placeholder="请选择">
                <el-option
                  v-for="item in ssex"
                  :key="item.id"
                  :label="item.lable"
                  :value=item.id
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item> -->
            <el-form-item label="是否启用">
              <el-radio-group v-model="form.status">
                <el-radio :label=1>启用</el-radio>
                <el-radio :label=0>禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色">
              <el-select v-model="form.roleIds" multiple placeholder="请选择" @change="$forceUpdate()">
                <el-option
                  v-for="item in roleOptions"
                  :key="item.roleId"
                  :label="item.roleName"
                  :value="item.roleId" 
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.description" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 修改参数配置对话框 -->
    <el-dialog title="修改用户" :visible.sync="updateOpen" width="600px">
      <el-form ref="form" :model="form" :rules="updateRules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名称" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入用户昵称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="mobile">
              <el-input v-model="form.mobile" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="用户密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入用户密码" type="password" />
            </el-form-item>
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="用户性别">
              <el-select v-model="form.ssex" placeholder="请选择">
                <el-option
                  v-for="item in ssex"
                  :key="item.id"
                  :label="item.lable"
                  :value=item.id
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item> -->
            <el-form-item label="是否启用">
              <el-radio-group v-model="form.status">
                <el-radio :label=1>启用</el-radio>
                <el-radio :label=0>禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色">
              <el-select v-model="form.roleIds" multiple placeholder="请选择" @change="$forceUpdate()">
                <el-option
                  v-for="item in roleOptions"
                  :key="item.roleId"
                  :label="item.roleName"
                  :value="item.roleId" 
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.description" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <!-- <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px">
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog> -->
  </div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, exportUser, resetUserPwd, changeUserStatus, importTemplate } from "@/api/system/user";
import { listRole } from "@/api/system/role"
import { checkUsername } from "@/api/common"
import { getToken } from "@/utils/auth";
// import { treeselect } from "@/api/system/dept";
// import Treeselect from "@riophae/vue-treeselect";
// import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "User",
  // components: { Treeselect },
  data() {
    var valideteUsername = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('用户名称不能为空'))
      }
      checkUsername(value).then(response => {
        if (response.data){
          callback(new Error('用户名已经存在，不能重复添加'))
        } else {
          callback()
        }
        })
      }
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
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        addOpen: false,
        updateOpen: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        // headers: { Authorization: "Bearer " + getToken() },
        headers: { Authorization: getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      ssex: [{
        id: 0,
        lable: '男'
      },{
        id: 1,
        lable: '女'
      },{
        id: 2,
        lable: '未知'
      }],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: undefined,
        mobile: undefined
      },
      // 表单校验
      addRules: {
        username: [
          {required:true, validator: valideteUsername, trigger: "blur"}
        ],
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        deptId: [
          { required: true, message: "归属部门不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" }
        ],
        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        mobile: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      },
      updateRules: {
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        deptId: [
          { required: true, message: "归属部门不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" }
        ],
        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        mobile: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    };
    
    var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm.username !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    // this.getTreeselect();
    // this.getDicts("sys_normal_disable").then(response => {
    //   this.statusOptions = response.data;
    // });
    // this.getDicts("sys_user_sex").then(response => {
    //   this.sexOptions = response.data;
    // });
    // this.getConfigKey("sys.user.initPassword").then(response => {
    //   this.initPassword = response.data;
    // });
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.data.records;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 查询角色下拉树结构 */
    // getTreeselect() {
    //   listRole().then(response => {
    //     this.deptOptions = response.data;
    //   });
    // },
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
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === 1 ? "启用" : "停用";
      this.$confirm('确认要"' + text + '""' + row.username + '"用户吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return changeUserStatus(row.userId, row.status);
        }).then(() => {
          this.msgSuccess(text + "成功");
        }).catch(function() {
          row.status = row.status === 0 ? 1 : 0;
        });
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
        ssex: 2,
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
      const userId = row.userId;
      getUser(userId).then(response => {
        this.form = response.data;
        this.form.roleIds = response.data.roleIds;
      });
      this.userInfo = true;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      listRole().then(response => {
          this.roleOptions = response.data.records;
      })
      // this.getTreeselect();
      // getUser().then(response => {
      
        this.addOpen = true;
        // this.form.password = this.initPassword;
      // });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      // this.getTreeselect();
      listRole().then(response => {
          this.roleOptions = response.data.records;
      })
      const userId = row.userId;
      getUser(userId).then(response => {
        this.form = response.data;
        this.form.roleIds = response.data.roleIds;
      });
      this.title = "修改用户";
      this.updateOpen = true;
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.username + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(({ value }) => {
          resetUserPwd(row.userId, value,row.username).then(response => {
            if (response.status === 200) {
              this.msgSuccess("修改成功，新密码是：" + value);
            } else {
              this.msgError(response.msg);
            }
          });
        }).catch(() => {});
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
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
      const usernames = row.username || this.usernames;
      const userIds = row.userId || this.userIds;
      this.$confirm('是否确认删除用户名为"' + usernames + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delUser(userIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
  //   handleExport() {
  //     const queryParams = this.queryParams;
  //     this.$confirm('是否确认导出所有用户数据项?', "警告", {
  //         confirmButtonText: "确定",
  //         cancelButtonText: "取消",
  //         type: "warning"
  //       }).then(function() {
  //         return exportUser(queryParams);
  //       }).then(response => {
  //         this.download(response.msg);
  //       }).catch(function() {});
  //   },
  //   /** 导入按钮操作 */
  //   handleImport() {
  //     this.upload.title = "用户导入";
  //     this.upload.open = true;
  //   },
  //   /** 下载模板操作 */
  //   importTemplate() {
  //     importTemplate().then(response => {
  //       this.download(response.msg);
  //     });
  //   },
  //   // 文件上传中处理
  //   handleFileUploadProgress(event, file, fileList) {
  //     this.upload.isUploading = true;
  //   },
  //   // 文件上传成功处理
  //   handleFileSuccess(response, file, fileList) {
  //     this.upload.open = false;
  //     this.upload.isUploading = false;
  //     this.$refs.upload.clearFiles();
  //     this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
  //     this.getList();
  //   },
  //   // 提交上传文件
  //   submitFileForm() {
  //     this.$refs.upload.submit();
  //   }
  }
};
</script>