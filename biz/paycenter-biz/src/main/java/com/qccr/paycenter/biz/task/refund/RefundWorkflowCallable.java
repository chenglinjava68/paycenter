package com.qccr.paycenter.biz.task.refund;

import com.qccr.paycenter.biz.service.refund.workflow.RefundWorkflow;
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
public class RefundWorkflowCallable implements Callable<RefundResultSO>{

	protected Logger logger = LoggerFactory.getLogger(RefundWorkflowCallable.class);

	private RefundWorkflow refundWorkflow ;

	private RefundParamSO paramSO;

	private RefundTrackService refundTrackService;

	public RefundWorkflowCallable(RefundWorkflow refundWorkflow, RefundParamSO paramSO, RefundTrackService refundTrackService) {
		super();
		this.refundWorkflow = refundWorkflow;
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
		return  refundWorkflow.doRefund(paramSO);
	}
	
}
