package com.qccr.paycenter.biz.third.alipay.model;
/**
 * 交易撤销枚举
 * @author pengdc
 *
 */
public enum TradeCancelEnum {
	SUCCESS(1,"success"),//撤销成功
	WAIT(4,"wait"),//等待重试
	PAYED(5,"payed");//已支付不可撤销
    private int value ;
    private String name;

    TradeCancelEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static TradeCancelEnum valueOf(int value) {
        for(TradeCancelEnum rcode : TradeCancelEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static TradeCancelEnum get(String name) {
        for(TradeCancelEnum config : TradeCancelEnum.values()){
            if(config.name.equals(name))
                return config;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
