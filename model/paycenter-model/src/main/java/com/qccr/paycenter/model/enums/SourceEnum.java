package com.qccr.paycenter.model.enums;

/**
 * 订单来源,标记订单来源，与订单中心一致
 * Created by lim on 2016/7/15.
 */
public enum SourceEnum {

    CARD(24, "card");//卡券

    private int value ;
    private String msg;

    private SourceEnum(int value,String msg){
        this.value = value;
        this.msg = msg;
    }

    public static SourceEnum valueOf(int value) {
        for(SourceEnum rcode : SourceEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static SourceEnum get(String msg) {
        for(SourceEnum config : SourceEnum.values()){
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
