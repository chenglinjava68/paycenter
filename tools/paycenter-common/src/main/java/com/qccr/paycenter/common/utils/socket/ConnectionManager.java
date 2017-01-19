package com.qccr.paycenter.common.utils.socket;

import com.qccr.paycenter.common.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CyclicBarrier;

/**
 * 短连接管理器
 * author: denghuajun
 * date: 2016/4/19 11:25
 * version: v1.1.0
 */
public class ConnectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    private  long maxWaitTime = 2000;

    private  int maxCount = 20;

    private  ConnectionPool pool ;

    public ConnectionManager(String address,int  port){
        this.pool = getConnectionPool(address, port, maxCount, maxWaitTime);
    }

    public ConnectionManager(String address,int  port,int maxCount,long maxWaitTime){
        this.maxCount = maxCount;
        this.maxWaitTime = maxWaitTime;
        this.pool = getConnectionPool(address, port, maxCount, maxWaitTime);
    }

    public  Connection getConnection () throws Exception {

        return pool.getConnection();

    }

    public  ConnectionPool getConnnectionPool(String address,int  port){
        return new ConnectionPool(address, port);
    }

    private  ConnectionPool getConnectionPool(String address,int  port,int maxCount,long maxWaitTime){
        return new ConnectionPool(address, port,maxCount,maxWaitTime);
    }

    public void  destroy(){
        pool.destroy();
    }

    public static class ConnectionThread implements Runnable {
        CyclicBarrier barrier;// 计数器
        ConnectionManager manager;


        // 构造方法
        public ConnectionThread(CyclicBarrier barrier, ConnectionManager manager) {
            this.barrier = barrier;
            this.manager = manager;
        }
        @Override
        public void run() {
            try {
                barrier.await();
                Connection connection = manager.getConnection();
                String data = "<?xml version=\"1.0\" encoding=\"GBK\"?><Request><Head><CorpNo>100004</CorpNo><CusId></CusId><TransCode>2003</TransCode><MsgId>611</MsgId><ReqId>123456</ReqId><TransDate>20160414</TransDate><TransTime>134700</TransTime></Head><Body><OrderNo>a0001</OrderNo></Body></Request>";
                String  result = connection.writeAndRead(data,"GBK");
                LogUtil.info(LOGGER, result);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(),ex);
            }
        }
    }


}
