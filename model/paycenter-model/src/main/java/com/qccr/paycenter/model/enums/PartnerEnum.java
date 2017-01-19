package com.qccr.paycenter.model.enums;


/**
 * 这个部分数据与knife里面的Source相同
 * Created by lim on 2016/7/15.
 */
public enum PartnerEnum {

    BORDERPROD(401,"borderprod","B端订单产品"),
    CARMAN(900,"carman","C端APP网关"),
    CASHCOW(950,"cashcow","B端APP网关"),
    CORDERPROD(303,"corderprod","C端订单管理产品"),
    FUELCARDPROD(347,"fuelcardprod","加油业务"),
    OPENAPI(903,"OPENAPI","开放平台"),
    ORDERCENTER(207, "ordercenter", "订单中心");

    private int value ;
    private String partner;
    private String desc;
    private PartnerEnum(int value, String partner, String desc) {
        this.value = value;
        this.partner = partner;
        this.desc = desc;
    }

    public static PartnerEnum valueOf(int value) {
        for(PartnerEnum rcode : PartnerEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static PartnerEnum get(String msg) {
        for(PartnerEnum config : PartnerEnum.values()){
            if(config.desc.equals(msg))
                return config;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getPartner() {
        return partner;
    }

    public String getDesc() {
        return desc;
    }
}
