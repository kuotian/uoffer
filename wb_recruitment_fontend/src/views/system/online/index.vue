<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="用户名称" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入用户名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="list.slice((pageNum-1)*pageSize,pageNum*pageSize)"
      style="width: 100%;"
    >
      <el-table-column label="序号" type="index" align="center">
        <template slot-scope="scope">
          <span>{{(pageNum - 1) * pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="会话编号" align="center" prop="id"  width="200" :show-overflow-tooltip="true"/>
      <el-table-column label="登录名称" align="center" prop="username" :show-overflow-tooltip="true" >
        <template slot-scope="scope">
          {{ scope.row.username }}
           <el-tag type="danger" v-if="scope.row.id === myId">当前用户</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="IP地址" align="center" prop="ip" />
      <el-table-column label="登录地点" align="center" prop="loginAddress" :show-overflow-tooltip="true" />
      <el-table-column label="登录时间" align="center" prop="loginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleForceLogout(scope.row)"
            v-hasPermi="['system:online:forcedAccountOut']"
          >强退</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- <pagination v-show="total>0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" /> -->
  </div>
</template>

<script>
import { list, forcedAccountOut, getIsMe } from "@/api/system/online";

export default {
  name: "Online",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      // total: 0,
      // 表格数据
      list: [],
      myId: undefined,
      name: undefined,
      pageNum: 1,
      pageSize: 10,
      token: undefined,
      // 查询参数
      queryParams: {
        username: undefined
      }
    };
  },
  created() {
    this.getList();
    this.getMe();
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      list(this.queryParams).then(response => {
        this.list = response.data;
        // this.total = response.total;
        this.loading = false;
      });
    },
    getMe() {
      getIsMe().then(response => {
        this.myId = response.data.id
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 强退按钮操作 */
    handleForceLogout(row) {
      this.$confirm('是否确认强退会话ID为"' + row.id + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return forcedAccountOut(row.id);
        }).then(() => {
          this.getList();
          this.msgSuccess("强退成功");
        }).catch(function() {});
    }
  }
};
</script>

