/*
 * SignFacade.java
 *
 * Copyright, (c) 2014-2016-09-19 qccr.inc
 *
 * All right reserved
 */

package com.qccr.paycenter.facade.service.pay;

import com.qccr.knife.result.Result;
import com.qccr.paycenter.facade.dal.ro.CommonResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.SignParamRO;
import com.qccr.paycenter.facade.dal.ro.pay.SignVerifyCodeParamRO;

/**
 * 签约相关，目前是交行的签约
 *
 * @name 签约相关入口
 * @author lim
 * @version $Id: SignFacade, v 1.1.9 2016年08月19日 下午3:39:00 lim Exp $
 */
public interface SignFacade {

    /**
     * 签约---目前用于交通银行
     *
     * @name 签约
     * @param propertiesRO 签约参数组装类
     * @return 返回受理结果
     * @author lim
     * @date 2016年08月19日 下午3:39:00
     */
    Result<CommonResultRO> sign(SignParamRO propertiesRO);

    /**
     * 签约获取验证码，使用于交通银行
     * @name 获取签约验证码
     * @param propertiesRO 签约验证码参数组装类
     * @return 返回受理结果
     * @author lim
     * @date 2016年08月19日 下午3:39:00
     */
    Result<CommonResultRO> signVerifyCode(SignVerifyCodeParamRO propertiesRO);

    /**
     * 判断是否已经签约
     * @name 判断是否已经签约
     * @param userId 签约用户ID
     * @param payChannel 支付渠道(1支付宝，2微信，8银联，10浙商，12线下，13交行)，暂时支持交行
     * @return 返回受理结果
     * @author lim
     * @date 2016年08月19日 下午3:39:00
     */
    Result<CommonResultRO> isSigned(String userId, int payChannel);
}
//~ Formatted by Jindent --- http://www.jindent.com
