package com.qccr.paycenter.model.enums;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 14:24 $
 */
public enum WhichPayEnum {
    //混合支付(blend_pay)，线下支付(offline_pay)，线上支付(online_pay)

    ONLINE_PAY(1,"online_pay"),
    OFFLINE_PAY(2,"offline_pay"),
    BLEND_PAY(3,"blend_pay"),
    ONLINE_SYNC_PAY(4,"online_sync_pay");//这个主要用于交行等支付同步返回结果使用的

    private int value ;
    private String whichType;


    WhichPayEnum(int value,String whichType){
        this.value = value;
        this.whichType = whichType;
    }

    public static WhichPayEnum valueOf(int value) {
        for(WhichPayEnum node : WhichPayEnum.values()){
            if(node.value == value)
                return node;
        }
        return null;
    }

    public static WhichPayEnum get(String msg) {
        for(WhichPayEnum node : WhichPayEnum.values()){
            if(node.whichType.equals(msg))
                return node;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getWhichType() {
        return whichType;
    }

    public void setWhichType(String whichType) {
        this.whichType = whichType;
    }
}
