package com.qccr.paycenter.biz.third.bocom.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by lim on 2016/8/9.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "transData")
@XmlType
public class BocomParam  implements Serializable {
    private static final long serialVersionUID = 7009186887720166010L;
    @XmlElement(name = "version")
    private String version;	//版本号
    @XmlElement(name = "msgId")
    private String msgId;	//消息类型
    @XmlElement(name = "transCode")
    private String transCode;	//交易代码
    @XmlElement(name = "cardNo")
    private String cardNo;	//卡号
    @XmlElement(name = "expiryDate")
    private String expiryDate;	//卡片有效期
    @XmlElement(name = "merNo")
    private String merNo;	//商户号
    @XmlElement(name = "termNo")
    private String termNo;	//终端号
    @XmlElement(name = "traceNo")
    private String traceNo;	//流水号
    @XmlElement(name = "amount")
    private String amount;	//金额
    @XmlElement(name = "invioceNo")
    private String invioceNo;	//票据号
    @XmlElement(name = "addDataField")
    private String addDataField;	//附加字段
    @XmlElement(name = "addData")
    private String addData;	//附加域

    public BocomParam() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getTermNo() {
        return termNo;
    }

    public void setTermNo(String termNo) {
        this.termNo = termNo;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInvioceNo() {
        return invioceNo;
    }

    public void setInvioceNo(String invioceNo) {
        this.invioceNo = invioceNo;
    }

    public String getAddDataField() {
        return addDataField;
    }

    public void setAddDataField(String addDataField) {
        this.addDataField = addDataField;
    }

    public String getAddData() {
        return addData;
    }

    public void setAddData(String addData) {
        this.addData = addData;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
