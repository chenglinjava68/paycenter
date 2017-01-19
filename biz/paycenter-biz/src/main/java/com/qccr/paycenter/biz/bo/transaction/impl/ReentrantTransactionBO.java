package com.qccr.paycenter.biz.bo.transaction.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.qccr.paycenter.biz.bo.transaction.TransactionBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;



@SuppressWarnings("rawtypes")
@Component
public class ReentrantTransactionBO implements TransactionBO {

    private static final ThreadLocal<TransactionCallback> rootTransactionCallbackTL = new ThreadLocal<TransactionCallback>();

    @Resource
    private TransactionBO targetTransactionBO;

    @Override
    public <T> T execute(TransactionCallback<T> transactionCallback) {
        TransactionCallback rootTransactionCallback = getRootTransactionCallback();
        if (rootTransactionCallback == null) {
            // 如果没有根事务，把当前事务设置成根事务
            rootTransactionCallback = transactionCallback;
            setRootTransactionCallback(transactionCallback);
        }
        if (rootTransactionCallback == transactionCallback) {
            // 如果当前事务就就是根事务，执行事务
            try {              
                return targetTransactionBO.execute(transactionCallback);
            } finally {              
                clearRootTransactionCallback();
            }
        } else {
            // 直接执行         
            T t = transactionCallback.doTransaction();        
            return t;
        }
    }

    private TransactionCallback getRootTransactionCallback() {
        return rootTransactionCallbackTL.get();
    }

    private void setRootTransactionCallback(TransactionCallback transactionCallback) {
        rootTransactionCallbackTL.set(transactionCallback);
    }

    private void clearRootTransactionCallback() {
        rootTransactionCallbackTL.remove();
    }

    public void setTargetTransactionBO(TransactionBO targetTransactionBO) {
        this.targetTransactionBO = targetTransactionBO;
    }
}
