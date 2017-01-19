/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.dao.pay;

import com.qccr.paycenter.dal.domain.pay.PayCodeSerialDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayCodeSerialDao {
    /**
     *
     * 删除单条记录
     */
//    int delete(Long id);

    /**
     *
     * 增加单条记录
     */
    int insert(PayCodeSerialDO record);

    /**
     *
     * 查询记录
     */
//    List<PayCodeSerialDO> query(PayCodeSerialDO condition);

    /**
     *
     * 更新单条记录
     */
    int update(PayCodeSerialDO record);


    /**
     * 用票据号查询当天的记录
     * @param invioceNo
     * @return
     */
    PayCodeSerialDO findByInvioceNo(String invioceNo);

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
//    PayCodeSerialDO findLatestByCardNo(String cardNo);

    /**
     * 获取最新的一条记录
     * @param userId
     * @param payChannel
     * @return
     */
    PayCodeSerialDO findLatestByUserIdAndPayChannel(@Param(value = "userId") String userId, @Param(value = "payChannel") String payChannel);
}