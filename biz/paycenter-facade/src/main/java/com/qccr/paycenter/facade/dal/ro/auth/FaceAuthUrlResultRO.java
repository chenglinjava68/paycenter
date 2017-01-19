package com.qccr.paycenter.facade.dal.ro.auth;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by user on 2016/11/16.
 */
public class FaceAuthUrlResultRO implements Serializable {
    private static final long serialVersionUID = 5052595144107438471L;
    //生成状态	status	是	String	1已授权成功，2未授权
    private int status;
    //错误描述	msg	否	String	错误描述
    private String msg;
    //授权回调地址	authUrl	是	String	授权回调地址
    private String authUrl;
    //备注	remark	否	String	备注
    private String remark;

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

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
