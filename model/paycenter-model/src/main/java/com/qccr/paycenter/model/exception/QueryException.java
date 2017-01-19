package com.qccr.paycenter.model.exception;

/**
 * author: denghuajun
 * date: 2016/3/17 10:56
 * version: v1.0.0
 */
public class QueryException  extends RuntimeException{

    public QueryException( String msg,
                            Exception e) {
        super(msg, e);

    }

    public QueryException( String msg) {
        this( msg, null);
    }

}
