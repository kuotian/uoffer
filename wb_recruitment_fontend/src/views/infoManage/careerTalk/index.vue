<template>
  <div class="app-container">
    <el-row :gutter="20">

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
          <!-- <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['system:user:add']"
            >新增</el-button>
          </el-col> -->
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['bus:careerTalk:del']"
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
            <el-table-column label="所属用户" align="center" prop="username" :show-overflow-tooltip="true" />
            <el-table-column label="公司名称" align="center" prop="entName" :show-overflow-tooltip="true" width="240"/>
            <el-table-column label="宣讲会时间" align="center" prop="time" :show-overflow-tooltip="true"/>
            <el-table-column label="联系人" align="center" prop="linkman"/>
            <el-table-column label="联系人邮箱" align="center" prop="linkmanEmail" width="120" :show-overflow-tooltip="true"/>
            <el-table-column label="创建时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true">
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
                icon="el-icon-view"
                @click="handleGetUserInfo(scope.row)"
                v-hasPermi="['bus:careerTalk:query']"
              >查看</el-button>
              <el-button

                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['bus:careerTalk:edit']"
              >修改</el-button>
              <el-button

                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['bus:careerTalk:del']"
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

<!-- 查看用户详情对话框 -->
   <el-dialog title="查看宣讲会" :visible.sync="userInfo">
      <el-row :gutter="15">
        <el-form :model="form" :rules="rules" size="medium" label-width="100px">
          <el-col :span="12">
            <el-form-item label="公司名称" prop="entName">
              <el-input v-model="form.entName" placeholder="请输入公司名称" clearable :style="{width: '100%'}" disabled>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-button type="primary" round @click="commentVisible = true">查看评论</el-button>
            <el-button type="info" round @click="clickVisible = true">查看点赞</el-button>
            <el-button type="danger" round @click="focusVisible = true">查看关注</el-button>
          </el-col>
          <el-col :span="24">
            <el-form-item label="宣讲会地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入宣讲会地址" clearable :style="{width: '100%'}" disabled>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="宣讲会内容" prop="content">
              <el-input v-model="form.content" type="textarea" placeholder="请输入宣讲会内容"
                :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" prop="linkman">
              <el-input v-model="form.linkman" placeholder="请输入联系人" :maxlength="4" clearable
                prefix-icon='el-icon-user-solid' :style="{width: '100%'}" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人邮箱" prop="linkmanEmail">
              <el-input v-model="form.linkmanEmail" placeholder="请输入联系人邮箱"  clearable
                 prefix-icon="el-icon-message" :style="{width: '100%'}" disabled></el-input>
            </el-form-item>
          </el-col>
           <el-col :span="12">
            <el-form-item label="宣讲会时间" prop="time">
              <el-date-picker v-model="form.time" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
                :style="{width: '100%'}" placeholder="请择选宣讲会时间" clearable disabled></el-date-picker>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div slot="footer">
        <el-button @click="cancel">取消</el-button>
        <!-- <el-button type="primary" @click="submitForm">确定</el-button> -->
      </div>
    </el-dialog>

    <!-- 评论列表 -->
    <el-dialog
      width="180"
      title="评论列表"
      :visible.sync="commentVisible"
      append-to-body>
      <el-table
          :data="commentList"
        >
          <el-table-column
            label="ID"
            type="index"
            align="center"
            width="80"
          />
          <el-table-column
            prop="wxNickname"
            label="微信昵称"
            width="180"
          />
          <el-table-column
            prop="content"
            label="评论内容" :show-overflow-tooltip="true"
          />
        </el-table>
    </el-dialog>

     <!-- 点赞列表 -->
    <el-dialog
      width="30%"
      title="点赞列表"
      :visible.sync="clickVisible"
      append-to-body>
      <el-table
          :data="clickList"
        >
          <el-table-column
            label="ID"
            type="index"
            align="center"
          />
          <el-table-column
            prop="wxNickname"
            label="微信昵称"
          />
        </el-table>
    </el-dialog>

     <!-- 关注列表 -->
    <el-dialog
      width="30%"
      title="关注列表"
      :visible.sync="focusVisible"
      append-to-body>
      <el-table
          :data="focusList"
        >
          <el-table-column
            label="ID"
            type="index"
            align="center"
          />
          <el-table-column
            prop="wxNickname"
            label="微信昵称"
          />
        </el-table>
    </el-dialog>

    <!-- 修改参数配置对话框 -->
   <el-dialog title="修改宣讲会" :visible.sync="updateOpen">
      <el-row :gutter="15">
        <el-form :model="form" :rules="rules" size="medium" label-width="100px">
          <el-col :span="12">
            <el-form-item label="公司名称" prop="entName">
              <el-input v-model="form.entName" placeholder="请输入公司名称" clearable :style="{width: '100%'}">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宣讲会时间" prop="time">
              <el-date-picker v-model="form.time" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
                :style="{width: '100%'}" placeholder="请择选宣讲会时间" clearable></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="宣讲会地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入宣讲会地址" clearable :style="{width: '100%'}">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="宣讲会内容" prop="content">
              <el-input v-model="form.content" type="textarea" placeholder="请输入宣讲会内容"
                :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" prop="linkman">
              <el-input v-model="form.linkman" placeholder="请输入联系人" :maxlength="4" clearable
                prefix-icon='el-icon-user-solid' :style="{width: '100%'}"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人邮箱" prop="linkmanEmail">
              <el-input v-model="form.linkmanEmail" placeholder="请输入联系人邮箱" clearable
                 prefix-icon="el-icon-message" :style="{width: '100%'}"></el-input>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div slot="footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import { list, getById, delById, update, getCareerTalkComment, getCareerTalkClick, getCareerTalkFocus} from "@/api/careerTalk";
import { listRole } from "@/api/system/role"
import { checkUsername } from "@/api/common"
import { getToken } from "@/utils/auth"

export default {
  name: "User",

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
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
       // 评论弹窗数据
      commentList: null,
      // 关注弹窗数据
      focusList: null,
      // 点赞弹窗数据
      clickList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      // deptOptions: undefined,
      // 是否显示弹出层
      addOpen: false,
      updateOpen: false,
      userInfo: false,
      commentVisible: false,
      clickVisible: false,
      focusVisible: false,
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
      form: {
        entName: undefined,
        time: undefined,
        address: undefined,
        content: undefined,
        linkman: undefined,
        linkmanEmail: undefined,
      },
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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: undefined,
        mobile: undefined
      },
      rules: {
        entName: [{
          required: true,
          message: '请输入公司名称',
          trigger: 'blur'
        }],
        time: [{
          required: true,
          message: '请择选宣讲会时间',
          trigger: 'change'
        }],
        address: [{
          required: true,
          message: '请输入宣讲会地址',
          trigger: 'blur'
        }],
        content: [{
          required: true,
          message: '请输入宣讲会内容',
          trigger: 'blur'
        }],
        linkman: [{
          required: true,
          message: '请输入联系人',
          trigger: 'blur'
        }],
        linkmanEmail: [{
          required: true,
          message: '请输入联系人电话',
          trigger: 'blur'
        },{
          pattern: /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
          message: '邮箱格式错误',
          trigger: 'blur'
        }],
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

  },
  methods: {
    //时间格式化
    dateFormat:function(row,column){
        var t=new Date(row.time);//row 表示一行数据, time 表示要格式化的字段名称
        // return t.getFullYear()+"-"+(t.getMonth()+1)+"-"+t.getDate()+" "+t.getHours()+":"+t.getMinutes()+":"+t.getSeconds()+"."+t.getMilliseconds();
        if(row.time===undefined){
          return null;
        }else{
          return t.getFullYear()+"-"+(t.getMonth()+1)+"-"+t.getDate();
        }
    },
    /** 查询用户列表 */
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
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    handleGetUserInfo(row){
      const id = row.id;
      getById(id).then(response => {
        this.form = response.data;
        this.form.roleIds = response.data.roleIds;
      });

      getCareerTalkComment(id).then(res => {
        this.commentList = res.data
      })
       getCareerTalkClick(id).then(res => {
        this.clickList = res.data
      })
       getCareerTalkFocus(id).then(res => {
        this.focusList = res.data
      })
      this.userInfo = true;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      listRole().then(response => {
          this.roleOptions = response.data.records;
      })  
        this.addOpen = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // this.reset();
      const id = row.id;
      getById(id).then(response => {
        this.form = response.data;
      });
      this.title = "修改用户";
      this.updateOpen = true;
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.token + '"的新密码', "提示", {
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
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除所选数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delById(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    }
  }
};
</script>