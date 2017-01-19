package com.qccr.paycenter.biz.task.job.timeout;

import java.util.List;

import com.qccr.paycenter.biz.service.pay.PayTimeOutService;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.facade.constants.PayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qccr.paycenter.biz.service.pay.FacePayService;
import com.qccr.paycenter.biz.task.job.ItemsWorker;
import com.qccr.paycenter.biz.task.job.JobContext;
import com.qccr.paycenter.dal.dao.pay.PayOrderDao;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.dal.dbo.pay.PayCancelOrderQueryDO;

import javax.annotation.Resource;

@Component
public class PayCancelWorker  extends ItemsWorker<PayCancelOrderQueryDO>{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PayCancelWorker.class);
	
	@Autowired
	private PayOrderDao payOrderDao;
	
	@Autowired
	private FacePayService facePayService;

	@Resource
	private PayTimeOutService payTimeOutService;

    @Override
    public List<PayCancelOrderQueryDO> fetch(List<Integer> integers) {
		/*将当前线程池情况，输出日志*/
		LOGGER.info("PAY_TIMEOUT_JOBS,job piled count:"+JobContext.PAY_TIMEOUT_JOBS.size());
		ExecutorUtil.healthydetection();
		JobContext.initCancelNext();
    	List<PayCancelOrderQueryDO> list= payOrderDao.queryNextCancelFacePayOrders(integers, JobContext.getCancelTimeNext(),JobContext.PAY_OUT_CANCEL_BEFORE_MINUTES,JobContext.DEFAULT_SHARDING_COUNT);
    	LOGGER.info("fetch counts:"+list.size());
    	return list;
    }

    @Override
    public boolean process(PayCancelOrderQueryDO order) {
    	LOGGER.info("process data:"+order.toString());
//    	facePayService.cancelTrade(order.getUserId(), order.getPayNo(),PayCenterConstants.ALIPAY_CALLBACK_CHANNEL);
		payTimeOutService.pushJob(order.getOutTime(),order.getPayNo(), PayCenterConstants.ALIPAY_CALLBACK_CHANNEL, PayConstants.ALI_PAY_FACEPAY);

		return true;
    }
}
