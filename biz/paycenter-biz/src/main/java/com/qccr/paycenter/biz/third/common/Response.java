package com.qccr.paycenter.biz.third.common;


import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * @author digBen
 * @param <T>
 */
public class Response<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 8350327877975282483L;

    private boolean success;  //表示调用是否成功 ,如果为true,则可以调用getResult,如果为false,则调用errorCode来获取出错信息

    private T result;  //获取调用返回值

    private String errorCode; //获取错误码

    private String errorMsg;

    public Response() {
    	
    }

    public Response(T result) {
        this.success = true;
        this.result = result;
    }

    public Response(String errorCode, String errorMsg) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Response(String errorCode, String errorMsg,T result) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.result = result;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        success = true;
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.success = false;
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Response response = (Response) o;

        if (success != response.success) return false;
        if (!errorCode.equals(response.errorCode)) return false;
        if (!result.equals(response.result)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = 1;
        if(!success) {
            result1 = 0;
        }
        result1 = 31 * result1 + result.hashCode();
        result1 = 31 * result1 + errorCode.hashCode();
        return result1;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("success", success)
                .add("result", result)
                .add("errorCode", errorCode)
                .add("errorMsg", errorMsg)
                .omitNullValues()
                .toString();
    }
}
