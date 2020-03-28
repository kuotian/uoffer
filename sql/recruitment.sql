/*
 Navicat Premium Data Transfer

 Source Server         : 121.199.49.84-root
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 121.199.49.84:3306
 Source Schema         : recruitment

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 28/03/2020 00:29:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for career_talk
-- ----------------------------
DROP TABLE IF EXISTS `career_talk`;
CREATE TABLE `career_talk` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增Id',
  `ent_name` varchar(30) DEFAULT NULL COMMENT '公司名称',
  `address` varchar(50) DEFAULT NULL COMMENT '宣讲会地址',
  `time` datetime DEFAULT NULL COMMENT '宣讲会时间',
  `content` text COMMENT '宣讲会内容',
  `linkman` varchar(10) DEFAULT NULL COMMENT '联系人',
  `linkman_email` varchar(100) DEFAULT NULL COMMENT '联系人邮箱',
  `school` varchar(20) DEFAULT NULL COMMENT '发布学校，可不选择',
  `user_id` int(11) DEFAULT NULL COMMENT '发布人id',
  `username` varchar(20) DEFAULT NULL COMMENT '发布人用户名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='宣讲会表';

-- ----------------------------
-- Records of career_talk
-- ----------------------------
BEGIN;
INSERT INTO `career_talk` VALUES (8, '北京测试公司', '北京大学东校区', '2020-03-28 14:30:00', '测试宣讲会', '张三', '123@qq.com', NULL, 98, 'ceshi', '2020-03-27 23:26:48', 0);
COMMIT;

-- ----------------------------
-- Table structure for click
-- ----------------------------
DROP TABLE IF EXISTS `click`;
CREATE TABLE `click` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `type` int(11) NOT NULL COMMENT '招聘信息或者宣讲会信息，招聘信息：1，宣讲会信息：2',
  `type_id` int(11) DEFAULT NULL COMMENT '对应宣讲会或者招聘信息id,type为1此字段为招聘信息id，type为2此字段为宣讲会id',
  `wx_user_id` int(11) NOT NULL COMMENT '点赞人id',
  `wx_nickname` varchar(30) DEFAULT NULL COMMENT '点赞人微信昵称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='点赞表';

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `type` int(11) DEFAULT NULL COMMENT '招聘信息或者宣讲会信息，招聘信息：1，宣讲会信息：2',
  `type_id` int(11) DEFAULT NULL COMMENT '对应宣讲会或者招聘信息id,type为1此字段为招聘信息id，type为2此字段为宣讲会id',
  `wx_user_id` int(11) NOT NULL COMMENT '评论人id',
  `wx_nickname` varchar(30) DEFAULT NULL COMMENT '微信昵称',
  `content` text COMMENT '评论内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='评论表';

-- ----------------------------
-- Table structure for enterprise
-- ----------------------------
DROP TABLE IF EXISTS `enterprise`;
CREATE TABLE `enterprise` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '企业信息表',
  `name` varchar(20) DEFAULT NULL COMMENT '企业名称',
  `leader_name` varchar(10) DEFAULT NULL COMMENT '法人姓名',
  `id_card` varchar(18) DEFAULT NULL COMMENT '法人身份证号',
  `usc_code` varchar(18) DEFAULT NULL COMMENT '统一社会信用代码',
  `business_license` varchar(100) DEFAULT NULL COMMENT '营业执照附件',
  `review_status` tinyint(1) DEFAULT '0' COMMENT '审核状态,true：审核成功，false：审核失败',
  `review_people` varchar(10) DEFAULT NULL COMMENT '审核人',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `user_id` int(11) DEFAULT NULL COMMENT '对应sys_user表中的Id',
  `username` varchar(20) DEFAULT NULL COMMENT '对应username',
  `is_publish` tinyint(1) DEFAULT '0' COMMENT '是否可以发布招聘信息、宣讲会信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='企业信息表';

-- ----------------------------
-- Records of enterprise
-- ----------------------------
BEGIN;
INSERT INTO `enterprise` VALUES (7, '北京测试公司', '测试法人', '101212199912120121', '232132132232123222', NULL, 1, 'admin', '2020-03-27 22:47:22', 98, 'ceshi', 1, '2020-03-27 22:47:11', 0);
COMMIT;

-- ----------------------------
-- Table structure for focus
-- ----------------------------
DROP TABLE IF EXISTS `focus`;
CREATE TABLE `focus` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `type` int(11) NOT NULL COMMENT '招聘信息或者宣讲会信息，招聘信息：1，宣讲会信息：2',
  `type_id` int(11) NOT NULL COMMENT '对应宣讲会或者招聘信息id,type为1此字段为招聘信息id，type为2此字段为宣讲会id',
  `wx_user_id` int(11) NOT NULL COMMENT '关注人id',
  `wx_nickname` varchar(30) NOT NULL COMMENT '微信昵称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='关注表';

-- ----------------------------
-- Table structure for recruitment_info
-- ----------------------------
DROP TABLE IF EXISTS `recruitment_info`;
CREATE TABLE `recruitment_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增Id',
  `ent_name` varchar(20) DEFAULT NULL COMMENT '公司名',
  `position` text COMMENT '职位信息',
  `job_requirements` text COMMENT '任职要求',
  `linkman` varchar(20) DEFAULT NULL COMMENT '联系人',
  `linkman_email` varchar(100) DEFAULT NULL COMMENT '联系人邮箱',
  `ent_info` text COMMENT '公司信息',
  `ent_address` varchar(50) DEFAULT NULL COMMENT '公司地址',
  `is_negotiable` tinyint(1) DEFAULT '0' COMMENT '薪资是否面议',
  `salary` varchar(50) DEFAULT NULL COMMENT '薪资范围',
  `school` varchar(20) DEFAULT NULL COMMENT '发布学校，可不选择',
  `user_id` int(11) DEFAULT NULL COMMENT '发布人Id',
  `username` varchar(20) DEFAULT NULL COMMENT '发布人账号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='招聘信息';

-- ----------------------------
-- Records of recruitment_info
-- ----------------------------
BEGIN;
INSERT INTO `recruitment_info` VALUES (20, '北京测试公司', 'JAVA开发', '（1）两年及以上互联网行业开发工作经验。\n（2）熟练掌握java基础知识，包括面向对象特征，常见API，如文件流、多线程、反射注解等。\n（3）了解并掌握Linux开发环境，并熟练使用常用命令，熟悉主流数据库oracle和mysql。\n（4）熟练使用Redis,memcached相关缓存技术。\n（5）有数据库设计经验，精通sql语言，并熟悉Oracle、Mysql数据库系统。\n（6）学习能力强，善于思考总结，工作认真、责任心强，有较强的沟通和协作能力，工作效率高。', '张三', 'zhangsan@qq.com', '公司成立于2003年，目前人员规模达1200人左右。公司是一家电子商务及信息化综合服务提供商，主要为通信运营商提供电子商务综合服务，同时为其他行业用户提供技术+运营的一体化解决方案。通过多年的经营，公司业务已经覆盖三大通信运营商的16个省、直辖市区域市场。主要产品或服务分为技术开发类业务、技术和运营支撑类业务、内容销售类业务服务三类。', '北京中关村', 1, NULL, NULL, 98, 'ceshi', '2020-03-27 22:49:51', 0);
INSERT INTO `recruitment_info` VALUES (21, '北京测试公司', 'UI设计', '1、熟悉Web页面UI设计、移动端设计规则、基础平面设计等；\n2、熟练掌握Photoshop、CorelDraw、Illustrator、Indesign等专业软件操作；\n3、负责与项目经理、售前人员沟通完成基本产品原型设计；\n4、负责公司软件产品Web端,移动端的界面设计、编辑、美化等工作；\n5、对公司在进行中的项目产品及相关宣传资料进行设计；\n6、协助其他部门处理基础平面设计和领导交办的其他事项。', '张三', '123@qq.com', '公司成立于2005年2月，注册资金2000万元，是一家以软硬件研发、网络信息系统集成为主营业务的科技型技术服务企业。公司为中华人民共和国工业和信息化部认证的“系统集成二级企业”；甘肃省科技厅认证的“高新技术企业”；同时也是甘肃省工信委认证的“软件企业”。此外公司还具有建筑智能化三级资质，安全防范一级资质，并连续多年被工商管理部门认定为“守合同、重信用”企业。\n多年来公司不断扩大销售领域，目前主要业务领域为水利行业信息化建设，气象国土资源信息化建设，以及政府、金融、教育领域的信息化建设，特别是灾害防御领域，公司拥有多项软件著作权以及软件产品。\n“承载个人理想，追求企业进步，促进社会发展”是甘肃宏天亚达电子技术有限公司的经营理念。多年来在全体员工的努力下，公司通过不断改进和创新满足快速变化的客户需求，始终如一的为客户提供满意的服务，通过质量、服务提升客户价值，同时创造公司价值，实现员工的个人价值。', '北京市天水北路万达写字楼1306室', 0, '7000-9000', NULL, 98, 'ceshi', '2020-03-27 23:45:33', 0);
COMMIT;

-- ----------------------------
-- Table structure for resume
-- ----------------------------
DROP TABLE IF EXISTS `resume`;
CREATE TABLE `resume` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增Id',
  `title` varchar(50) DEFAULT NULL COMMENT '简历标题',
  `name` varchar(10) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `gender` varchar(1) DEFAULT NULL COMMENT '性别',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `working_hours` date DEFAULT NULL COMMENT '参加工作时间',
  `now_address` varchar(100) DEFAULT NULL COMMENT '现居住地址',
  `domicile_address` varchar(100) DEFAULT NULL COMMENT '户籍地址',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `job_intention` varchar(20) DEFAULT NULL COMMENT '求职意向',
  `education` varchar(10) DEFAULT NULL COMMENT '学历，本科、大专、高中、初中、小学',
  `school_of_graduation` varchar(20) DEFAULT NULL COMMENT '毕业院校',
  `major` varchar(20) DEFAULT NULL COMMENT '专业',
  `work_experience` text COMMENT '工作经历',
  `project_experience` text COMMENT '项目经验',
  `self_appraisal` text COMMENT '自我评价',
  `refresh_time` datetime DEFAULT NULL COMMENT '刷新简历时间',
  `is_hide` tinyint(1) DEFAULT '0' COMMENT '是否隐藏',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认简历',
  `wx_user_id` int(11) DEFAULT NULL COMMENT '对应微信Userid',
  `wx_nickname` varchar(30) DEFAULT NULL COMMENT '微信昵称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='简历表';

-- ----------------------------
-- Table structure for resume_send
-- ----------------------------
DROP TABLE IF EXISTS `resume_send`;
CREATE TABLE `resume_send` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增Id',
  `resume_id` int(11) DEFAULT NULL COMMENT '简历Id',
  `info_id` int(11) DEFAULT NULL COMMENT '招聘信息Id',
  `is_check` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否查看',
  `check_time` datetime DEFAULT NULL COMMENT '查看时间',
  `status` int(11) DEFAULT NULL COMMENT '1不合适，2面试邀请',
  `interview_time` datetime DEFAULT NULL COMMENT '面试时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `interview_status` int(11) DEFAULT NULL COMMENT '面试邀请状态，0：未回复，1：已接受',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='简历投递表';

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增Id',
  `user_id` int(11) NOT NULL COMMENT '用户Id',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `address` varchar(255) DEFAULT NULL COMMENT '登录地点',
  `ip` varchar(100) DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统登录日志';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮id',
  `parent_id` int(11) NOT NULL COMMENT '上级菜单id',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单/按钮名称',
  `path` varchar(255) DEFAULT NULL COMMENT '对应路由path',
  `component` varchar(255) DEFAULT NULL COMMENT '对应路由组件component',
  `perms` varchar(50) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `type` int(11) NOT NULL COMMENT '类型 0菜单 1按钮',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `is_link` tinyint(1) DEFAULT '0' COMMENT '是否是外链',
  `is_visible` tinyint(1) DEFAULT '1' COMMENT '是否可见,1为可见，0为不可见',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=250 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', '/system', '', NULL, 'system', 0, 1, 0, 1, '2019-12-31 09:31:22', '2019-12-31 10:55:40');
INSERT INTO `sys_menu` VALUES (3, 1, '用户管理', '/system/user', 'system/user/index', '', 'user', 0, 1, 0, 1, '2019-12-31 09:31:24', '2019-01-22 06:45:55');
INSERT INTO `sys_menu` VALUES (4, 1, '角色管理', '/system/role', 'system/role/index', '', 'peoples', 0, 2, 0, 1, '2019-12-31 09:31:27', '2019-12-31 09:33:37');
INSERT INTO `sys_menu` VALUES (5, 1, '菜单管理', '/system/menu', 'system/menu/index', '', 'tree-table', 0, 3, 0, 1, '2019-12-31 09:31:29', '2019-12-31 09:33:38');
INSERT INTO `sys_menu` VALUES (11, 3, '新增用户', '', '', 'system:user:add', NULL, 1, 3, 0, 1, '2019-12-31 09:31:30', '2020-02-25 12:19:25');
INSERT INTO `sys_menu` VALUES (12, 3, '修改用户', '', '', 'system:user:edit', NULL, 1, 5, 0, 1, '2019-12-31 09:31:31', '2020-02-25 13:59:31');
INSERT INTO `sys_menu` VALUES (13, 3, '删除用户', '', '', 'system:user:remove', NULL, 1, 6, 0, 1, '2019-12-31 09:31:32', '2020-02-25 12:19:39');
INSERT INTO `sys_menu` VALUES (14, 4, '新增角色', '', '', 'system:role:add', NULL, 1, 2, 0, 1, '2019-12-31 09:32:59', '2020-02-25 14:00:28');
INSERT INTO `sys_menu` VALUES (15, 4, '修改角色', '', '', 'system:role:edit', NULL, 1, 4, 0, 1, '2019-12-31 09:33:03', '2020-02-25 14:00:40');
INSERT INTO `sys_menu` VALUES (16, 4, '删除角色', '', '', 'system:role:remove', NULL, 1, 3, 0, 1, '2019-12-31 09:33:01', '2020-02-25 14:00:35');
INSERT INTO `sys_menu` VALUES (17, 5, '新增菜单', '', '', 'system:menu:add', NULL, 1, 5, 0, 1, '2019-12-31 09:33:06', '2020-02-25 14:02:02');
INSERT INTO `sys_menu` VALUES (18, 5, '修改菜单', '', '', 'system:menu:edit', NULL, 1, 2, 0, 1, '2019-12-31 09:33:08', '2020-02-25 14:01:36');
INSERT INTO `sys_menu` VALUES (19, 5, '删除菜单', '', '', 'system:menu:remove', NULL, 1, 3, 0, 1, '2019-12-31 09:33:10', '2020-02-25 14:01:49');
INSERT INTO `sys_menu` VALUES (144, 4, '导出Excel', NULL, NULL, 'system:role:export', NULL, 1, 5, 0, 1, '2019-12-31 09:33:17', '2020-02-25 14:00:44');
INSERT INTO `sys_menu` VALUES (148, 3, '密码重置', NULL, NULL, 'system:user:resetPwd', NULL, 1, 4, 0, 1, '2019-12-31 09:33:18', '2020-02-25 12:19:35');
INSERT INTO `sys_menu` VALUES (173, 0, '南风知我意', 'https://hxxzt.com', NULL, '', 'guide', 0, 10, 1, 1, '2020-02-20 16:37:14', '2020-02-25 17:23:59');
INSERT INTO `sys_menu` VALUES (174, 1, '日志管理', '/log', 'log/index', '', 'log', 0, 5, 0, 1, '2020-02-20 17:29:08', '2020-02-23 21:53:40');
INSERT INTO `sys_menu` VALUES (175, 174, '操作日志', '/log/operlog', 'log/operlog/index', '', 'form', 0, 1, 0, 1, '2020-02-20 17:31:26', '2020-02-20 17:31:28');
INSERT INTO `sys_menu` VALUES (176, 174, '登录日志', '/log/logininfor', 'log/logininfor/index', '', 'logininfor', 0, 2, 0, 1, '2020-02-20 17:35:49', '2020-02-20 17:35:51');
INSERT INTO `sys_menu` VALUES (177, 5, '角色查询', NULL, NULL, 'system:role:query', NULL, 1, 4, 0, 1, '2020-02-20 23:03:50', '2020-02-25 14:01:53');
INSERT INTO `sys_menu` VALUES (178, 176, '删除登录日志', NULL, NULL, 'log:logininfor:remove', NULL, 1, 2, 0, 1, '2020-02-21 00:09:29', '2020-02-25 14:05:26');
INSERT INTO `sys_menu` VALUES (179, 176, '清空登录日志', NULL, NULL, 'log:logininfor:clean', NULL, 1, 3, 0, 1, '2020-02-21 00:09:33', '2020-02-25 14:05:35');
INSERT INTO `sys_menu` VALUES (180, 176, '导出登录日志', NULL, NULL, 'log:logininfor:export', NULL, 1, 4, 0, 1, '2020-02-21 00:09:34', '2020-02-25 14:05:39');
INSERT INTO `sys_menu` VALUES (184, 3, '查询详细信息', NULL, NULL, 'system:user:query', NULL, 1, 2, 0, 1, '2020-02-22 00:01:44', '2020-02-25 13:59:22');
INSERT INTO `sys_menu` VALUES (185, 175, '删除操作日志', NULL, NULL, 'log:operlog:remove', NULL, 1, 3, 0, 1, '2020-02-23 01:09:37', '2020-02-25 14:04:11');
INSERT INTO `sys_menu` VALUES (186, 175, '清空操作日志', NULL, NULL, 'log:operlog:clean', NULL, 1, 4, 0, 1, '2020-02-23 01:09:38', '2020-02-25 14:04:16');
INSERT INTO `sys_menu` VALUES (187, 175, '导出操作日志', NULL, NULL, 'log:operlog:export', NULL, 1, 5, 0, 1, '2020-02-23 01:09:39', '2020-02-25 14:04:21');
INSERT INTO `sys_menu` VALUES (188, 175, '查询详细操作日志', NULL, NULL, 'log:operlog:query', NULL, 1, 2, 0, 1, '2020-02-23 01:12:47', '2020-02-25 14:03:59');
INSERT INTO `sys_menu` VALUES (189, 1, '在线用户', '/system/online', 'system/online/index', '', 'eye', 0, 4, 0, 1, '2020-02-23 19:07:11', '2020-02-23 21:53:49');
INSERT INTO `sys_menu` VALUES (190, 189, '强退用户', NULL, NULL, 'system:online:forcedAccountOut', NULL, 1, 2, 0, 1, '2020-02-23 19:13:30', '2020-02-25 14:02:35');
INSERT INTO `sys_menu` VALUES (192, 3, '查看列表', NULL, NULL, 'system:user:view', NULL, 1, 1, 0, 1, '2020-02-25 12:18:50', NULL);
INSERT INTO `sys_menu` VALUES (193, 4, '查看列表', NULL, NULL, 'system:role:view', NULL, 1, 1, 0, 1, '2020-02-25 14:00:07', NULL);
INSERT INTO `sys_menu` VALUES (194, 5, '查看列表', NULL, NULL, 'system:menu:view', NULL, 1, 1, 0, 1, '2020-02-25 14:01:22', NULL);
INSERT INTO `sys_menu` VALUES (195, 189, '查看列表', NULL, NULL, 'system:online:view', NULL, 1, 1, 0, 1, '2020-02-25 14:02:28', NULL);
INSERT INTO `sys_menu` VALUES (196, 175, '查看列表', NULL, NULL, 'log:operlog:view', NULL, 1, 1, 0, 1, '2020-02-25 14:03:40', '2020-02-25 14:12:50');
INSERT INTO `sys_menu` VALUES (197, 176, '查看列表', NULL, NULL, 'log:logininfor:view', NULL, 1, 1, 0, 1, '2020-02-25 14:05:16', '2020-02-25 14:12:58');
INSERT INTO `sys_menu` VALUES (198, 0, '企业管理', '/enterprise', NULL, NULL, 'build', 0, 2, 0, 1, '2020-03-02 20:27:34', '2020-03-02 20:55:32');
INSERT INTO `sys_menu` VALUES (199, 198, '待审核企业', '/enterprise/reviewlist', 'enterprise/reviewlist/index', NULL, 'search', 0, 2, 0, 1, '2020-03-02 20:28:00', '2020-03-04 17:07:55');
INSERT INTO `sys_menu` VALUES (200, 198, '已审核企业', '/enterprise/list', 'enterprise/list/index', NULL, 'chart', 0, 1, 0, 1, '2020-03-02 20:32:46', '2020-03-04 17:08:09');
INSERT INTO `sys_menu` VALUES (204, 0, '信息发布', '/publishinfo', NULL, NULL, 'form', 0, 5, 0, 1, '2020-03-02 23:26:40', '2020-03-03 00:05:22');
INSERT INTO `sys_menu` VALUES (205, 204, '宣讲会发布', '/publishinfo/careerTalk', 'publishinfo/careerTalk/index', NULL, 'peoples', 0, 1, 0, 1, '2020-03-02 23:29:55', NULL);
INSERT INTO `sys_menu` VALUES (206, 204, '招聘发布', '/publishinfo/recruitment', 'publishinfo/recruitment/index', NULL, 'example', 0, 2, 0, 1, '2020-03-02 23:30:37', NULL);
INSERT INTO `sys_menu` VALUES (207, 0, '微信管理', '/weChat', NULL, NULL, 'wechat', 0, 3, 0, 1, '2020-03-02 23:31:54', '2020-03-03 00:05:07');
INSERT INTO `sys_menu` VALUES (208, 207, '微信用户管理', '/weChat/user', 'weChat/user/index', NULL, 'peoples', 0, 1, 0, 1, '2020-03-02 23:32:29', '2020-03-02 23:38:36');
INSERT INTO `sys_menu` VALUES (209, 207, '点赞管理', '/weChat/click', 'weChat/click/index', NULL, 'post', 0, 2, 0, 1, '2020-03-02 23:33:19', '2020-03-02 23:39:03');
INSERT INTO `sys_menu` VALUES (210, 207, '关注管理', '/weChat/focus', 'weChat/focus/index', NULL, 'eye', 0, 3, 0, 1, '2020-03-02 23:33:38', '2020-03-02 23:39:25');
INSERT INTO `sys_menu` VALUES (211, 207, '评论管理', '/weChat/comment', 'weChat/comment/index', NULL, 'edit', 0, 4, 0, 1, '2020-03-02 23:33:56', '2020-03-02 23:39:43');
INSERT INTO `sys_menu` VALUES (212, 0, '企业认证', '/identification', '', NULL, 'validCode', 0, 6, 0, 1, '2020-03-02 23:46:18', '2020-03-03 00:04:59');
INSERT INTO `sys_menu` VALUES (213, 212, '企业认证', '/identification/index', 'identification/index', NULL, 'validCode', 0, 1, 0, 1, '2020-03-02 23:55:07', '2020-03-02 23:59:04');
INSERT INTO `sys_menu` VALUES (214, 0, '信息管理', '/infoManage', NULL, NULL, 'list', 0, 4, 0, 1, '2020-03-03 00:04:14', '2020-03-03 00:08:20');
INSERT INTO `sys_menu` VALUES (215, 214, '宣讲会', '/infoManage/careerTalk', 'infoManage/careerTalk/index', NULL, 'peoples', 0, 1, 0, 1, '2020-03-03 00:06:09', '2020-03-03 00:08:31');
INSERT INTO `sys_menu` VALUES (216, 214, '招聘', '/infoManage/recruitment', 'infoManage/recruitment/index', NULL, 'example', 0, 2, 0, 1, '2020-03-03 00:06:53', NULL);
INSERT INTO `sys_menu` VALUES (217, 200, '查看列表', NULL, NULL, 'bus:reviewEnt:view', NULL, 1, 1, 0, 1, '2020-03-04 16:34:40', NULL);
INSERT INTO `sys_menu` VALUES (218, 200, '查看详情', NULL, NULL, 'bus:reviewEnt:query', NULL, 1, 2, 0, 1, '2020-03-04 16:35:02', NULL);
INSERT INTO `sys_menu` VALUES (219, 200, '删除', NULL, NULL, 'bus:reviewEnt:del', NULL, 1, 3, 0, 1, '2020-03-04 16:40:37', NULL);
INSERT INTO `sys_menu` VALUES (220, 199, '查看列表', NULL, NULL, 'bus:notReviewEnt:view', NULL, 1, 1, 0, 1, '2020-03-04 16:41:29', '2020-03-04 17:08:57');
INSERT INTO `sys_menu` VALUES (221, 199, '查看详情', NULL, NULL, 'bus:notReviewEnt:query', NULL, 1, 2, 0, 1, '2020-03-04 16:43:34', '2020-03-04 17:09:35');
INSERT INTO `sys_menu` VALUES (222, 199, '审核通过', NULL, NULL, 'bus:notReviewEnt:review', NULL, 1, 3, 0, 1, '2020-03-04 16:43:50', '2020-03-04 17:09:21');
INSERT INTO `sys_menu` VALUES (223, 215, '查看列表', NULL, NULL, 'bus:careerTalk:view', NULL, 1, 1, 0, 1, '2020-03-04 16:47:15', '2020-03-04 16:48:11');
INSERT INTO `sys_menu` VALUES (224, 215, '删除', NULL, NULL, 'bus:careerTalk:del', NULL, 1, 2, 0, 1, '2020-03-04 16:47:27', '2020-03-04 16:48:18');
INSERT INTO `sys_menu` VALUES (225, 215, '修改', NULL, NULL, 'bus:careerTalk:edit', NULL, 1, 3, 0, 1, '2020-03-04 16:47:42', '2020-03-04 16:48:24');
INSERT INTO `sys_menu` VALUES (226, 215, '查询详细', NULL, NULL, 'bus:careerTalk:query', NULL, 1, 4, 0, 1, '2020-03-04 16:47:57', '2020-03-04 16:48:31');
INSERT INTO `sys_menu` VALUES (227, 216, '查看列表', NULL, NULL, 'bus:recruitment:view', NULL, 1, 1, 0, 1, '2020-03-04 16:48:51', NULL);
INSERT INTO `sys_menu` VALUES (228, 216, '删除', NULL, NULL, 'bus:recruitment:del', NULL, 1, 2, 0, 1, '2020-03-04 16:49:04', NULL);
INSERT INTO `sys_menu` VALUES (229, 216, '修改', NULL, NULL, 'bus:recruitment:edit', NULL, 1, 3, 0, 1, '2020-03-04 16:49:19', NULL);
INSERT INTO `sys_menu` VALUES (230, 216, '查询详细', NULL, NULL, 'bus:recruitment:query', NULL, 1, 4, 0, 1, '2020-03-04 16:49:32', NULL);
INSERT INTO `sys_menu` VALUES (231, 208, '查看列表', NULL, NULL, 'bus:wxUser:view', NULL, 1, 1, 0, 1, '2020-03-04 16:52:36', NULL);
INSERT INTO `sys_menu` VALUES (232, 208, '查询详细', NULL, NULL, 'bus:wxUser:query', NULL, 1, 2, 0, 1, '2020-03-04 16:53:00', NULL);
INSERT INTO `sys_menu` VALUES (233, 208, '删除', NULL, NULL, 'bus:wxUser:del', NULL, 1, 3, 0, 1, '2020-03-04 16:53:13', NULL);
INSERT INTO `sys_menu` VALUES (234, 209, '查看列表', NULL, NULL, 'bus:click:view', NULL, 1, 1, 0, 1, '2020-03-04 16:53:26', NULL);
INSERT INTO `sys_menu` VALUES (235, 209, '删除', NULL, NULL, 'bus:click:del', NULL, 1, 2, 0, 1, '2020-03-04 16:53:39', NULL);
INSERT INTO `sys_menu` VALUES (236, 210, '查看列表', NULL, NULL, 'bus:focus:view', NULL, 1, 1, 0, 1, '2020-03-04 16:53:53', NULL);
INSERT INTO `sys_menu` VALUES (237, 210, '删除', NULL, '', 'bus:focus:del', NULL, 1, 2, 0, 1, '2020-03-04 16:54:11', NULL);
INSERT INTO `sys_menu` VALUES (238, 211, '查看列表', NULL, NULL, 'bus:comment:view', NULL, 1, 1, 0, 1, '2020-03-04 16:54:32', '2020-03-04 16:55:23');
INSERT INTO `sys_menu` VALUES (239, 211, '查询详细', NULL, NULL, 'bus:comment:query', NULL, 1, 2, 0, 1, '2020-03-04 16:55:36', '2020-03-04 17:22:23');
INSERT INTO `sys_menu` VALUES (240, 211, '删除', NULL, NULL, 'bus:comment:del', NULL, 1, 3, 0, 1, '2020-03-04 16:55:49', NULL);
INSERT INTO `sys_menu` VALUES (241, 211, '修改', NULL, NULL, 'bus:comment:edit', NULL, 1, 4, 0, 1, '2020-03-04 16:56:03', NULL);
INSERT INTO `sys_menu` VALUES (242, 207, '简历管理', '/weChat/resume', 'weChat/resume/index', NULL, 'documentation', 0, 5, 0, 1, '2020-03-05 22:09:55', NULL);
INSERT INTO `sys_menu` VALUES (243, 214, '简历查看', '/infoManage/resume', 'infoManage/resume/index', NULL, 'clipboard', 0, 3, 0, 1, '2020-03-06 00:24:56', NULL);
INSERT INTO `sys_menu` VALUES (244, 242, '查看列表', NULL, NULL, 'bus:resume:view', NULL, 1, 1, 0, 1, '2020-03-07 01:03:03', NULL);
INSERT INTO `sys_menu` VALUES (245, 242, '查看详细', NULL, NULL, 'bus:resume:query', NULL, 1, 2, 0, 1, '2020-03-07 01:03:36', NULL);
INSERT INTO `sys_menu` VALUES (246, 242, '删除', NULL, NULL, 'bus:resume:del', NULL, 1, 3, 0, 1, '2020-03-07 01:04:10', NULL);
INSERT INTO `sys_menu` VALUES (247, 242, '修改', NULL, NULL, 'bus:resume:edit', NULL, 1, 4, 0, 1, '2020-03-07 01:04:40', NULL);
INSERT INTO `sys_menu` VALUES (248, 243, '查看列表', NULL, NULL, 'bus:resumeSend:view', NULL, 1, 1, 0, 1, '2020-03-07 01:07:03', NULL);
INSERT INTO `sys_menu` VALUES (249, 243, '查看详细', NULL, NULL, 'bus:resumeSend:query', NULL, 1, 2, 0, 1, '2020-03-07 01:07:27', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `module` varchar(20) DEFAULT NULL COMMENT '系统模版',
  `username` varchar(50) DEFAULT NULL COMMENT '操作用户',
  `operation` varchar(100) DEFAULT NULL COMMENT '操作内容',
  `time` mediumtext COMMENT '耗时(ms)',
  `method` text COMMENT '操作方法',
  `params` text COMMENT '方法参数',
  `ip` varchar(64) DEFAULT NULL COMMENT '操作者IP',
  `address` varchar(50) DEFAULT NULL COMMENT '操作地点',
  `result` text COMMENT '返回结果',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_success` tinyint(1) DEFAULT NULL COMMENT '是否操作成功',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='操作日志表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(10) NOT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '超级管理员', '勿删除。超级管理拥有全部权限。', '2019-12-31 09:35:41', '2020-03-07 01:08:30');
INSERT INTO `sys_role` VALUES (72, '企业用户', '勿删除。企业用户可发布信息和查看修改发布的信息，查看投递的简历。', '2019-12-31 09:35:43', '2020-03-07 01:09:08');
INSERT INTO `sys_role` VALUES (103, '注册用户', '勿删除。注册用户只能通过认证审核通过之后才能成为企业用户并发布信息。', '2020-03-03 00:00:19', '2020-03-04 16:56:52');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `role_id` int(11) NOT NULL COMMENT '角色Id',
  `menu_id` int(11) NOT NULL COMMENT '菜单Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3441 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES (2824, 103, 212);
INSERT INTO `sys_role_menu` VALUES (2825, 103, 213);
INSERT INTO `sys_role_menu` VALUES (3226, 1, 1);
INSERT INTO `sys_role_menu` VALUES (3227, 1, 3);
INSERT INTO `sys_role_menu` VALUES (3228, 1, 192);
INSERT INTO `sys_role_menu` VALUES (3229, 1, 184);
INSERT INTO `sys_role_menu` VALUES (3230, 1, 11);
INSERT INTO `sys_role_menu` VALUES (3231, 1, 148);
INSERT INTO `sys_role_menu` VALUES (3232, 1, 12);
INSERT INTO `sys_role_menu` VALUES (3233, 1, 13);
INSERT INTO `sys_role_menu` VALUES (3234, 1, 4);
INSERT INTO `sys_role_menu` VALUES (3235, 1, 193);
INSERT INTO `sys_role_menu` VALUES (3236, 1, 14);
INSERT INTO `sys_role_menu` VALUES (3237, 1, 16);
INSERT INTO `sys_role_menu` VALUES (3238, 1, 15);
INSERT INTO `sys_role_menu` VALUES (3239, 1, 144);
INSERT INTO `sys_role_menu` VALUES (3240, 1, 5);
INSERT INTO `sys_role_menu` VALUES (3241, 1, 194);
INSERT INTO `sys_role_menu` VALUES (3242, 1, 18);
INSERT INTO `sys_role_menu` VALUES (3243, 1, 19);
INSERT INTO `sys_role_menu` VALUES (3244, 1, 177);
INSERT INTO `sys_role_menu` VALUES (3245, 1, 17);
INSERT INTO `sys_role_menu` VALUES (3246, 1, 189);
INSERT INTO `sys_role_menu` VALUES (3247, 1, 195);
INSERT INTO `sys_role_menu` VALUES (3248, 1, 190);
INSERT INTO `sys_role_menu` VALUES (3249, 1, 174);
INSERT INTO `sys_role_menu` VALUES (3250, 1, 175);
INSERT INTO `sys_role_menu` VALUES (3251, 1, 196);
INSERT INTO `sys_role_menu` VALUES (3252, 1, 188);
INSERT INTO `sys_role_menu` VALUES (3253, 1, 185);
INSERT INTO `sys_role_menu` VALUES (3254, 1, 186);
INSERT INTO `sys_role_menu` VALUES (3255, 1, 187);
INSERT INTO `sys_role_menu` VALUES (3256, 1, 176);
INSERT INTO `sys_role_menu` VALUES (3257, 1, 197);
INSERT INTO `sys_role_menu` VALUES (3258, 1, 178);
INSERT INTO `sys_role_menu` VALUES (3259, 1, 179);
INSERT INTO `sys_role_menu` VALUES (3260, 1, 180);
INSERT INTO `sys_role_menu` VALUES (3261, 1, 198);
INSERT INTO `sys_role_menu` VALUES (3262, 1, 200);
INSERT INTO `sys_role_menu` VALUES (3263, 1, 217);
INSERT INTO `sys_role_menu` VALUES (3264, 1, 218);
INSERT INTO `sys_role_menu` VALUES (3265, 1, 219);
INSERT INTO `sys_role_menu` VALUES (3266, 1, 199);
INSERT INTO `sys_role_menu` VALUES (3267, 1, 220);
INSERT INTO `sys_role_menu` VALUES (3268, 1, 221);
INSERT INTO `sys_role_menu` VALUES (3269, 1, 222);
INSERT INTO `sys_role_menu` VALUES (3270, 1, 207);
INSERT INTO `sys_role_menu` VALUES (3271, 1, 208);
INSERT INTO `sys_role_menu` VALUES (3272, 1, 231);
INSERT INTO `sys_role_menu` VALUES (3273, 1, 232);
INSERT INTO `sys_role_menu` VALUES (3274, 1, 233);
INSERT INTO `sys_role_menu` VALUES (3275, 1, 209);
INSERT INTO `sys_role_menu` VALUES (3276, 1, 234);
INSERT INTO `sys_role_menu` VALUES (3277, 1, 235);
INSERT INTO `sys_role_menu` VALUES (3278, 1, 210);
INSERT INTO `sys_role_menu` VALUES (3279, 1, 236);
INSERT INTO `sys_role_menu` VALUES (3280, 1, 237);
INSERT INTO `sys_role_menu` VALUES (3281, 1, 211);
INSERT INTO `sys_role_menu` VALUES (3282, 1, 238);
INSERT INTO `sys_role_menu` VALUES (3283, 1, 239);
INSERT INTO `sys_role_menu` VALUES (3284, 1, 240);
INSERT INTO `sys_role_menu` VALUES (3285, 1, 241);
INSERT INTO `sys_role_menu` VALUES (3286, 1, 242);
INSERT INTO `sys_role_menu` VALUES (3287, 1, 244);
INSERT INTO `sys_role_menu` VALUES (3288, 1, 245);
INSERT INTO `sys_role_menu` VALUES (3289, 1, 246);
INSERT INTO `sys_role_menu` VALUES (3290, 1, 247);
INSERT INTO `sys_role_menu` VALUES (3291, 1, 214);
INSERT INTO `sys_role_menu` VALUES (3292, 1, 215);
INSERT INTO `sys_role_menu` VALUES (3293, 1, 223);
INSERT INTO `sys_role_menu` VALUES (3294, 1, 224);
INSERT INTO `sys_role_menu` VALUES (3295, 1, 225);
INSERT INTO `sys_role_menu` VALUES (3296, 1, 226);
INSERT INTO `sys_role_menu` VALUES (3297, 1, 216);
INSERT INTO `sys_role_menu` VALUES (3298, 1, 227);
INSERT INTO `sys_role_menu` VALUES (3299, 1, 228);
INSERT INTO `sys_role_menu` VALUES (3300, 1, 229);
INSERT INTO `sys_role_menu` VALUES (3301, 1, 230);
INSERT INTO `sys_role_menu` VALUES (3302, 1, 243);
INSERT INTO `sys_role_menu` VALUES (3303, 1, 248);
INSERT INTO `sys_role_menu` VALUES (3304, 1, 249);
INSERT INTO `sys_role_menu` VALUES (3305, 1, 204);
INSERT INTO `sys_role_menu` VALUES (3306, 1, 205);
INSERT INTO `sys_role_menu` VALUES (3307, 1, 206);
INSERT INTO `sys_role_menu` VALUES (3308, 1, 212);
INSERT INTO `sys_role_menu` VALUES (3309, 1, 213);
INSERT INTO `sys_role_menu` VALUES (3310, 72, 214);
INSERT INTO `sys_role_menu` VALUES (3311, 72, 215);
INSERT INTO `sys_role_menu` VALUES (3312, 72, 223);
INSERT INTO `sys_role_menu` VALUES (3313, 72, 224);
INSERT INTO `sys_role_menu` VALUES (3314, 72, 225);
INSERT INTO `sys_role_menu` VALUES (3315, 72, 226);
INSERT INTO `sys_role_menu` VALUES (3316, 72, 216);
INSERT INTO `sys_role_menu` VALUES (3317, 72, 227);
INSERT INTO `sys_role_menu` VALUES (3318, 72, 228);
INSERT INTO `sys_role_menu` VALUES (3319, 72, 229);
INSERT INTO `sys_role_menu` VALUES (3320, 72, 230);
INSERT INTO `sys_role_menu` VALUES (3321, 72, 243);
INSERT INTO `sys_role_menu` VALUES (3322, 72, 248);
INSERT INTO `sys_role_menu` VALUES (3323, 72, 249);
INSERT INTO `sys_role_menu` VALUES (3324, 72, 204);
INSERT INTO `sys_role_menu` VALUES (3325, 72, 205);
INSERT INTO `sys_role_menu` VALUES (3326, 72, 206);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `nick_name` varchar(20) DEFAULT NULL COMMENT '用户昵称/真实姓名',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `last_login_ip` varchar(20) DEFAULT NULL COMMENT '上次登录Ip',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近访问时间',
  `ssex` int(11) DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `avatar` varchar(500) DEFAULT '/image/avatar/default.jpg' COMMENT '用户头像',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态 0锁定 1有效,默认为1',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `t_user_username_uindex` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (44, 'admin', 'admin', '1d6b9e506ef40eea815d973916cfbc0d', 'niko_hxx@163.com', '13252082376', '125.75.20.219', '2020-03-27 22:42:47', 0, '南风知我意技术博客：www.hxxzt.com', '/image/avatar/202032/f1ceeea5-df80-43aa-9147-677d1e4b9b8b', '2019-12-24 11:29:00', '2019-12-30 09:10:39', 1);
INSERT INTO `sys_user` VALUES (98, 'ceshi', NULL, '9cac7d39166fdde33d3a3b874026dc6d', '130@qq.com', '13090901212', '125.75.20.219', '2020-03-27 22:46:16', NULL, NULL, '/image/avatar/default.jpg', '2020-03-27 22:45:59', NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_config`;
CREATE TABLE `sys_user_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增Id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `layout` varchar(10) DEFAULT NULL COMMENT '系统布局 side侧边栏，head顶部栏',
  `multi_page` char(1) DEFAULT NULL COMMENT '页面风格 1多标签页 0单页',
  `fix_siderbar` char(1) DEFAULT NULL COMMENT '页面滚动是否固定侧边栏 1固定 0不固定',
  `fix_header` char(1) DEFAULT NULL COMMENT '页面滚动是否固定顶栏 1固定 0不固定',
  `color` varchar(20) DEFAULT NULL COMMENT '主题颜色 rgb值',
  `theme` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户后台主题表';

-- ----------------------------
-- Records of sys_user_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_config` VALUES (1, 44, 'side', '0', '1', '1', 'rgb(24, 144, 255)', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (97, 44, 1);
INSERT INTO `sys_user_role` VALUES (136, 98, 72);
COMMIT;

-- ----------------------------
-- Table structure for todo
-- ----------------------------
DROP TABLE IF EXISTS `todo`;
CREATE TABLE `todo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增Id',
  `time` datetime DEFAULT NULL COMMENT '时间',
  `content` varchar(1000) DEFAULT NULL COMMENT '内容',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `wx_user_id` int(11) DEFAULT NULL COMMENT '微信用户ID',
  `wx_nickname` varchar(30) DEFAULT NULL COMMENT '微信用户昵称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='待办事项';

-- ----------------------------
-- Table structure for wx_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '微信用户Id',
  `nick_name` varchar(30) DEFAULT NULL COMMENT '微信用户昵称',
  `open_id` varchar(30) DEFAULT NULL COMMENT '微信openId',
  `wx_id` varchar(30) DEFAULT NULL COMMENT '微信号',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(200) DEFAULT NULL COMMENT '微信用户头像地址',
  `gender` int(11) DEFAULT NULL COMMENT '性别，0：未知，1男，2女',
  `last_login_time` datetime DEFAULT NULL COMMENT '上次登录时间',
  `last_login_ip` varchar(30) DEFAULT NULL COMMENT '上次登录IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_speak` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁言',
  `school` varchar(20) DEFAULT NULL COMMENT '所属学校',
  `is_student` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为学生',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '账号状态：0锁定，1有效',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='微信小程序用户';

-- ----------------------------
-- View structure for school
-- ----------------------------
DROP VIEW IF EXISTS `school`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `school` AS (select distinct `wx_user`.`school` AS `school` from `wx_user` where ((`wx_user`.`school` is not null) and (`wx_user`.`school` <> '')));

SET FOREIGN_KEY_CHECKS = 1;
