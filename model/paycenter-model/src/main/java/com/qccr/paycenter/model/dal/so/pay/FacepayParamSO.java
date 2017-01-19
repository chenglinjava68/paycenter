package com.qccr.paycenter.model.dal.so.pay;

/**
 * Created by user on 2016/11/18.
 */
public class FacepayParamSO {
    //    用户ID	super_user_id	是	String(32)	超人平台上面的商户的userId
    private String userId;

    //    支付条形码	auth_code	是	String	用户支付宝付款码信息
    private String authCode;
    //    订单号	business_no	是	String	业务单号
    private String businessNo;
    //    订单总金额	total_amount	是	Integer	支付金额，单位：分
    private Integer totalAmount;
    //    订单主题	subject	是	String	订单标题
    private String subject;
    //    商户门店编号	store_id	可选	String	商户门店编号
    private String storeId;
    //    是否时间	out_time	可选	Boolean	true超时
    private Boolean outTime;
    //    超时时间	times	可选	Integer	out_time为true必传，交易超时时间，单位：秒
    private Integer times;
    //    订单描述	desc	否	String	订单描述
    private String desc;
    //    渠道	channel	是	String	alipay||wechat||…
    private String channel;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Boolean getOutTime() {
        return outTime;
    }

    public void setOutTime(Boolean outTime) {
        this.outTime = outTime;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
