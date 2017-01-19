package com.qccr.paycenter.biz.task.job;

import com.qccr.paycenter.model.config.PayCenterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * job 上下文
 * author: denghuajun
 * date: 2016/4/8 13:40
 * version: v1.0.0
 */
public class JobContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobContext.class);
    private JobContext() {
    }

    /**
     * 支付超时任务，任务数据抽取时间间隔
     * 600000毫秒=10分钟
     */
    public static final long PAY_OUT_INTERVAL = PayCenterConfig.getLong("pay_timeout_interval",10*60*1000L);
    /**
     * 10分钟
     */
    public static final long INTERVAL_MINUTE = PAY_OUT_INTERVAL/(60*1000);
    
    public static final long PAY_TIMEOUT_CANCEL_INTERVAL_MILLISECOND = PayCenterConfig.getLong("pay_timeout_cancel_interval",10*60*1000L);
    
    public static final long PAY_TIMEOUT_CENCEL_INTERVAL_MINUTE = PAY_TIMEOUT_CANCEL_INTERVAL_MILLISECOND/(60*1000);
    /**
     * 30分钟
     */
    public static final long PAY_OUT_BEFORE_MINUTES = INTERVAL_MINUTE *3 ;
    
    public static final long PAY_OUT_CANCEL_BEFORE_MINUTES = INTERVAL_MINUTE *3 ;
    /**
     * 5
     */
    public static final String DEFAULT_SHARDING_COUNT_NAME = "default.job.sharding.count";
    /**
     * 5
     */
    public static final Integer DEFAULT_SHARDING_COUNT = PayCenterConfig.getInteger(DEFAULT_SHARDING_COUNT_NAME,5);

    public static final Map<String,Integer> PAY_TIMEOUT_JOBS = new ConcurrentHashMap<>();
    /**
     * 超时开关
     */
//    public static final Boolean PAY_TIMEOUT_OPEN = PayCenterConfig.getBool("pay_timeout_open",false);
    /**
     * 当面付开关
     */
//    private static final Boolean FACE_PAY_TIMEOUT_OPEN = PayCenterConfig.getBool("face_pay_timeout_open",false);

    private  static  Date payOutNext;
    
    private static Date cancelTimeNext;

    /**
     * 支付宝的当面付的TOKEN刷新周期：30天
     */
    private static final String ALIPAY_FACEPAY_AUTH_TOKEN_EXPIRES_DAYS_NAME = "alipay_facepay_auth_token_expires_days";
    public static final Integer ALIPAY_FACEPAY_AUTH_TOKEN_EXPIRES_DAYS = PayCenterConfig.getInteger(ALIPAY_FACEPAY_AUTH_TOKEN_EXPIRES_DAYS_NAME,30);

    /**
     * 初始十分钟后
     */
    public static synchronized void initPayOutNext(){
        payOutNext = new Date(System.currentTimeMillis()+ JobContext.PAY_OUT_INTERVAL);
    }

    public static synchronized Date getPayOutNext() {
        if(payOutNext==null){
            payOutNext = new Date((System.currentTimeMillis()/1000/60/INTERVAL_MINUTE +1)*1000*60*INTERVAL_MINUTE);
        }
        return payOutNext;
    }

    public static void setPayOutNext(Date payOutNext) {
        JobContext.payOutNext = payOutNext;
    }
    
    public static synchronized void initCancelNext(){
    	cancelTimeNext = new Date(System.currentTimeMillis()+ JobContext.PAY_TIMEOUT_CANCEL_INTERVAL_MILLISECOND);
    }

    public static synchronized Date getCancelTimeNext() {
        if(cancelTimeNext==null){
        	cancelTimeNext = new Date((System.currentTimeMillis()/1000/60/PAY_TIMEOUT_CENCEL_INTERVAL_MINUTE +1)*1000*60*PAY_TIMEOUT_CENCEL_INTERVAL_MINUTE);
        }
        return cancelTimeNext;
    }

    public static void setCancelTimeNext(Date cancelTimeNext) {
        JobContext.cancelTimeNext = cancelTimeNext;
    }

    /**
     * 当面付开关
     * #0false,1true
     */
    public static Boolean getFacePayTimeoutOpen() {
        int opened = PayCenterConfig.getInteger("face_pay_timeout_open",0);
        boolean bool = false;
        if(opened == 1) {
            bool = true;
        }
//        LOGGER.info("face_pay_timeout_open:"+bool);
        return bool;
    }

    /**
     * 超时开关
     * @return
     */
    public static Boolean getPayTimeoutOpen() {
        int opened = PayCenterConfig.getInteger("pay_timeout_open",0);
        boolean bool = false;
        if(opened == 1) {
            bool = true;
        }
//        LOGGER.info("pay_timeout_open:"+bool);
        return bool;
    }
}
