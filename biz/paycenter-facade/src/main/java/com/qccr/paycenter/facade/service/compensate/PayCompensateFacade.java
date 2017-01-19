/*
 * PayCompensateFacade.java
 *
 * Copyright, (c) 2014-2016-09-19 qccr.inc
 *
 * All right reserved
 */

package com.qccr.paycenter.facade.service.compensate;

import com.qccr.knife.result.Result;
import com.qccr.paycenter.facade.dal.ro.PagedDataRO;
import com.qccr.paycenter.facade.dal.ro.compensate.PayCompensateParamRO;
import com.qccr.paycenter.facade.dal.ro.compensate.PayCompensateQueryRO;
import com.qccr.paycenter.facade.dal.ro.compensate.PayCompensateRO;
import com.qccr.paycenter.facade.dal.ro.compensate.PayCompensateResultRO;

/**
 * 补偿相关的接口都在这个接口里面
 *
 * @name 补偿入口
 * @author denghuajun
 * @version $Id: PayFacade, v 1.1.9 2016/3/5 16:01 denghuajun Exp $
 */
public interface PayCompensateFacade {

    /**
     * 根据第三方流水，查询当前第三方渠道的补偿
     * @name 查询当前第三方渠道的补偿
     * @deprecated (目前没有使用)
     * @param billNo 第三方单号
     * @return 返回补偿对象
     * @author denghuajun
     * @date 2016/3/5 16:01
     */
    @Deprecated
    public Result<PayCompensateRO> findPayCompensateByBillNo(String billNo);

    /**
     * 获取补偿列表
     * @name 获取补偿列表
     * @deprecated (目前没有使用)
     * @param payCompensateQueryRO 获取补偿参数组装对象
     * @return 分页返回补偿记录
     * @author denghuajun
     * @date 2016/3/5 16:01
     */
    @Deprecated
    public Result<PagedDataRO<PayCompensateRO>> findPayCompensates(PayCompensateQueryRO payCompensateQueryRO);

    /**
     * 根据业务订单编号，获取补偿列表
     * @name 根据业务订单编号，获取补偿列表
     * @deprecated (目前没有使用)
     * @param bizNo 业务单号
     * @return 分页返回补偿记录
     * @author denghuajun
     * @date 2016/3/5 16:01
     */
    @Deprecated
    public Result<PagedDataRO<PayCompensateRO>> findPayCompensatesByBizNo(String bizNo);

    /**
     * 发起补偿退款
     * @name 发起补偿退款
     * @param paramRO 补偿退款的参数组装
     * @return 返回补偿退款受理结果
     * @author denghuajun
     * @date 2016/3/5 16:01
     */
    public Result<PayCompensateResultRO> refund(PayCompensateParamRO paramRO);

    /**
     * 使用补偿订单号退款
     * @name 使用补偿订单号退款
     * @param compensteNo 补偿订单号
     * @param sign 补偿订单号md5签名
     * @return 返回补偿退款受理结果
     * @author denghuajun
     * @date 2016/3/5 16:01
     */
    public Result<PayCompensateResultRO> refund(String compensteNo, String sign);
}
//~ Formatted by Jindent --- http://www.jindent.com
