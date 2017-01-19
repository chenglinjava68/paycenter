package com.qccr.paycenter.biz.task.schedule.pay;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.dataflow.AbstractIndividualThroughputDataFlowElasticJob;
import com.qccr.paycenter.biz.task.job.JobContext;
import com.qccr.paycenter.biz.task.job.timeout.PayTimeoutWorker;
import com.qccr.paycenter.model.dal.dbo.pay.PayTimeoutOrderQueryDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * 支付超时，定时任务定义
 * author: denghuajun
 * date: 2016/4/8 11:45
 * version: v1.1.0
 */
@Component
public class PayTimeoutSchedule extends AbstractIndividualThroughputDataFlowElasticJob<PayTimeoutOrderQueryDO> {

    @Resource
    private PayTimeoutWorker payTimeoutWorker;

    @Override
    public List<PayTimeoutOrderQueryDO> fetchData(JobExecutionMultipleShardingContext jobExecutionMultipleShardingContext) {
        if(!JobContext.getPayTimeoutOpen()){
            return Collections.emptyList();
        }
        return payTimeoutWorker.fetch(jobExecutionMultipleShardingContext.getShardingItems());
    }

    @Override
    public boolean isStreamingProcess() {
        return payTimeoutWorker.continued();
    }

    @Override
    public boolean processData(JobExecutionMultipleShardingContext jobExecutionMultipleShardingContext, PayTimeoutOrderQueryDO payOrderDO) {
        return payTimeoutWorker.process(payOrderDO);
    }
}
