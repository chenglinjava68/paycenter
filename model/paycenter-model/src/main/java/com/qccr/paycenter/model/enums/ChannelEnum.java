package com.qccr.paycenter.model.enums;

import com.qccr.paycenter.facade.constants.PayConstants;

/**
 * Created by user on 2016/1/14.
 */
public enum ChannelEnum {

    ALIPAY(1, PayConstants.ALI_PAY_CHANNEL),
    WECHAT(6,PayConstants.WECHAT_PAY_CHANNEL),
    UNION(8,PayConstants.UNION_PAY_CHANNEL),
    CZBANK(10, PayConstants.CZBANK_PAY_CHANNEL),
    OFFLINE(12, PayConstants.OFFLINE_PAY_CHANNEL),
    BOCOM(13, PayConstants.BOCOM_PAY_CHANNEL);
    private int value ;
    private String name;

    ChannelEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static ChannelEnum valueOf(int value) {
        for(ChannelEnum rcode : ChannelEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static ChannelEnum get(String name) {
        for(ChannelEnum config : ChannelEnum.values()){
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

    public static int getValue(String name) {
        for(ChannelEnum config : ChannelEnum.values()){
            if(config.name.equals(name))
                return config.getValue();
        }
        return 0;
    }


}
