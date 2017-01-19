package com.qccr.paycenter.common.utils.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * socket端连接
 * author: denghuajun
 * date: 2016/4/19 11:23
 * version: v1.1.0
 */
public class Connection {
    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);
    private String address;

    private int port;

    private ConnectionPool pool;

    private Socket socket;

    private int length = 8;

    public Connection(String address, int port, ConnectionPool pool) {
        this.address = address;
        this.port = port;
        this.pool = pool;
        try {
            socket = new Socket(address,port);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }

    }

    public String read(String encoding){
        InputStream in = null;
        String result = "";
        byte[] counts = new byte[length];
        try {
            in = socket.getInputStream();
            in.read(counts,0,length);
            int count = Integer.parseInt(new String(counts));
            byte[] data = new byte[count];
            in.read(data,0,count);
            result = new String(data,encoding);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException e1) {
                LOGGER.error(e1.getMessage(),e1);
            }
            close();
        }
        return result;
    }

    public void write(String data,String encoding){
        OutputStream out = null;
        try {
            byte[] bytes = data.getBytes(encoding);
            int count = bytes.length;
            String countStr =  pillDataCout(count);
            String content = countStr+data;
            out = socket.getOutputStream();
            out.write(countStr.getBytes(encoding));
            out.write(content.getBytes());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }finally {
            try {
                if(out != null) {
                    out.close();
                }
            } catch (IOException e1) {
                LOGGER.error(e1.getMessage(),e1);
            }
            close();
        }
    }

    public String writeAndRead(String data,String encoding){
        if(socket == null) {
            return null;
        }
        InputStream in = null;
        OutputStream out = null;
        String result = "";
        byte[] counts = new byte[length];
        try {
            byte[] bytes = data.getBytes(encoding);
            int writeCount = bytes.length;
            String countStr =  pillDataCout(writeCount);
            String content = countStr+data;
            out = socket.getOutputStream();
            out.write(content.getBytes());

            in = socket.getInputStream();
            in.read(counts,0,length);
            int readCount = Integer.parseInt(new String(counts));
            byte[] readData = new byte[readCount];
            in.read(readData,0,readCount);
            result = new String(readData,encoding);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(out != null) {
                    out.close();
                }
            } catch (IOException e1) {
                LOGGER.error(e1.getMessage(),e1);
            }
            close();
        }

        return result;
    }

    public void  close(){
        try {
            if(socket != null) {
                socket.close();
            }
            pool.remove();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    public String pillDataCout( int count){
        String countStr =  String.valueOf(count);
        if(countStr.length()<length){
            StringBuilder temp  = new StringBuilder();
            for(int i=0;i<length-countStr.length();i++){
                temp.append("0");
            }
            temp.append(countStr);
            return temp.toString();
        }
        return countStr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ConnectionPool getPool() {
        return pool;
    }

    public void setPool(ConnectionPool pool) {
        this.pool = pool;
    }

}
