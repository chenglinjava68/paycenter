package com.qccr.paycenter.common.utils;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * @author dhj
 * @version $Id: HttpRetryHandler ,v 0.1 2016/12/22 14:01 dhj Exp $
 * @name
 */
public class HttpRetryHandler implements HttpRequestRetryHandler {

    private final int maxTries = 3;

    public HttpRetryHandler() {
    }

    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        return (exception instanceof NoHttpResponseException || exception instanceof ConnectTimeoutException || exception instanceof SocketTimeoutException) && executionCount <= maxTries;
    }

}