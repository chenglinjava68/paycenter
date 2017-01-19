package com.qccr.paycenter.biz.service.pay;

import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;

import javax.servlet.http.HttpServletRequest;

public interface PayNotifyService {
	
	public PayNotifySO payNotify(String payChannel,String payType,HttpServletRequest request,String partnre);

}
