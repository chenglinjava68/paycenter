/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.dao.auth.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.qccr.paycenter.dal.dao.auth.FacepayAuthDao;
import com.qccr.paycenter.dal.domain.auth.FacepayAuthDO;
import com.qccr.paycenter.dal.mapper.auth.FacepayAuthMapper;
import com.qccr.paycenter.model.dal.dbo.pay.AuthTokenRefreshQueryDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("facepayAuthDao")
public class FacepayAuthDaoImpl implements FacepayAuthDao {
    @Resource
    private FacepayAuthMapper facepayAuthMapper;

    /**
     * 删除单条记录
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return facepayAuthMapper.delete(id);
    }

    /**
     * 增加单条记录
     *
     * @param record
     */
    @Override
    public int insert(FacepayAuthDO record) {
        return facepayAuthMapper.insert(record);
    }

    /**
     * 查询记录
     *
     * @param condition
     */
    @Override
    public List<FacepayAuthDO> query(FacepayAuthDO condition) {
        return facepayAuthMapper.query(condition);
    }

    /**
     * 更新单条记录
     *
     * @param record
     */
    @Override
    public int update(FacepayAuthDO record) {
        return facepayAuthMapper.update(record);
    }

    /**
     * userId查授权记录
     *
     * @param superUserId
     * @return
     */
    @Override
    public FacepayAuthDO findBySuperUserId(Long superUserId) {
        return facepayAuthMapper.findBySuperUserId(superUserId);
    }

    /**
     * 更新状态为过期
     *
     * @param superUserId
     * @return
     */
    @Override
    public int updateStatusBySuperUserId(Long superUserId) {
        return facepayAuthMapper.updateStatusBySuperUserId(superUserId);
    }

    /**
     * 更新
     *
     * @param map
     * @return
     */
    @Override
    public int updateBySuperUserId(Map map) {
        return facepayAuthMapper.updateBySuperUserId(map);
    }

    /**
     * userId查已经授权的记录
     *
     * @param superUserId
     * @return
     */
    @Override
    public FacepayAuthDO findNormalBySuperUserId(Long superUserId) {
        return facepayAuthMapper.findNormalBySuperUserId(superUserId);
    }

    /**
     * 查询最早的即将过期的TOKEN
     *
     * @param shardingItems
     * @param days
     * @param shardingCount
     * @return
     */
    @Override
    public List<AuthTokenRefreshQueryDO> queryNextTimeoutTokens(@Param(value = "shardingItems") List<Integer> shardingItems, @Param(value = "days") Integer days, @Param(value = "shardingCount") Integer shardingCount) {
        return facepayAuthMapper.queryNextTimeoutTokens(shardingItems, days, shardingCount);
    }

    /**
     * userId查未授权或者已经过期的记录
     *
     * @param superUserId
     * @return
     */
    @Override
    public FacepayAuthDO findOneBySuperUserId(Long superUserId) {
        return facepayAuthMapper.findOneBySuperUserId(superUserId);
    }
}