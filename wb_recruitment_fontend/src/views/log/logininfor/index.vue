<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="登录地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入登录地址"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入用户名称"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录时间">
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
          v-hasPermi="['log:logininfor:remove']"
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['log:logininfor:clean']"
          type="danger"
          icon="el-icon-delete"
          size="mini"
          @click="handleClean"
        >清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['log:logininfor:export']"
          type="warning"
          icon="el-icon-download"
          size="mini"
          :loading="downloadLoading"
          @click="handleExport"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="访问编号" align="center" prop="id" />
      <el-table-column label="登录人ID" align="center" prop="userId" />
      <el-table-column label="用户名" align="center" prop="username" />
      <el-table-column label="登录IP" align="center" prop="ip" :show-overflow-tooltip="true" />
      <el-table-column label="登录地点" align="center" prop="address" width="220" :show-overflow-tooltip="true" />
      <el-table-column label="登录日期" align="center" prop="loginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
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
  </div>
</template>

<script>
import { list, delLogininfor, cleanLogininfor, exportLogininfor } from '@/api/log/logininfor'

export default {
  name: 'Logininfor',
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
      // 状态数据字典
      statusOptions: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        address: undefined,
        username: undefined,
        status: undefined
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
    // this.getDicts("sys_common_status").then(response => {
    //   this.statusOptions = response.data;
    // });
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true
      list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.list = response.data.records
        this.total = response.data.total
        this.loading = false
      }
      )
    },
    // 登录状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const infoIds = row.id || this.ids
      this.$confirm('是否确认删除访问编号为"' + infoIds + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delLogininfor(infoIds)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function() {})
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$confirm('是否确认清空所有登录日志数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return cleanLogininfor()
      }).then(() => {
        this.getList()
        this.msgSuccess('清空成功')
      }).catch(function() {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.downloadLoading = true
      exportLogininfor().then(res => {
        this.downloadLoading = false
        const BLOB = res// Blob 对象表示一个不可变、原始数据的类文件对象（File 接口都是基于Blob）
        const fileReader = new FileReader() // FileReader 对象允许Web应用程序异步读取存储在用户计算机上的文件的内容
        fileReader.readAsDataURL(BLOB) // 开始读取指定的Blob中的内容。一旦完成，result属性中将包含一个data: URL格式的Base64字符串以表示所读取文件的内容
        fileReader.onload = event => {
          // 处理load事件。该事件在读取操作完成时触发
          // 新建个下载的a标签，完成后移除。
          const a = document.createElement('a')
          a.download = '登录日志' + new Date().getFullYear() + '' + (new Date().getMonth() + 1) + '' + new Date().getDate() + '' + new Date().getHours() + '' + new Date().getMinutes() + '' + new Date().getSeconds() + '.xlsx'
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

