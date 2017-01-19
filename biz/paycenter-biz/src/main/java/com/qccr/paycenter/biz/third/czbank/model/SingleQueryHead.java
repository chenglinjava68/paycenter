package com.qccr.paycenter.biz.third.czbank.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 浙商XML请求报文头
 * Created by lim on 2016/4/14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Head")
@XmlType
public class SingleQueryHead implements Serializable {

    /**
     * CorpNo 100004     ，
     CusId  你们在我们行的客户号     查询可以先不填     ，
     TransCode    2003
     */

    @XmlElement(name = "CorpNo")
    private String corpNo;//	商户编号 由银行提供，请在联调时咨询银行
    @XmlElement(name = "CusId")
    private String cusId;//	商户客户号 由银行提供，请在联调时咨询银行
    @XmlElement(name = "TransCode")
    private String transCode;//	交易号 例如：1001
    @XmlElement(name = "MsgId")
    private String msgId;//	报文标识号 要求每笔报文系统周期内唯一，返回报文生成系统内唯一编号返回。
    @XmlElement(name = "ReqId")
    private String reqId;//	请求号 要求每笔请求系统周期内唯一，返回报文原包返回。
    @XmlElement(name = "TransDate")
    private String transDate;//	交易日期
    @XmlElement(name = "TransTime")
    private String transTime;//	交易时间

    public SingleQueryHead() {
    }

    public SingleQueryHead(String corpNo, String cusId, String transCode, String msgId, String reqId, String transDate, String transTime) {
        this.corpNo = corpNo;
        this.cusId = cusId;
        this.transCode = transCode;
        this.msgId = msgId;
        this.reqId = reqId;
        this.transDate = transDate;
        this.transTime = transTime;
    }

    public String getCorpNo() {
        return corpNo;
    }

    public void setCorpNo(String corpNo) {
        this.corpNo = corpNo;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

}
