package com.qccr.paycenter.biz.bo.refund;

import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;

public interface RefundBO {
	
	public void refundSuccess(RefundNotifySO notifySO);
	
	public void refundFail(RefundNotifySO notifySO);
	
	public boolean orderIsSuccess(String payNo);

}
