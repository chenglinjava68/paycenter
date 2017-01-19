package com.qccr.paycenter.biz.task.job.timeout;

import java.util.List;

import javax.annotation.Resource;

import com.qccr.paycenter.biz.service.pay.AuthTokenRefreshService;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.dal.dao.auth.FacepayAuthDao;
import com.qccr.paycenter.model.dal.dbo.pay.AuthTokenRefreshQueryDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qccr.paycenter.biz.task.job.ItemsWorker;
import com.qccr.paycenter.biz.task.job.JobContext;

/**
 * 用于刷新即将过期TOKEN的任务作业,
 * 每天查询超过#{days}的未过期TOKEN。
 * author: denghuajun
 * date: 2016/4/8 11:06
 * version: v1.0.0
 */
@Component
public class AuthTokenRefreshWorker extends ItemsWorker<AuthTokenRefreshQueryDO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenRefreshWorker.class);

	@Resource
	private FacepayAuthDao facepayAuthDao;

    @Resource
    private AuthTokenRefreshService authTokenRefreshService;

    /**
     * 拉取记录
     * @param integers
     * @return
     */
    @Override
    public List<AuthTokenRefreshQueryDO> fetch(List<Integer> integers) {
        List<AuthTokenRefreshQueryDO> list = facepayAuthDao.queryNextTimeoutTokens(integers, JobContext.ALIPAY_FACEPAY_AUTH_TOKEN_EXPIRES_DAYS, JobContext.DEFAULT_SHARDING_COUNT);
        LOGGER.info("AuthTokenRefreshWorker.fetch():"+list.size());
        return list;
    }

    /**
     * 处理记录
     * @param authTokenRefreshQueryDO
     * @return
     */
    @Override
    public boolean process(AuthTokenRefreshQueryDO authTokenRefreshQueryDO) {
        LOGGER.info("AuthTokenRefreshWorker.process():"+authTokenRefreshQueryDO.toString());
        authTokenRefreshService.doRefresh(authTokenRefreshQueryDO.getSuperUserId(), authTokenRefreshQueryDO.getAppRefreshToken(), authTokenRefreshQueryDO.getChannel());
        return true;
    }
}
