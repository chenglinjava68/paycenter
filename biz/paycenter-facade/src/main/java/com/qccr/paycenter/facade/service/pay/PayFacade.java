/*
 * PayFacade.java
 *
 * Copyright, (c) 2014-2016-09-19 qccr.inc
 *
 * All right reserved
 */

package com.qccr.paycenter.facade.service.pay;

import java.util.List;

import com.qccr.knife.result.Result;
import com.qccr.paycenter.facade.dal.ro.CommonResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayOrderRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayParamRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayVerifyCodeParamRO;

/**
 * 支付相关的接口都在这个接口里面
 *
 * @name 支付入口
 * @author denghuajun
 * @version $Id: PayFacade, v 1.1.9 2015年12月17日 下午3:39:00 denghuajun Exp $
 */
public interface PayFacade {

    /**
	 * 通过业务单号和调用方来查询支付订单
	 *
     * @name 查询支付订单
     * @param businessNo 业务单号
     * @param partner 调用方
     * @return 返回支付单信息
	 * @author denghuajun
	 * @date 2015年12月17日 下午3:39:00
     */
    Result<PayOrderRO> checkByBusinessNo(String businessNo, String partner);

    /**
	 * 接收关闭请求参数，返回关闭受理结果
	 *
     * @name 接收关闭请求参数，返回关闭受理结果
	 * @param businessNo 业务单号
	 * @param partner 调用方
     * @return 返回关闭受理结果
	 * @author denghuajun
	 * @date 2016年06月17日 下午3:39:00
     */
    Result<CommonResultRO> close(String businessNo, String partner);

    /**
	 * 标记订单是线下汇款
	 *
     * @name 标记订单是线下汇款
     * @param propertiesRO
     * @return 返回标记受理结果
	 * @author lim
	 * @date 2016年08月17日 下午3:39:00
     */
    Result<CommonResultRO> markOfflinePay(PayParamRO propertiesRO);

    /**
	 * 生成支付链接
	 *
     * @name 生成支付链接
     * @param paramRO 预支付参数
     * @return 返回支付结果
	 * @author denghuajun
	 * @date 2015年12月17日 下午3:39:00
     */
    Result<PayResultRO> pay(PayParamRO paramRO);

    /**
     * 消费获取验证码
     * 5.1  获取手机验证代码——快捷支付银行生成消费时使用的动态验证码
     * 使用于交通银行
	 * @name 消费获取验证码(目前使用于交通银行)
     * @param propertiesRO 获取验证码的组装对象
     * @return 返回受理结果
	 * @author lim
	 * @date 2016年08月17日 下午3:39:00
     */
    Result<CommonResultRO> payVerifyCode(PayVerifyCodeParamRO propertiesRO);

    /**
     * 查询支付流水接口(paycenter)1 供订单中心调用，卡券订单记录由php请求订单中心，
     * 然后订单中心来支付中心请求支付流水数据，由业务单号查所有的支付流水记录
	 * @name 查询支付流水接口
     * @param businessNo 业务单号
     * @return 返回查询的支付单列表
	 * @author denghuajun
	 * @date 2015年12月17日 下午3:39:00
     */
    Result<List<PayOrderRO>> queryByBusinessNo(String businessNo);
}
//~ Formatted by Jindent --- http://www.jindent.com
