package com.qccr.paycenter.biz.task.schedule.pay;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.dataflow.AbstractIndividualThroughputDataFlowElasticJob;
import com.qccr.paycenter.biz.task.job.timeout.AuthTokenRefreshWorker;
import com.qccr.paycenter.model.dal.dbo.pay.AuthTokenRefreshQueryDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by user on 2016/11/21.
 */
@Component
public class AuthTokenRefreshSchedule extends AbstractIndividualThroughputDataFlowElasticJob<AuthTokenRefreshQueryDO> {

    @Resource
    private AuthTokenRefreshWorker authTokenRefreshWorker;

    @Override
    public List<AuthTokenRefreshQueryDO> fetchData(JobExecutionMultipleShardingContext jobExecutionMultipleShardingContext) {
        return authTokenRefreshWorker.fetch(jobExecutionMultipleShardingContext.getShardingItems());
    }

    @Override
    public boolean isStreamingProcess() {
        return authTokenRefreshWorker.continued();
    }

    @Override
    public boolean processData(JobExecutionMultipleShardingContext jobExecutionMultipleShardingContext, AuthTokenRefreshQueryDO payOrderDO) {
        return authTokenRefreshWorker.process(payOrderDO);
    }
}
