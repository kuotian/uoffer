<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <!-- <el-form-item label="系统模块" prop="module">
        <el-input
          v-model="queryParams.module"
          placeholder="请输入系统模块"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="系统模块">
        <el-select v-model="queryParams.module" placeholder="请选择系统模块" style="width: 240px;" @change="$forceUpdate()">
          <el-option
            v-for="item in moduleOptions"
            :key="item.module"
            :label="item.module"
            :value="item.module"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="操作人员" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入操作人员"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="isSuccess">
        <el-select v-model="queryParams.isSuccess" placeholder="请选择" @change="$forceUpdate()">
          <el-option
            v-for="item in successOption"
            :key="item.isSuccess"
            :label="item.lable"
            :value="item.isSuccess"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="操作时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['log:operlog:remove']"
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['log:operlog:clean']"
          type="danger"
          icon="el-icon-delete"
          size="mini"
          @click="handleClean"
        >清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['log:operlog:export']"
          :loading="downloadLoading"
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日志编号" align="center" prop="id" />
      <el-table-column label="系统模块" align="center" prop="module" width="120" />
      <el-table-column label="操作人员" align="center" prop="username" />
      <el-table-column label="操作内容" align="center" prop="operation" width="120" />
      <el-table-column label="耗时(毫秒)" align="center" prop="time">
        <template slot-scope="scope">
          <span>{{ scope.row.time }}ms</span>
        </template>
      </el-table-column>
      <el-table-column label="操作IP" align="center" prop="ip" width="120" :show-overflow-tooltip="true" />
      <el-table-column label="操作地点" align="center" prop="address" width="200" :show-overflow-tooltip="true" />
      <el-table-column label="请求状态" align="center" prop="isSuccess" :formatter="formatBoolean">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isSuccess==true" type="success">成功</el-tag>
          <el-tag v-if="scope.row.isSuccess==false" type="danger">失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作日期" align="center" prop="createTime" width="170">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['log:operlog:query']"
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
          >详细</el-button>
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

    <!-- 操作日志详细 -->
    <el-dialog title="操作日志详细" :visible.sync="open" width="900px">
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="操作模块：">{{ form.module }} / {{ form.operation }}</el-form-item>
            <el-form-item
              label="登录信息："
            >{{ form.username }} / {{ form.ip }} / {{ form.address }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="耗时(ms)">{{ form.time }}ms</el-form-item>
            <el-form-item label="日志编号">{{ form.id }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="操作方法：">{{ form.method }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="请求参数：">{{ form.params }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="返回参数：">{{ form.result }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作状态：">
              <el-tag v-if="form.isSuccess === true" type="success">成功</el-tag>
              <el-tag v-else-if="form.isSuccess === false" type="danger">失败</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作时间：">{{ parseTime(form.createTime) }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, delOperlog, cleanOperlog, exportOperlog, getModule } from '@/api/log/operlog'
export default {
  name: 'Operlog',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 是否显示弹出层
      open: false,
      // 类型数据字典
      typeOptions: [],
      // 类型数据字典
      statusOptions: [],
      moduleOptions: undefined,
      createTime: undefined,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        module: undefined,
        username: undefined,
        isSuccess: undefined
      },
      isSuccessValue: '',
      successOption: [{
        isSuccess: true,
        lable: '成功'
      }, {
        isSuccess: false,
        lable: '失败'
      }],
      downloadLoading: false
    }
  },
  created() {
    this.getList()
    getModule().then(response => {
      this.moduleOptions = response.data
    })
    // this.getDicts("sys_oper_type").then(response => {
    //   this.typeOptions = response.data;
    // });
    // this.getDicts("sys_common_status").then(response => {
    //   this.statusOptions = response.data;
    // });
  },
  methods: {
    /** 查询登录日志 */
    getList() {
      this.loading = true
      list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.list = response.data.records
        this.total = response.data.total
        this.loading = false
      }
      )
    },
    /* 布尔值格式化：cellValue为后台返回的值
     */
    formatBoolean: function(row, column, cellValue) {
      var ret = '' // 你想在页面展示的值
      if (cellValue) {
        ret = '是' // 根据自己的需求设定
      } else {
        ret = '否'
      }
      return ret
    },
    // 操作日志状态字典翻译
    // statusFormat(row, column) {
    //   return this.selectDictLabel(this.statusOptions, row.status);
    // },
    // // 操作日志类型字典翻译
    // typeFormat(row, column) {
    //   return this.selectDictLabel(this.typeOptions, row.businessType);
    // },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true
      this.form = row
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const operIds = row.id || this.ids
      this.$confirm('是否确认删除日志编号为"' + operIds + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delOperlog(operIds)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function() {})
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$confirm('是否确认清空所有操作日志数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return cleanOperlog()
      }).then(() => {
        this.getList()
        this.msgSuccess('清空成功')
      }).catch(function() {})
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.downloadLoading = true
      exportOperlog(queryParams).then(res => {
        this.downloadLoading = false
        const BLOB = res// Blob 对象表示一个不可变、原始数据的类文件对象（File 接口都是基于Blob）
        const fileReader = new FileReader() // FileReader 对象允许Web应用程序异步读取存储在用户计算机上的文件的内容
        fileReader.readAsDataURL(BLOB) // 开始读取指定的Blob中的内容。一旦完成，result属性中将包含一个data: URL格式的Base64字符串以表示所读取文件的内容
        fileReader.onload = event => {
          // 处理load事件。该事件在读取操作完成时触发
          // 新建个下载的a标签，完成后移除。
          const a = document.createElement('a')
          a.download = '操作日志' + new Date().getFullYear() + '' + (new Date().getMonth() + 1) + '' + new Date().getDate() + '' + new Date().getHours() + '' + new Date().getMinutes() + '' + new Date().getSeconds() + '.xlsx'
          a.href = event.target.result
          document.body.appendChild(a)
          a.click()
          document.body.removeChild(a)
          this.$notify({
            title: '成功',
            message: '下载成功',
            type: 'success',
            duration: 2000
          })
        }
      })
    }
  }
}
</script>
