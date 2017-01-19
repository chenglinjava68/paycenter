package com.qccr.paycenter.biz.bo.transaction;


public interface TransactionCallback<T> {

    T doTransaction();

}
