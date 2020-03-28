<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询条件-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-form-item label="姓名" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入姓名"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="专业" prop="major">
            <el-input
              v-model="queryParams.major"
              placeholder="请输入专业"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="投递时间">
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
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['bus:comment:del']"
            >删除</el-button>
          </el-col> -->
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="29" align="center" />
          <el-table-column label="序号" align="center" prop="userId" width="50">
            <template slot-scope="scope">{{scope.$index+1}}</template>
          </el-table-column>
          <el-table-column label="姓名" align="center" prop="resume.name" width="60"/>
          <el-table-column label="年龄" align="center" prop="resume.age" width="50"/>
          <el-table-column label="性别" align="center" prop="resume.gender" width="50"/>
          <el-table-column label="求职意向" align="center" prop="resume.jobIntention" :show-overflow-tooltip="true"/>
          <el-table-column label="学历" align="center" prop="resume.education" :show-overflow-tooltip="true"/>
          <el-table-column label="毕业院校" align="center" prop="resume.schoolOfGraduation" :show-overflow-tooltip="true"/>
          <el-table-column label="专业" align="center" prop="resume.major" :show-overflow-tooltip="true"/>
          <el-table-column label="是否查看" align="center" :prop="isCheck" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.isCheck==true" type="success">已查看</el-tag>
              <el-tag v-else-if="scope.row.isCheck==false">未查看</el-tag>
            </template>
          </el-table-column>
           <el-table-column label="简历状态" align="center" :prop="status" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status==1" type="info">简历不符合</el-tag>
              <el-tag v-else-if="scope.row.status==2" type="danger">已发面试邀请</el-tag>
              <el-tag v-else-if="scope.row.status==null" type="warning">无</el-tag>
            </template>
          </el-table-column>
           <el-table-column label="邀请状态" align="center" :prop="interviewStatus" :show-overflow-tooltip="true" width="120">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.interviewStatus==0" type="success">已邀请未回复</el-tag>
              <el-tag v-else-if="scope.row.interviewStatus==1">接受面试邀请</el-tag>
              <el-tag v-else-if="scope.row.interviewStatus==null">未邀请</el-tag>
            </template>
          </el-table-column>
          <!-- <el-table-column label="微信昵称" align="center" prop="resume.wxNickname" /> -->
          <el-table-column
            label="投递时间 "
            align="center"
            prop="createTime"
            width="160"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="80"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleGetInfo(scope.row)"
                v-hasPermi="['bus:resumeSend:query']"
              >查看</el-button>
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


<!-- 查看评论 -->
    <el-dialog :title="title" :visible.sync="userInfo">
      
      <el-row :gutter="15">
        <el-form  :model="form" size="medium" label-width="100px">
          <el-col :span="8">
            <el-form-item label="姓名：" prop="name">
              <span>{{ form.name }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
          <el-form-item label="年龄：" prop="age">
              <span>{{ form.age }}</span>
              </el-form-item>
           </el-col>
            <el-col :span="8">
          <el-form-item label="性别：" prop="gender">
              <span>{{ form.gender }}</span>
              </el-form-item>
           </el-col>
           <el-col :span="8">
            <el-form-item label="出生日期：" prop="birthday">
              <span>{{ form.birthday }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
          <el-form-item label="工作时间：" prop="workingHours">
              <span>{{ form.workingHours }}</span>
              </el-form-item>
           </el-col>
           <el-col :span="8">
          <el-form-item label="求职意向：" prop="jobIntention">
              <span>{{ form.jobIntention }}</span>
              </el-form-item>
           </el-col>
           <el-col :span="8">
            <el-form-item label="学历：" prop="education">
              <span>{{ form.education }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
          <el-form-item label="毕业院校：" prop="schoolOfGraduation">
              <span>{{ form.schoolOfGraduation }}</span>
              </el-form-item>
           </el-col>
            <el-col :span="8">
          <el-form-item label="专业：" prop="major">
              <span>{{ form.major }}</span>
              </el-form-item>
           </el-col>
             <el-col :span="24">
          <el-form-item label="居住地址：" prop="nowAddress">
              <span>{{ form.nowAddress }}</span>
              </el-form-item>
           </el-col>
           <el-col :span="24">
          <el-form-item label="户籍地址：" prop="domicileAddress">
              <span>{{ form.domicileAddress }}</span>
              </el-form-item>
           </el-col>
           <el-col :span="24">
            <el-form-item label="工作经历：" prop="workExperience">
              <el-input
                v-model="form.workExperience"
                type="textarea"
                placeholder=""
                :autosize="{minRows: 4, maxRows: 4}"
                :style="{width: '100%'}" readonly
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
          <el-form-item label="项目经验：" prop="projectExperience">
              <el-input
                v-model="form.projectExperience"
                type="textarea"
                placeholder=""
                :autosize="{minRows: 4, maxRows: 4}"
                :style="{width: '100%'}" readonly
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
          <el-form-item label="自我评价：" prop="selfAppraisal">
              <el-input
                v-model="form.selfAppraisal"
                type="textarea"
                placeholder=""
                :autosize="{minRows: 3, maxRows: 3}"
                :style="{width: '100%'}" readonly
              ></el-input>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div slot="footer">
        <el-button type="success" @click="onOpenInvitation = true" round>发送面试邀请</el-button>
        <el-button type="danger" @click="handleInadequacy" round>不符合要求</el-button>
        <el-button @click="cancel" round>取消</el-button>
         
        <!-- <el-button type="primary" @click="submitForm">确定</el-button> -->
      </div>
    </el-dialog>

    <el-dialog :visible.sync="onOpenInvitation"  title="面试时间选择" append-to-body>
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
        <el-form-item label="面试时间" prop="interviewTime">
          <el-date-picker type="datetime" v-model="formData.interviewTime" format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss" :style="{width: '100%'}" placeholder="请选择面试时间" clearable>
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="handleInvite">发送面试邀请</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, getById,invite,inadequacy } from "@/api/resumeSend";
import { listRole } from "@/api/system/role";
import { checkUsername } from "@/api/common";
import { getToken } from "@/utils/auth";

export default {
  name: "resume",
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
      isHide: undefined,
      isDefault: undefined,
      isCheck: undefined,
      interviewStatus: undefined,
      statusVisible: undefined,
      name: undefined,
      status: undefined,
      userInfo: false,
      onOpenInvitation: false,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {
        type: undefined,
        typeId: undefined,
        content: undefined,
        wxUserId: undefined,
        wxNickname: undefined,
        createTime: "",
        gender: undefined,
        status: undefined,
        name: undefined
      },
      formData: {
        interviewTime: null,
      },
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
      rules: {
        interviewTime: [{
          required: true,
          message: '请选择面试时间',
          trigger: 'blur'
        }],
      }
    }
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
      this.onOpenInvitation = false;
      this.reset();
      this.getList();
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
        description: undefined
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
      const id = row.id;
      getById(id).then(response => {
        this.form = response.data;
      });
      this.title = row.resume.name + "的简历，标题为：" + row.resume.title;
      this.userInfo = true;
    },
     // 发送面试邀请
    handleInvite() {
      this.$refs["elForm"].validate(valid => {
      if (!valid) return;
      const status = 2
      const row = this.form
      const time = this.formData.interviewTime
      this.$confirm('确认要发送面试邀请给' + row.name + '"吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return invite(row.resumeSend.rsId, status, time)
           }).then(() => {
            this.msgSuccess("邀请成功");
            this.cancel();
            this.getList();
        })
      })
    },
      // 不合适
    handleInadequacy() {
      const status = 1
      const row = this.form
      this.$confirm('确认' + row.name + '的简历不符合要求"吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return inadequacy(row.resumeSend.rsId, status)
           }).then(() => {
            this.msgSuccess("简历状态修改为不符合要求");
            this.userInfo = false;
            this.getList();
        })
    }
  }
};
</script>