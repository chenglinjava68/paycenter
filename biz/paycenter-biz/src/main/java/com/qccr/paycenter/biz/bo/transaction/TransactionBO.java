package com.qccr.paycenter.biz.bo.transaction;


public interface TransactionBO {

    <T> T execute(TransactionCallback<T> transactionCallback);

}
