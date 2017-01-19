package com.qccr.paycenter.model.enums;

/**
 * Created by user on 2016/11/18.
 */
public enum FacepayAuthEnum {
    CREATED(0,"created"),//创建状态
    NORMAL(1,"normal"),	//正常授权状态
    TIMEOUT(2,"timeout");  //过期状态，或者未授权

    private int value ;
    private String msg;

    private FacepayAuthEnum(int value,String msg){
        this.value = value;
        this.msg = msg;
    }

    public static FacepayAuthEnum valueOf(int value) {
        for(FacepayAuthEnum rcode : FacepayAuthEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static FacepayAuthEnum get(String msg) {
        for(FacepayAuthEnum config : FacepayAuthEnum.values()){
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
