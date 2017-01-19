package com.qccr.paycenter.model.dal.so.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by limin on 2016/8/9.
 */
public class SignVerifyCodeResultSO implements Serializable {

    private static final long serialVersionUID = 3791799492785469427L;

    /**
     * 数据封装是否成功
     */
    private boolean success;

    /**
     * 错误描述
     */
    private String msg;
    private String addData;	//附加域

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAddData() {
        return addData;
    }

    public void setAddData(String addData) {
        this.addData = addData;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
