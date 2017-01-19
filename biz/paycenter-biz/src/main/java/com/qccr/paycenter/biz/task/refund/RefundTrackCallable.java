package com.qccr.paycenter.biz.task.refund;

import java.util.concurrent.Callable;

import com.qccr.paycenter.biz.service.track.RefundTrackService;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;

public class RefundTrackCallable implements Callable{
	
	
	private RefundResultSO resultSO ;
	
	private RefundParamSO paramSO;
	
	private RefundTrackService refundTrackService;

	

	public RefundTrackCallable(RefundResultSO resultSO, RefundParamSO paramSO, RefundTrackService refundTrackService) {
		super();
		this.resultSO = resultSO;
		this.paramSO = paramSO;
		this.refundTrackService = refundTrackService;
	}



	@Override
	public Object call() throws Exception {
		refundTrackService.track(paramSO, resultSO);
		return null;
	}

}
