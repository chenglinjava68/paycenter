package com.qccr.paycenter.biz.task.job;

import java.util.Collections;
import java.util.List;

/**
 * author: denghuajun
 * date: 2016/4/8 10:48
 * version: v1.1.0
 */
public abstract class ItemsWorker<T> implements Worker<T,List<Integer>>{

    @Override
    public List<T> fetch(List<Integer> integers) {
        return Collections.emptyList();
    }

    @Override
    public boolean process(T t) {
        return false;
    }

    @Override
    public boolean continued() {
        return false;
    }
}
