/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.dao.auth;

import com.qccr.paycenter.dal.domain.auth.FacepayAuthDO;
import com.qccr.paycenter.model.dal.dbo.pay.AuthTokenRefreshQueryDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FacepayAuthDao {
    /**
     *
     * 删除单条记录
     */
    int delete(Long id);

    /**
     *
     * 增加单条记录
     */
    int insert(FacepayAuthDO record);

    /**
     *
     * 查询记录
     */
    List<FacepayAuthDO> query(FacepayAuthDO condition);

    /**
     *
     * 更新单条记录
     */
    int update(FacepayAuthDO record);

    /**
     * userId查新建或者已授权记录
     * @param superUserId
     * @return
     */
    FacepayAuthDO findBySuperUserId(Long superUserId);

    /**
     * 更新状态为过期
     * @param superUserId
     * @return
     */
    int updateStatusBySuperUserId(Long superUserId);

    /**
     * 更新
     * @param map
     * @return
     */
    int updateBySuperUserId(Map map);

    /**
     * userId查已经授权的记录
     * @param superUserId
     * @return
     */
    FacepayAuthDO findNormalBySuperUserId(Long superUserId);
    /**
     * userId查未授权或者已经过期的记录
     * @param superUserId
     * @return
     */
    FacepayAuthDO findOneBySuperUserId(Long superUserId);
    /**
     * 查询最早的即将过期的TOKEN
     * @param shardingItems
     * @param days
     * @param shardingCount
     * @return
     */
    List<AuthTokenRefreshQueryDO> queryNextTimeoutTokens(@Param(value = "shardingItems") List<Integer> shardingItems, @Param(value = "days") Integer days, @Param(value = "shardingCount") Integer shardingCount);
}