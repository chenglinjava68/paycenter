package com.qccr.paycenter.biz.task.job;

import com.dangdang.ddframe.job.api.JobScheduler;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperRegistryCenter;
import com.qccr.paycenter.model.config.PayCenterConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 分布式定时任务管理
 * author: denghuajun
 * date: 2016/4/15 10:45
 * version: v1.1.0
 */
public class JobManager implements ApplicationContextAware {

    private String server = PayCenterConfig.getValue("zk.url","conn.zookeeperregistry.com:2181");

    private String namespace = PayCenterConfig.getValue("app_name","paycenter")+"_job";

    private ZookeeperConfiguration  zkConfig = new ZookeeperConfiguration(server, namespace, 1000, 3000, 3);

    private CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zkConfig);

    private AtomicBoolean inited = new AtomicBoolean(false);

    private List<JobScheduler> activeJobs = new ArrayList<JobScheduler>();

    private ApplicationContext applicationContext;


    /**
     * 任务初始化
     */
    private void init(){
//        Map<String,DataFlowSchedule> flowScheduleMap = applicationContext.getBeansOfType(DataFlowSchedule.class);
//        for (Map.Entry<String, DataFlowSchedule> entry : flowScheduleMap.entrySet()) {
//            DataFlowSchedule schedule = entry.getValue();
//            // Class clazz = ClassUtil.getGenericType(schedule.getClass(),1);
//            JobConfiguration jobConfig =  new JobConfiguration(entry.getKey(), schedule.getClass(), schedule.getJob_sharding_count(), schedule.getJob_cron());
//            jobConfig.setShardingItemParameters(schedule.getJob_sharding_items());
//            JobScheduler job = new JobScheduler(regCenter, jobConfig);
//            jobs.add(job);
//            if(schedule.isJob_open()){
//                activeJobs.add(job);
//            }
//        }
//        start();
    }

    /**
     * 任务开始
     */
    private void start(){
        if(!activeJobs.isEmpty()){
            regCenter.init();
            for(JobScheduler job:activeJobs){
                job.init();
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if(inited.compareAndSet(false,true)){
            init();
        }
    }
}
