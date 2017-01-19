package com.qccr.paycenter.common.exception;

/**
 * author: denghuajun
 * date: 2016/4/6 16:23
 * version: v1.0.0
 */
public class BeanTransException extends RuntimeException{

    public BeanTransException(String message, Throwable cause) {
        super(message, cause);
    }


    public BeanTransException(Throwable cause) {
        super(cause);
    }

}
