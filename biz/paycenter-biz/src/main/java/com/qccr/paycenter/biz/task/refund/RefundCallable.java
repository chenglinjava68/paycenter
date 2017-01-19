package com.qccr.paycenter.biz.task.refund;

import com.qccr.paycenter.biz.service.refund.RefundService;
import com.qccr.paycenter.biz.service.track.RefundTrackService;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
/**
 * 退款工作线程
 * @author denghuajun
 * date:2015年12月17日 下午3:45:49
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class RefundCallable implements Callable<RefundResultSO>{

	protected Logger logger = LoggerFactory.getLogger(RefundCallable.class);

	private RefundService refundService ;
	
	private RefundParamSO paramSO;
	
	private RefundTrackService refundTrackService;
	
	public RefundCallable(RefundService refundService, RefundParamSO paramSO,RefundTrackService refundTrackService) {
		super();
		this.refundService = refundService;
		this.paramSO = paramSO;
		this.refundTrackService = refundTrackService;
	}

	
	@Override
	public RefundResultSO call() throws Exception {
		RefundResultSO resultSO = doRefund(paramSO);
		ExecutorUtil.submit(new RefundTrackCallable(resultSO,paramSO,refundTrackService));
		return resultSO;
		
	}

	public RefundResultSO doRefund(RefundParamSO paramSO) throws Exception{		
		return  refundService.doRefund(paramSO);
	}
	
}
