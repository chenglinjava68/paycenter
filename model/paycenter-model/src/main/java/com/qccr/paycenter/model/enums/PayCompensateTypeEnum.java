package com.qccr.paycenter.model.enums;

/**
 *
 * author: denghuajun
 * date: 2016/4/12 13:40
 * version: v1.0.0
 */
public enum  PayCompensateTypeEnum {

    COMMON(0,"common"),//创建状态
    REPEAT(1,"repeat"),//创建状态
    CLOSE(2,"close");//退款成功

    private int value;
    private String msg;

    private PayCompensateTypeEnum(int value,String msg){
        this.value = value;
        this.msg = msg;
    }

    public static PayCompensateTypeEnum valueOf(int value) {
        for(PayCompensateTypeEnum rcode : PayCompensateTypeEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static PayCompensateTypeEnum get(String msg) {
        for(PayCompensateTypeEnum config : PayCompensateTypeEnum.values()){
            if(config.msg.equals(msg))
                return config;
        }
        return null;
    }

    public  int getValue(){

        return value;

    }

    public  String getMsg(){

        return msg;
    }

}
