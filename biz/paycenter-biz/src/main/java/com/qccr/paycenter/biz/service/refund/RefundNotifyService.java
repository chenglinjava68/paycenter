package com.qccr.paycenter.biz.service.refund;

import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;

import javax.servlet.http.HttpServletRequest;

public interface RefundNotifyService {
	
	public RefundNotifySO refundNotify(String payChannel,String payType,HttpServletRequest request,String partnre);

}
