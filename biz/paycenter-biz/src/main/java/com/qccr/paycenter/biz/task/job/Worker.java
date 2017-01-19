package com.qccr.paycenter.biz.task.job;

import java.util.List;

/**
 * author: denghuajun
 * date: 2016/4/8 10:35
 * version: v1.1.0
 */
public interface Worker<T,K> {

    /**
     * 根据参数，抽取任务数据
     * @param k
     * @return
     */
    public List<T> fetch(K k);

    /**
     * 任务数据处理
     * @param t
     * @return
     */
    public boolean process(T t);

    /**
     * 任务是否持续执行
     * @return
     */
    public boolean continued();

}
