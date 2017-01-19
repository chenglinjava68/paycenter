package com.qccr.paycenter.biz.task.job.timeout;

import com.qccr.paycenter.biz.service.pay.PayTimeOutService;
import com.qccr.paycenter.biz.task.job.ItemsWorker;
import com.qccr.paycenter.biz.task.job.JobContext;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.dao.pay.PayOrderDao;
import com.qccr.paycenter.model.dal.dbo.pay.PayTimeoutOrderQueryDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用于支付超时任务作业,
 * 每10分钟查询0-创建,4-进行中的支付订单。
 * author: denghuajun
 * date: 2016/4/8 11:06
 * version: v1.0.0
 */
@Component
public class PayTimeoutWorker extends ItemsWorker<PayTimeoutOrderQueryDO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayTimeoutWorker.class);

	@Resource
	private PayOrderDao payOrderDao;

    @Resource
    private PayTimeOutService payTimeOutService;

    @Override
    public List<PayTimeoutOrderQueryDO> fetch(List<Integer> integers) {
        /*将当前线程池情况，输出日志*/
        LogUtil.info(LOGGER, "PAY_TIMEOUT_JOBS,job piled count:"+JobContext.PAY_TIMEOUT_JOBS.size());
        ExecutorUtil.healthydetection();
        /*设置下次拉取截止时间*/
        JobContext.initPayOutNext();
        return payOrderDao.queryNextTimeoutOrders(integers, JobContext.getPayOutNext(),JobContext.PAY_OUT_BEFORE_MINUTES,JobContext.DEFAULT_SHARDING_COUNT);
    }

    @Override
    public boolean process(PayTimeoutOrderQueryDO order) {
        payTimeOutService.pushJob(order.getOutTime(),order.getPayNo(), order.getPayChannel(), order.getPayType());
        return true;
    }
}
