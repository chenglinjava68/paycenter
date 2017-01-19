package com.qccr.paycenter.common.exception;

/**
 * author: denghuajun
 * date: 2016/3/29 10:15
 * version: v1.0.0
 */
public class PaycenterRedisException extends RuntimeException{

    public PaycenterRedisException(String message, Throwable cause) {
        super(message, cause);
    }


    public PaycenterRedisException(Throwable cause) {
        super(cause);
    }


}
