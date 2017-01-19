package com.qccr.paycenter.biz.task.schedule.pay;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import com.qccr.paycenter.biz.task.job.JobContext;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.dataflow.AbstractIndividualThroughputDataFlowElasticJob;
import com.qccr.paycenter.biz.task.job.timeout.PayCancelWorker;
import com.qccr.paycenter.model.dal.dbo.pay.PayCancelOrderQueryDO;

/**
 * 支付撤销
 * 
 * @author pengdc
 * 
 */
@Component
public class PayCancelShedule extends AbstractIndividualThroughputDataFlowElasticJob<PayCancelOrderQueryDO> {
	
	@Resource
	private PayCancelWorker payCancelWorker;
	
	@Override
	public boolean processData(JobExecutionMultipleShardingContext arg0, PayCancelOrderQueryDO queryDO) {
		return payCancelWorker.process(queryDO);
	}

	@Override
	public List<PayCancelOrderQueryDO> fetchData(JobExecutionMultipleShardingContext arg0) {
		if(!JobContext.getFacePayTimeoutOpen()){
			return Collections.emptyList();
		}
		return payCancelWorker.fetch(arg0.getShardingItems());
	}

	@Override
	public boolean isStreamingProcess() {
		return false;
	}

}
