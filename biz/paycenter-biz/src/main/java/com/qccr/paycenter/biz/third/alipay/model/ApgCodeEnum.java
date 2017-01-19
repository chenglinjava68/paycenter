package com.qccr.paycenter.biz.third.alipay.model;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/6/28 11:39 $
 */
public enum ApgCodeEnum {
//支付成功（10000），支付失败（40004），等待用户付款（10003）和未知异常（20000）
    SUCCESS(1,"10000"),
    WAIT_PAY(4,"10003"),
    ERROR(2,"20000"),
    BUSINESS_ERROR(3,"40004");
    private int value ;
    private String msg;

    ApgCodeEnum(int value, String msg){
        this.value = value;
        this.msg = msg;
    }

    /**
     *
     * @param value
     * @return
     */
    public static ApgCodeEnum valueOf(int value) {
        for(ApgCodeEnum rcode : ApgCodeEnum.values()){
            if(rcode.value == value){
                return rcode;
            }
        }
        return null;
    }

    /**
     *
     * @param msg
     * @return
     */
    public static ApgCodeEnum get(String msg) {
        for(ApgCodeEnum config : ApgCodeEnum.values()){
            if(config.msg.equals(msg)){
                return config;
            }
        }
        return null;
    }

    public  int getValue(){

        return value;

    }

    public  String getMsg(){

        return msg;
    }
    @Override
    public String toString(){

        return String.valueOf(this.value);

    }

    public int getInfo(){

        return this.value;
    }
}
