package com.qccr.paycenter.biz.task.schedule.refund;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.dataflow.AbstractIndividualThroughputDataFlowElasticJob;
import com.qccr.paycenter.biz.task.job.ItemsWorker;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class RefundCheckBackLowAmountSchedule extends AbstractIndividualThroughputDataFlowElasticJob<RefundCheckBackSO> {
	@Resource
	private ItemsWorker<RefundCheckBackSO> refundCheckBackLowAmountWorker;

	@Override
	public boolean processData(JobExecutionMultipleShardingContext shardingContext, RefundCheckBackSO data) {
		return refundCheckBackLowAmountWorker.process(data);
	}

	@Override
	public List<RefundCheckBackSO> fetchData(JobExecutionMultipleShardingContext shardingContext) {
		return refundCheckBackLowAmountWorker.fetch(shardingContext.getShardingItems());
	}

	@Override
	public boolean isStreamingProcess() {
		return refundCheckBackLowAmountWorker.continued();
	}

}
