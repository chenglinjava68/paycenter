package com.qccr.paycenter.biz.task;

/**
 * 延迟任务触发器，负责将任务转发至核心任务池
 * author: denghuajun
 * date: 2016/4/8 20:26
 * version: v1.0.0
 */
public interface Trigger {

    public void trigger();

}
