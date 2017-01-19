package com.qccr.paycenter.biz.service.pay.imp;

import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.pay.FacePayService;
import com.qccr.paycenter.biz.service.pay.PayTimeOutService;
import com.qccr.paycenter.biz.task.job.JobContext;
import com.qccr.paycenter.biz.task.pay.PayTimeoutCallable;
import com.qccr.paycenter.biz.task.pay.PayTimeoutTrigger;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.DateUtil;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.dao.pay.PayOrderDao;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * author: denghuajun
 * date: 2016/4/11 16:36
 * version: v1.0.0
 */
@Service
public class PayTimeOutServiceImpl implements PayTimeOutService{

    private static final Logger LOGGER = LoggerFactory.getLogger(PayTimeOutServiceImpl.class);

    @Resource
    private PayOrderDao payOrderDao;

    @Resource
    private ReentrantTransactionBO reentrantTransactionBO;

    @Resource
    private FacePayService facePayService;

    @Override
    public boolean overtimeClose(final Date outTime,final String payNo, String payChannel, String payType) {
        if(PayCenterConstants.ALIPAY_CALLBACK_CHANNEL.equals(payChannel) && PayConstants.ALI_PAY_FACEPAY.equals(payType)) {
            facePayService.cancelTradeByAuto(payNo, payChannel);
        }else {
            Integer row = reentrantTransactionBO.execute(new TransactionCallback<Integer>() {
                @Override
                public Integer doTransaction() {
                    return payOrderDao.overtimeClose(payNo);
                }
            });
            if (row > 0) {
                LogUtil.info(LOGGER, "execute pay timeout close,the {payNo:" + payNo + ",outTime:" + DateUtil.format2Second(outTime) + "}");
            }
        }
        return false;
    }

    @Override
    public boolean pushJob(Date outTime, String payNo, String payChannel, String payType) {
        Long delay =  (outTime.getTime()-System.currentTimeMillis());
        if(delay<1000){
            LogUtil.info(LOGGER, "immediately excute pay timeout work，add PayTimeoutTrigger in Executor,{order.payNo: "+ payNo+",outTime:"+outTime+"}");
            PayTimeoutCallable payTimeoutCallable = new PayTimeoutCallable(payNo,outTime, payChannel, payType, this);
            ExecutorUtil.submit(payTimeoutCallable);
            return true;
        }
        /*判断本地容器是否存在当前任务*/
        if(JobContext.PAY_TIMEOUT_JOBS.containsKey(payNo)){
            LogUtil.info(LOGGER, "exists pay timeout work,{order.payNo: "+ payNo+",outTime:"+outTime+"}");
            return false;
        }
        /*本地容器，加入任务声明*/
        JobContext.PAY_TIMEOUT_JOBS.put(payNo,1);//这个1是没意义的
        LogUtil.info(LOGGER, "delay excute pay timeout work，add PayTimeoutTrigger in scheduleExecutor ,{order.payNo: "+ payNo+",outTime:"+outTime+"}");
        PayTimeoutTrigger trigger = new PayTimeoutTrigger(payNo,outTime, payChannel, payType,this);
        ExecutorUtil.schedule(trigger, delay, TimeUnit.MILLISECONDS);
        return false;
    }
}
