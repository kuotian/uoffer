#### 一、目录结构

├── README.md 说明文档  
├── wb_recruitment_backend 后台接口SPRINGBOOT  
├── wb_recruitment_fontend 前台VUE管理界面  
└── wb_recruitment_wx 小程序   

---

二、项目启动

- vue项目启动
  - 安装nodejs
  - 进入wb_recruitment_fontend 目录
  - 命令行界面配置淘宝镜像`npm config set registry https://registry.npm.taobao.org`
  - 命令行界面输入`npm install`
  - 项目根目录下`vue_config.js` 37行配置java后台接口地址，管理后台的接口地址统一为http://ip:port/adminApi
  - 启动`npm run dev`
  - 打包`npm run build:prod`
  - 打包后生成的目录为`recruitment_vue`
- java项目启动
  - 配置jdk、maven
  - idea 打开项目，maven下载完jar包
  - 打开`/Users/planet/wb/wb_recruitment/wb_recruitment_backend/src/main/resources/application.yml`，最底下，微信配置，配置小程序Id和密钥
  - 安装mysql5.7版本数据库，并导入数据库文档
  - 打开`/Users/planet/wb/wb_recruitment/wb_recruitment_backend/src/main/resources/application-dev.yml`,配置数据库地址
  - 打开`wb_recruitment/wb_recruitment_backend/src/main/java/com/hxxzt/recruitment`目录下的`Application.java,右键运行`
- 小程序
  - 安装小程序开发者工具
  - 导入项目`wb_recruitment_wx`
  - 打开根目录下面的`project.config.json`,26行，配置自己小程序ID
  - 打开根目录下面的`app.js`,53和54行，配置后台接口地址，小程序的接口地址统一为http://ip:port/wxApi