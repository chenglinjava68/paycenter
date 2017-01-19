package com.qccr.paycenter.biz.third.bocom.core;

import com.qccr.paycenter.biz.third.PayProcess;
import com.qccr.paycenter.biz.third.bocom.model.BocomCancelParam;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.bocom.BocomCancelParamSO;

/**
 * 交通银行信用卡对接
 * Created by lim on 2016/8/8.
 */
public interface BocomPayProcess extends PayProcess {

    /**
     * 交易撤销，当日
     * @param param
     * @return
     */
    RefundResultSO cancel(BocomCancelParamSO param);

}
