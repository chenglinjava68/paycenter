package com.qccr.paycenter.model.dal.so.auth;

/**
 * Created by user on 2016/11/17.
 */
public class AuthResultSO {


    /**
     * 1已授权成功，2未授权
     */
    private int status;

    /**
     * 错误描述
     */
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
