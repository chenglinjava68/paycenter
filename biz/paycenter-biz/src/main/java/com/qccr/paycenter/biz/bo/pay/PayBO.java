package com.qccr.paycenter.biz.bo.pay;

import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;

public interface PayBO {
	
	void paySuccess(PayNotifySO notifySO);
	
	void payFail(PayNotifySO notifySO);
	
	boolean orderIsSuccess(String payNo);

}
