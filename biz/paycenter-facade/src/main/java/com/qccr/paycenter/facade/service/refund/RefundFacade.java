/*
 * RefundFacade.java
 *
 * Copyright, (c) 2014-2016-09-19 qccr.inc
 *
 * All right reserved
 */

package com.qccr.paycenter.facade.service.refund;

import com.qccr.knife.result.Result;
import com.qccr.paycenter.facade.dal.ro.refund.RefundOrderRO;
import com.qccr.paycenter.facade.dal.ro.refund.RefundParamRO;
import com.qccr.paycenter.facade.dal.ro.refund.RefundResultRO;

/**
 * 退款相关功能
 *
 * @name 退款入口
 * @version  1.1.9
 * @author denghuajun
 * @date 2015年12月17日 下午3:40:26
 */
public interface RefundFacade {

    /**
     * 通过支付单号和调用方查询退款单
     *
     * @name 查询退款单
     * @param payNo 支付单号
     * @param partner 调用方
     * @return 返回退款单
     * @author denghuajun
     * @date   2016-09-19
     */
    Result<RefundOrderRO> checkByPayNo(String payNo, String partner);

    /**
     * 接收退款请求参数，返回退款受理结果，异步通知退款真实结果
	 * @name 退款
     * @param paramRO 退款参数组装对象
     * @return 返回退款受理结果
	 * @author denghuajun
	 * @date 2015年12月17日 下午3:39:00
     */
    Result<RefundResultRO> refund(RefundParamRO paramRO);
}
//~ Formatted by Jindent --- http://www.jindent.com
