package com.qccr.paycenter.model.enums;

/**
 * author: denghuajun
 * date: 2016/3/5 18:25
 * version: v1.0.0
 */
public enum PayCompensateEnum {

    CREATED(0,"created"),//创建状态
    SUCCESS(1,"success"),	//退款成功
    FAIL(2,"fail"),  //退款失败
    FINISH(3,"finish"), //退款完成
    TIMING(4,"timing"), //退款进行中
    CHANGE(5,"change"), //必须转人工退款
    NOTSURE(6,"notsure"); // 不缺定状态
    private int value;
    private String msg;

    private PayCompensateEnum(int value,String msg){
        this.value = value;
        this.msg = msg;
    }

    public static PayCompensateEnum valueOf(int value) {
        for(PayCompensateEnum rcode : PayCompensateEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static PayCompensateEnum get(String msg) {
        for(PayCompensateEnum config : PayCompensateEnum.values()){
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
    @Override
    public String toString(){

        return String.valueOf(this.value);

    }

    public int getInfo(){

        return this.value;
    }

}
