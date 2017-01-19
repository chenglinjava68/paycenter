package com.qccr.paycenter.facade.dal.ro.compensate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 受理补偿退款申请返回结果
 * @author: denghuajun
 * @date: 2016/3/7 11:39
 * @version: v1.0.0
 */
public class PayCompensateResultRO implements Serializable{
    private static final long serialVersionUID = 2808376472090352087L;

    /**
     * 补偿退款申请是否成功
     */
    private boolean success;

    /**
     * 失败时，错误信息
     */
    private String errMsg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
