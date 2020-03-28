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
              v-hasPermi="['bus:resume:del']"
            >删除</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="29" align="center" />
          <el-table-column label="序号" align="center" prop="userId" width="50">
            <template slot-scope="scope">{{scope.$index+1}}</template>
          </el-table-column>
          <el-table-column label="姓名" align="center" prop="name" width="60"/>
          <el-table-column label="年龄" align="center" prop="age" width="50"/>
          <el-table-column label="性别" align="center" prop="gender" width="50"/>
          <el-table-column label="求职意向" align="center" prop="jobIntention" :show-overflow-tooltip="true"/>
          <el-table-column label="学历" align="center" prop="education" :show-overflow-tooltip="true"/>
          <el-table-column label="毕业院校" align="center" prop="schoolOfGraduation" :show-overflow-tooltip="true"/>
          <el-table-column label="专业" align="center" prop="major" :show-overflow-tooltip="true"/>
          <el-table-column label="默认简历" align="center" :prop="isDefault" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.isDefault==true" type="success">是</el-tag>
              <el-tag v-else-if="scope.row.isDefault==false">否</el-tag>
            </template>
          </el-table-column>
           <el-table-column label="是否隐藏" align="center" :prop="isHide" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.isHide==true" type="success">是</el-tag>
              <el-tag v-else-if="scope.row.isHide==false">否</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="微信昵称" align="center" prop="wxNickname" />
          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            width="140"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
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
                v-hasPermi="['bus:resume:query']"
              >查看</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['bus:resume:edit']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['bus:resume:del']"
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


<!-- 查看评论 -->
    <el-dialog :title="title" :visible.sync="userInfo">
      
      <el-row :gutter="15">
        <el-form  :model="form" :rules="rules" size="medium" label-width="100px">
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
                :style="{width: '100%'}" readonly="true"
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
                :style="{width: '100%'}" readonly="true"
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
                :style="{width: '100%'}" readonly="true"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div slot="footer">
        <el-button @click="cancel">取消</el-button>
        <!-- <el-button type="primary" @click="submitForm">确定</el-button> -->
      </div>
    </el-dialog>

    <!-- 修改简历 -->
    <el-dialog :title="title" :visible.sync="updateOpen">
      <el-row :gutter="15">
        <el-form :model="form" :rules="rules" size="medium" label-width="100px">
              <el-col :span="8">
            <el-form-item label="姓名：" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
          <el-form-item label="年龄：" prop="age">
            <el-input-number v-model="form.age" placeholder="年龄"></el-input-number>
            </el-form-item>
           </el-col>
            <el-col :span="8">
          <el-form-item label="性别：" prop="gender">
             <el-select v-model="form.gender" placeholder="请选择下拉选择下拉选择" clearable :style="{width: '100%'}">
            <el-option v-for="(item, index) in genderOptions" :key="index" :label="item.label"
              :value="item.value" :disabled="item.disabled"></el-option>
          </el-select>
          </el-form-item>
           </el-col>
           <el-col :span="8">
          <el-form-item label="求职意向：" prop="jobIntention">
              <el-input v-model="form.jobIntention" placeholder="请输入求职意向" />
              </el-form-item>
           </el-col>
           <el-col :span="8">
            <el-form-item label="学历：" prop="education">
               <el-input v-model="form.education" placeholder="请输入学历" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
          <el-form-item label="毕业院校：" prop="schoolOfGraduation">
               <el-input v-model="form.schoolOfGraduation" placeholder="请输入毕业院校：" />
              </el-form-item>
           </el-col>
            <el-col :span="8">
          <el-form-item label="专业：" prop="major">
              <el-input v-model="form.major" placeholder="请输入专业：" />
              </el-form-item>
           </el-col>
             <el-col :span="24">
          <el-form-item label="居住地址：" prop="nowAddress">
               <el-input v-model="form.nowAddress" placeholder="请输入居住地址：：" />
          </el-form-item>
           </el-col>
           <el-col :span="24">
          <el-form-item label="户籍地址：" prop="domicileAddress">
              <el-input v-model="form.domicileAddress" placeholder="请输入户籍地址：：" />
          </el-form-item>
           </el-col>
           
          <el-col :span="12">
             <el-form-item label="出生日期：" prop="birthday">
              <el-date-picker v-model="form.birthday" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
            :style="{width: '100%'}" placeholder="请选择日期选择日期选择" clearable></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
          <el-form-item label="工作时间：" prop="workingHours">
              <el-date-picker v-model="form.workingHours" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
            :style="{width: '100%'}" placeholder="请选择日期选择日期选择" clearable></el-date-picker>
          </el-form-item>
           </el-col>
           <el-col :span="24">
            <el-form-item label="工作经历：" prop="workExperience">
              <el-input
                v-model="form.workExperience"
                type="textarea"
                placeholder=""
                :autosize="{minRows: 4, maxRows: 4}"
                :style="{width: '100%'}"
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
                :style="{width: '100%'}"
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
                :style="{width: '100%'}"
              ></el-input>
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
import { list, getById, delById, addUser, update } from "@/api/resume";
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
      isHide: undefined,
      isDefault: undefined,
      addOpen: false,
      updateOpen: false,
      userInfo: false,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      typeOptions: undefined,
      genderOptions: undefined,
      // 表单参数
      form: {
        type: undefined,
        typeId: undefined,
        content: undefined,
        wxUserId: undefined,
        wxNickname: undefined,
        createTime: "",
        gender: undefined
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
        name: [
          {
            required: true,
            message: "请输入姓名",
            trigger: "blur"
          }
        ],
        age: [
          {
            required: true,
            message: "请输入年龄",
            trigger: "blur"
          }
        ],
        gender: [
          {
            required: true,
            message: "请选择性别",
            trigger: "blur"
          }
        ],
        jobIntention: [
          {
            required: true,
            message: "请输入求职意向",
            trigger: "blur"
          }
        ],
        education: [
          {
            required: true,
            message: "请输入学历",
            trigger: "change"
          }
        ],
        schoolOfGraduation: [
          {
            required: true,
            message: "请输入毕业院校",
            trigger: "change"
          }
        ],
        major: [
          {
            required: true,
            message: "请输入专业",
            trigger: "change"
          }
        ],
        nowAddress: [
          {
            required: true,
            message: "请输入居住地址",
            trigger: "change"
          }
        ],
        domicileAddress: [
          {
            required: true,
            message: "请输入户籍地址",
            trigger: "change"
          }
        ],
        birthday: [
          {
            required: true,
            message: "请选择日期",
            trigger: "change"
          }
        ],
        workingHours: [
          {
            required: true,
            message: "请选择日期",
            trigger: "change"
          }
        ],
        workExperience: [
          {
            required: true,
            message: "请输入工作经历",
            trigger: "change"
          }
        ],
        projectExperience: [
          {
            required: true,
            message: "请输入项目经验",
            trigger: "change"
          }
        ],
        selfAppraisal: [
          {
            required: true,
            message: "请输入自我评价",
            trigger: "change"
          }
        ]
      },
      typeoptions: [
        {
          id: 1,
          lable: "招聘"
        },
        {
          id: 2,
          lable: "宣讲会"
        }
      ],
      genderOptions: [
        {
          label: "男",
          value: 1
        },
        {
          label: "女",
          value: 2
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
      this.title = row.name + "的简历，标题为：" + row.title;
      this.userInfo = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getById(id).then(response => {
        this.form = response.data;
      });
      this.title = row.name + "的简历，标题为：" + row.title;
      this.updateOpen = true;
    },
    /** 提交按钮 */
    submitForm: function() {
      if (this.form.id != undefined) {
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