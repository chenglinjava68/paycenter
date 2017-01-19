package com.qccr.paycenter.common.utils.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * socket短连接池
 * author: denghuajun
 * date: 2016/4/19 11:24
 * version: v1.1.0
 */
public class ConnectionPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionPool.class);

    private  long maxWaitTime = 2000;

    private  int maxCount = 20;

    private String address;

    private int port;

    private AtomicInteger active =  new AtomicInteger(0);


    public ConnectionPool(String address,int port) {
        this.address = address;
        this.port = port;
    }

    public ConnectionPool(String address,int port,int maxCount,long maxWaitTime) {
        this.address = address;
        this.port = port;
        this.maxCount = maxCount;
        this.maxWaitTime = maxWaitTime;
    }

    public  Connection getConnection() throws InterruptedException {

        Connection connection = null;
        int size = active.get();
        if(size>=maxCount){
            long times = 0;
            while(times<maxWaitTime){
                size = active.get();
                if(size<maxCount){
                    break;
                }
                Thread.sleep(200);
                times += 200;
            }
            if(times>maxWaitTime){
                LOGGER.error("get connecttion time out");
                throw new RuntimeException("get connecttion time out");
            }
        }
        if(active.getAndIncrement()<maxCount){
            connection = new Connection(address,port,this);
        }
        return connection;
    }

    public void remove(){
        active.getAndDecrement();

    }

    public void destroy(){
// Do nothing because of X and Y.
    }


}
