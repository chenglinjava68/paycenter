/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.dao.pay;

import com.qccr.paycenter.dal.domain.pay.VerifyCodeSerialDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VerifyCodeSerialDao {
    /**
     *
     * 删除单条记录
     */
//    int delete(Long id);

    /**
     *
     * 增加单条记录
     */
    int insert(VerifyCodeSerialDO record);

    /**
     *
     * 查询记录
     */
//    List<VerifyCodeSerialDO> query(VerifyCodeSerialDO condition);

    /**
     *
     * 更新单条记录
     */
    int update(VerifyCodeSerialDO record);

    /**
     * 用票据号查询当天的记录
     * @param invioceNo
     * @return
     */
    VerifyCodeSerialDO findByInvioceNo(String invioceNo);

    /**
     * 更新返回的附加域值
     * @param returnAddData
     * @param invioceNo
     * @return
     */
    int addReturnAddData(@Param(value = "returnAddData") String returnAddData, @Param(value = "invioceNo") String invioceNo);

    /**
     * 获取最新的一条记录
     * @param cardNo
     * @return
     */
//    VerifyCodeSerialDO findLatestByCardNo(String cardNo);

    /**
     * 获取最新的一条记录
     * @param userId
     * @param payChannel
     * @return
     */
    VerifyCodeSerialDO findLatestByUserIdAndPayChannel(@Param(value = "userId") String userId, @Param(value = "payChannel") String payChannel);
}