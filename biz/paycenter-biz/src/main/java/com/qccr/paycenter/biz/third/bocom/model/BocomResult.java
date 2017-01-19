package com.qccr.paycenter.biz.third.bocom.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by limin on 2016/8/10.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "transData")
public class BocomResult  implements Serializable {
    private static final long serialVersionUID = 3017081190571218190L;
    private String version;//报文版本号,当前版本号：1.0
    private String returnCode;//返回码
    private String hostDate;//日期,格式：yyyyMMdd
    private String hostTime;//时间,格式:HHmmss
    private String authNo;//授权号
    private String addDataField;//附加字段
    private String addData;//附加域

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getHostDate() {
        return hostDate;
    }

    public void setHostDate(String hostDate) {
        this.hostDate = hostDate;
    }

    public String getHostTime() {
        return hostTime;
    }

    public void setHostTime(String hostTime) {
        this.hostTime = hostTime;
    }

    public String getAuthNo() {
        return authNo;
    }

    public void setAuthNo(String authNo) {
        this.authNo = authNo;
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
