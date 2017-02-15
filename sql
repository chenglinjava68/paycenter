/*
Navicat MySQL Data Transfer

Source Server         : qccr5.122
Source Server Version : 50627
Source Host           : 192.168.5.122:3306
Source Database       : paycenter

Target Server Type    : MYSQL
Target Server Version : 50627
File Encoding         : 65001

Date: 2017-02-15 11:38:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for card_info
-- ----------------------------
DROP TABLE IF EXISTS `card_info`;
CREATE TABLE `card_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `card_name` varchar(32) NOT NULL COMMENT '持卡人姓名',
  `card_no` varchar(1024) NOT NULL COMMENT '卡号',
  `expiry_date` varchar(16) NOT NULL COMMENT '卡片有效期',
  `card_type` tinyint(4) DEFAULT '1' COMMENT '，卡类型(信用卡1还是储蓄卡2,)',
  `user_id` varchar(50) DEFAULT 'system' COMMENT '用户Id',
  `is_sign` tinyint(4) DEFAULT '0' COMMENT '是否已经签约',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_person` varchar(50) DEFAULT 'system' COMMENT '创建人',
  `update_person` varchar(50) DEFAULT 'system' COMMENT '修改人',
  `pay_channel` varchar(64) DEFAULT NULL COMMENT '支付渠道',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=338 DEFAULT CHARSET=utf8 COMMENT='卡信息表';

-- ----------------------------
-- Table structure for commerce_order
-- ----------------------------
DROP TABLE IF EXISTS `commerce_order`;
CREATE TABLE `commerce_order` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `trade_no` varchar(32) NOT NULL COMMENT '支付订单编号',
  `business_no` varchar(32) NOT NULL COMMENT '外部业务订单编号',
  `business_type` varchar(32) NOT NULL COMMENT '外部业务订单类型',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态',
  `total_amount` int(10) NOT NULL COMMENT '订单总金额',
  `source` varchar(64) DEFAULT NULL COMMENT '支付订单所属',
  `partner` varchar(64) DEFAULT NULL COMMENT '支付订单来源',
  `which_pay` varchar(50) DEFAULT 'system' COMMENT '哪类支付类型：混合支付(blend_pay)，线下支付(offline_pay)，线上支付(online_pay)',
  `trade_type` varchar(50) DEFAULT 'system' COMMENT '交易种类：分期支付(instalment)，一次付(once_pay)，多笔付(multiple_pay)  ',
  `user_id` varchar(50) DEFAULT 'system' COMMENT '用户Id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_person` varchar(50) DEFAULT 'system' COMMENT '创建人',
  `update_person` varchar(50) DEFAULT 'system' COMMENT '修改人',
  `biz_type` varchar(32) DEFAULT NULL COMMENT '产品类型：B2C,B2B',
  `sys_source` varchar(64) DEFAULT NULL COMMENT '系统源，逐步替换掉partner',
  PRIMARY KEY (`id`),
  KEY `idx_trade_no` (`trade_no`),
  KEY `idx_business_no` (`business_no`)
) ENGINE=InnoDB AUTO_INCREMENT=10382 DEFAULT CHARSET=utf8 COMMENT='交易订单,主订单表';

-- ----------------------------
-- Table structure for facepay_auth
-- ----------------------------
DROP TABLE IF EXISTS `facepay_auth`;
CREATE TABLE `facepay_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `super_user_id` bigint(20) NOT NULL COMMENT '超人平台上面的商户的userId',
  `channel` varchar(32) DEFAULT '' COMMENT 'alipay||wechat',
  `app_id` varchar(32) DEFAULT '' COMMENT '开发者的app_id,超人的appId',
  `app_auth_code` varchar(32) DEFAULT '' COMMENT '授权码',
  `code_create_time` datetime DEFAULT NULL COMMENT '授权码生成时间',
  `app_auth_token` varchar(64) DEFAULT '' COMMENT '授权TOKEN',
  `token_create_time` datetime DEFAULT NULL COMMENT '授权TOKEN生成时间',
  `out_user_id` varchar(32) DEFAULT '' COMMENT '授权商户的userId,授权者的PID',
  `out_auth_app_id` varchar(32) DEFAULT '' COMMENT '授权商户AppId',
  `expires_in` int(11) DEFAULT '0' COMMENT '交换令牌的有效期，单位秒，换算成天的话为365天',
  `re_expires_in` int(11) DEFAULT '0' COMMENT '刷新令牌有效期，单位秒，换算成天的话为372天',
  `app_refresh_token` varchar(64) DEFAULT '' COMMENT '刷新令牌时使用',
  `status` tinyint(1) DEFAULT '1' COMMENT '正常1,过期2,若过期会生成新一条记录，老记录在24小时内可继续使用',
  `notifyurl_create_time` datetime DEFAULT NULL COMMENT '回调地址生成时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_person` varchar(20) NOT NULL DEFAULT 'system' COMMENT '创建人',
  `update_person` varchar(20) NOT NULL DEFAULT 'system' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8 COMMENT='当面付商家授权信息表';

-- ----------------------------
-- Table structure for pay_code_serial
-- ----------------------------
DROP TABLE IF EXISTS `pay_code_serial`;
CREATE TABLE `pay_code_serial` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `card_no` varchar(1024) NOT NULL COMMENT '卡号',
  `request_add_data` varchar(1024) DEFAULT NULL COMMENT '请求的附加域',
  `return_add_data` varchar(1024) DEFAULT NULL COMMENT '返回的附加域，00+12位手续费+3位错误代码+手机号后四位+2位验证代码序号',
  `trace_no` varchar(50) DEFAULT NULL COMMENT '流水号,商户自定义6位流水号(可重复提交)',
  `invioce_no` varchar(50) DEFAULT NULL COMMENT '票据号，6位批次号+6位凭证号每天唯一(不可重复提交，由0~9数字组成)',
  `user_id` varchar(50) DEFAULT 'system' COMMENT '用户Id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_person` varchar(50) DEFAULT 'system' COMMENT '创建人',
  `update_person` varchar(50) DEFAULT 'system' COMMENT '修改人',
  `pay_channel` varchar(64) DEFAULT NULL COMMENT '支付渠道',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=752 DEFAULT CHARSET=utf8 COMMENT='获取消费验证码流水信息表';

-- ----------------------------
-- Table structure for pay_compensate
-- ----------------------------
DROP TABLE IF EXISTS `pay_compensate`;
CREATE TABLE `pay_compensate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `compensate_no` varchar(255) NOT NULL COMMENT '支付补偿订单编号',
  `batch_no` varchar(255) DEFAULT NULL COMMENT '补偿批次编号',
  `pay_no` varchar(255) NOT NULL COMMENT '对应，支付订单编号',
  `business_no` varchar(255) DEFAULT NULL COMMENT '业务订单编号',
  `business_type` varchar(255) DEFAULT NULL COMMENT '业务订单类型',
  `bill_no` varchar(255) DEFAULT NULL COMMENT '第三方支付，支付编号',
  `pay_channel` varchar(255) DEFAULT NULL COMMENT '支付渠道',
  `pay_type` varchar(255) DEFAULT NULL COMMENT '支付类型',
  `payer_account` varchar(255) DEFAULT NULL COMMENT '用户第三方支付账号',
  `partner` varchar(255) DEFAULT NULL COMMENT '原支付订单来源',
  `source` varchar(255) DEFAULT NULL COMMENT '补偿处理完成，通知指向',
  `refund_bill_no` varchar(255) DEFAULT NULL COMMENT '三方退款编号',
  `refund_time` datetime DEFAULT NULL COMMENT '处理退款时间',
  `create_person` varchar(255) DEFAULT NULL COMMENT '记录创建者',
  `update_person` varchar(255) DEFAULT NULL COMMENT '记录修改者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态，0-创建，1-成功，2-失败',
  `amount` int(10) DEFAULT NULL COMMENT '金额，单位-分',
  `mch_id` varchar(255) DEFAULT NULL COMMENT '第三方商户号',
  `success_time` datetime DEFAULT NULL COMMENT '补偿退款完成时间',
  `compensate_type` tinyint(1) DEFAULT '0' COMMENT '赔偿类型，0-common，1-repeat，2-close',
  `refund_no` varchar(32) DEFAULT NULL COMMENT '退款编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_pno_channel` (`pay_no`,`pay_channel`) USING BTREE,
  KEY `idex_bachno` (`batch_no`),
  KEY `idx_ct_sta_amount` (`create_time`,`status`,`amount`),
  KEY `idx_cno` (`compensate_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=222222563 DEFAULT CHARSET=utf8 COMMENT='支付补偿表';

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `pay_no` varchar(32) NOT NULL COMMENT '支付订单编号',
  `business_no` varchar(32) NOT NULL COMMENT '外部业务订单编号',
  `business_type` varchar(32) NOT NULL COMMENT '外部业务订单类型',
  `status` tinyint(1) DEFAULT '0' COMMENT '订单状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_person` varchar(50) DEFAULT 'system' COMMENT '创建人',
  `update_person` varchar(50) DEFAULT 'system' COMMENT '修改人',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '外部系统通知地址',
  `notify_status` tinyint(1) DEFAULT NULL COMMENT '通知状态',
  `pay_serial_no` varchar(32) DEFAULT NULL COMMENT '流水编号',
  `amount` int(10) NOT NULL COMMENT '支付金额',
  `pay_channel` varchar(64) DEFAULT NULL COMMENT '支付渠道',
  `pay_type` varchar(64) DEFAULT NULL COMMENT '支付类型',
  `bill_no` varchar(64) DEFAULT NULL COMMENT '三方支付编号',
  `mch_id` varchar(64) DEFAULT NULL COMMENT '商户号',
  `payer_account` varchar(255) DEFAULT NULL COMMENT '用户支付账号',
  `partner` varchar(64) DEFAULT NULL COMMENT '支付订单来源',
  `pay_time` datetime DEFAULT NULL COMMENT '支付请求时间',
  `success_time` datetime DEFAULT NULL COMMENT '支付完成时间',
  `out_time` datetime DEFAULT NULL COMMENT '超时截止时间',
  `retain` varchar(255) DEFAULT NULL COMMENT '保留字段',
  `trade_no` varchar(32) DEFAULT NULL COMMENT '支付订单编号',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户Id',
  `remitter` varchar(50) DEFAULT NULL COMMENT '汇款人',
  `short_pay_no` varchar(32) DEFAULT NULL COMMENT '短支付流水',
  `request_param` varchar(1024) DEFAULT NULL COMMENT '支付请求的全部参数对象json',
  `return_param` varchar(1024) DEFAULT NULL COMMENT '支付返回的全部参数对象json',
  `receipt_amount` bigint(20) DEFAULT NULL COMMENT '实付金额',
  `debit_user_id` bigint(20) DEFAULT NULL COMMENT '收款用户Id',
  `sys_source` varchar(64) DEFAULT NULL COMMENT '系统源，逐步替换掉partner',
  `show_info` varchar(1024) DEFAULT NULL COMMENT '收银台展示信息，长文本',
  `mark_status` tinyint(1) DEFAULT '0' COMMENT '标记同步返回支付成功，默认0，标记成功1',
  `return_url` varchar(128) DEFAULT NULL COMMENT '返回url',
  `subject` varchar(128) DEFAULT NULL COMMENT '交易主题',
  `summary` varchar(128) DEFAULT NULL COMMENT '交易描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_bzno_pa` (`business_no`,`partner`),
  KEY `idx_tradeno` (`trade_no`),
  KEY `idx_payno` (`pay_no`)
) ENGINE=InnoDB AUTO_INCREMENT=20374 DEFAULT CHARSET=utf8 COMMENT='支付订单';

-- ----------------------------
-- Table structure for pay_serial
-- ----------------------------
DROP TABLE IF EXISTS `pay_serial`;
CREATE TABLE `pay_serial` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `serial_no` varchar(32) DEFAULT NULL COMMENT '支付流水编号',
  `pay_no` varchar(32) DEFAULT NULL COMMENT '支付订单编号',
  `product_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `pay_channel` varchar(32) DEFAULT NULL COMMENT '支付渠道',
  `pay_type` varchar(32) DEFAULT NULL COMMENT '支付类型',
  `mch_id` varchar(64) DEFAULT NULL COMMENT '商户号',
  `payer_account` varchar(64) DEFAULT NULL COMMENT '支付账号',
  `amount` int(10) DEFAULT NULL COMMENT '支付金额',
  `pay_data` varchar(1024) DEFAULT NULL COMMENT '支付数据',
  `is_timeout` tinyint(1) DEFAULT NULL COMMENT '是否超时',
  `out_time` datetime DEFAULT NULL COMMENT '超时时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `return_url` varchar(255) DEFAULT NULL COMMENT '页面返回url',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '三方通知支付平台url',
  `bill_no` varchar(64) DEFAULT NULL COMMENT '三方支付编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_person` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_person` varchar(64) DEFAULT NULL COMMENT '修改人',
  `subject` varchar(255) DEFAULT NULL COMMENT '支付主题',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态',
  `success_time` datetime DEFAULT NULL COMMENT '支付完成时间',
  `retain` varchar(255) DEFAULT NULL COMMENT '保留字段',
  `receipt_amount` bigint(20) DEFAULT NULL COMMENT '实付金额',
  PRIMARY KEY (`id`),
  KEY `idx_payno_ch_type` (`pay_no`,`pay_channel`,`pay_type`),
  KEY `idx_sno` (`serial_no`)
) ENGINE=InnoDB AUTO_INCREMENT=24644 DEFAULT CHARSET=utf8 COMMENT='支付操作流水';

-- ----------------------------
-- Table structure for refund_order
-- ----------------------------
DROP TABLE IF EXISTS `refund_order`;
CREATE TABLE `refund_order` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(32) DEFAULT NULL COMMENT '真正成功时，同步成功退款流水中的批次号',
  `refund_no` varchar(32) DEFAULT NULL COMMENT '退款编号',
  `refund_serial_no` varchar(32) DEFAULT NULL COMMENT '退款流水',
  `mch_id` varchar(128) DEFAULT NULL COMMENT '商户号',
  `out_refund_no` varchar(32) DEFAULT NULL COMMENT '外部退款编号',
  `business_no` varchar(32) DEFAULT NULL COMMENT '业务编号',
  `business_type` varchar(64) DEFAULT NULL COMMENT '业务类型',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '通知业务系统连接',
  `notify_status` tinyint(1) DEFAULT NULL COMMENT '通知结果',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `refund_fee` int(10) DEFAULT NULL COMMENT '退款金额',
  `refund_account` varchar(128) DEFAULT NULL COMMENT '退款用户账号',
  `pay_no` varchar(32) DEFAULT NULL COMMENT '支付订单号',
  `pay_bill_no` varchar(32) DEFAULT NULL COMMENT '三方支付编号',
  `refund_bill_no` varchar(32) DEFAULT NULL COMMENT '三方退款编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_person` varchar(128) DEFAULT NULL COMMENT '创建人',
  `create_person` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `subject` varchar(255) DEFAULT NULL COMMENT '退款主题',
  `status` tinyint(1) DEFAULT '0' COMMENT '退款订单状态',
  `refund_channel` varchar(64) DEFAULT NULL COMMENT '退款渠道，原支付渠道',
  `refund_type` varchar(64) DEFAULT NULL COMMENT '原支付类型',
  `total_fee` int(10) DEFAULT NULL COMMENT '总费用',
  `partner` varchar(64) DEFAULT NULL COMMENT '退款订单来源',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `success_time` datetime DEFAULT NULL COMMENT '退款完成时间',
  `trade_no` varchar(32) DEFAULT NULL COMMENT '支付订单编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_bzno_pa` (`business_no`,`partner`),
  KEY `idx_payno` (`pay_no`),
  KEY `idx_bchno` (`batch_no`),
  KEY `idx_rno` (`refund_no`)
) ENGINE=InnoDB AUTO_INCREMENT=621 DEFAULT CHARSET=utf8 COMMENT='退款订单';

-- ----------------------------
-- Table structure for refund_serial
-- ----------------------------
DROP TABLE IF EXISTS `refund_serial`;
CREATE TABLE `refund_serial` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(32) DEFAULT NULL COMMENT '退款批次号',
  `serial_no` varchar(32) DEFAULT NULL COMMENT '退款流水号',
  `refund_no` varchar(32) DEFAULT NULL COMMENT '退款编号',
  `mch_id` varchar(64) DEFAULT NULL COMMENT '商户号',
  `refund_account` varchar(255) DEFAULT NULL COMMENT '用户退款账号',
  `refund_fee` int(10) DEFAULT NULL COMMENT '退款费用',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '通知外部系统地址',
  `refund_bill_no` varchar(64) DEFAULT NULL COMMENT '第三方退款编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `refund_channel` varchar(64) DEFAULT NULL COMMENT '退款渠道-原支付渠道',
  `refund_type` varchar(64) DEFAULT NULL COMMENT '原支付类型',
  `create_person` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_person` varchar(64) DEFAULT NULL COMMENT '更新者',
  `out_time` int(10) DEFAULT NULL COMMENT '超时时间-秒单位',
  `is_timeout` tinyint(1) DEFAULT NULL COMMENT '是否超时',
  `subject` varchar(255) DEFAULT NULL COMMENT '退款主题',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `status` tinyint(1) DEFAULT NULL COMMENT '流水状态',
  `success_time` datetime DEFAULT NULL COMMENT '退款完成时间',
  PRIMARY KEY (`id`),
  KEY `idx_sno` (`serial_no`)
) ENGINE=InnoDB AUTO_INCREMENT=629 DEFAULT CHARSET=utf8 COMMENT='退款操作流水';

-- ----------------------------
-- Table structure for verify_code_serial
-- ----------------------------
DROP TABLE IF EXISTS `verify_code_serial`;
CREATE TABLE `verify_code_serial` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `card_no` varchar(1024) NOT NULL COMMENT '卡号',
  `request_add_data` varchar(1024) DEFAULT NULL COMMENT '请求的附加域',
  `return_add_data` varchar(1024) DEFAULT NULL COMMENT '返回的附加域，00+12位手续费+3位错误代码+手机号后四位+2位验证代码序号',
  `trace_no` varchar(50) DEFAULT NULL COMMENT '流水号,商户自定义6位流水号(可重复提交)',
  `invioce_no` varchar(50) DEFAULT NULL COMMENT '票据号，6位批次号+6位凭证号每天唯一(不可重复提交，由0~9数字组成)',
  `user_id` varchar(50) DEFAULT 'system' COMMENT '用户Id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_person` varchar(50) DEFAULT 'system' COMMENT '创建人',
  `update_person` varchar(50) DEFAULT 'system' COMMENT '修改人',
  `pay_channel` varchar(64) DEFAULT NULL COMMENT '支付渠道',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=800 DEFAULT CHARSET=utf8 COMMENT='获取签约验证码流水信息表';
