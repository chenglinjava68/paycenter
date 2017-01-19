package com.qccr.paycenter.facade.dal.ro;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 公共返回结果
 * @author lim
 * @version 1.1.9
 * @date 2016/4/22
 */
public class CommonResultRO implements Serializable {

    private static final long serialVersionUID = -1343843347321977696L;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 失败时，错误信息
     */
    private String errmsg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
