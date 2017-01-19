package com.qccr.paycenter.biz.bo.transaction.impl;



import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import com.qccr.paycenter.biz.bo.transaction.TransactionBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;

import javax.annotation.Resource;

@Component
public class DataSourceTransactionBO implements TransactionBO, InitializingBean {
	@Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public void afterPropertiesSet() {
        // Do nothing because of X and Y.
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute(final TransactionCallback<T> transactionCallback) {
        return (T)transactionTemplate.execute(new org.springframework.transaction.support.TransactionCallback() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                return transactionCallback.doTransaction();
            }
        });
    }

}
