package com.qccr.paycenter.biz.task.job;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * author: denghuajun
 * date: 2016/4/8 10:48
 * version: v1.1.0
 */
public abstract class ParamsWorker <T> implements Worker<T,Map<Integer,String>>{

    @Override
    public List<T> fetch(Map<Integer, String> integerStringMap) {
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
