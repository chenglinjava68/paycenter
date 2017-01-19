package com.qccr.paycenter.biz.third.czbank.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lim on 2016/4/14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Request")
@XmlType
public class SingleQueryRequest implements Serializable {

    @XmlElement(name = "Head")
    private SingleQueryHead head;

    @XmlElement(name = "Body")
    @XmlJavaTypeAdapter(MapAdapter.class)
    private HashMap<String, String> body;

    public SingleQueryHead getHead() {
        return head;
    }

    public void setHead(SingleQueryHead head) {
        this.head = head;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(HashMap<String, String> body) {
        this.body = body;
    }
}
