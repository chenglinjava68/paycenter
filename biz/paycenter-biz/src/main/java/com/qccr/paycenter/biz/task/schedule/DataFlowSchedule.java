package com.qccr.paycenter.biz.task.schedule;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.api.JobScheduler;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 数据流式作业
 * author: denghuajun
 * date: 2016/4/15 11:11
 * version: v1.1.0
 * desc:此处定义一些开关类型参数,在使用非配置类型作业时，便于任务配置改变
 */
public class DataFlowSchedule extends JobScheduler implements ApplicationContextAware{

    private boolean jobOpen = true;

    private String jobCron = null;

    private Integer jobShardingCount = 5;

    private String jobShardingItems = "0=A,1=B,2=B,3=C,4=D";

    private Integer jobDelay = 0;

    public DataFlowSchedule(CoordinatorRegistryCenter regCenter, JobConfiguration jobConfig) {
        super(regCenter, jobConfig);
    }

    @Override
    public void init() {
        if(jobOpen){
            super.init();
        }
    }

    public boolean isJobOpen() {
        return jobOpen;
    }

    public void setJobOpen(boolean jobOpen) {
        this.jobOpen = jobOpen;
    }

    public String getJobCron() {
        return jobCron;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron;
    }

    public Integer getJobShardingCount() {
        return jobShardingCount;
    }

    public void setJobShardingCount(Integer jobShardingCount) {
        this.jobShardingCount = jobShardingCount;
    }

    public String getJobShardingItems() {
        return jobShardingItems;
    }

    public void setJobShardingItems(String jobShardingItems) {
        this.jobShardingItems = jobShardingItems;
    }

    public Integer getJobDelay() {
        return jobDelay;
    }

    public void setJobDelay(Integer jobDelay) {
        this.jobDelay = jobDelay;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        init();
    }
}
