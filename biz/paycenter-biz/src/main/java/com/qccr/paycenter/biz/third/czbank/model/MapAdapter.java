package com.qccr.paycenter.biz.third.czbank.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/4/14.
 */
public class MapAdapter extends XmlAdapter<Object, Map<String, String>> {

    /**
     * 序列化方法。
     *
     * 把java bean转换成Element，方便JAXB按照指定格式序列化。
     */
    @Override
    public Object marshal(Map<String, String> rows) throws Exception {
        // TODO Auto-generated method stub
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        Element rootEle = document.createElement("rows");
            Iterator<Map.Entry<String, String>> itera = rows.entrySet().iterator();
            while (itera.hasNext()) {
                Map.Entry<String, String> entry = itera.next();
                String key = entry.getKey();
                String value = entry.getValue();
                if (key == null || "".equals(key)) {
                    continue;
                }
                if (value == null) {
                    value = "";
                }
                Element detailEle = document.createElement(key);
                detailEle.setTextContent(value);
                rootEle.appendChild(detailEle);
            }
        document.appendChild(rootEle);
        return rootEle;
    }

    /**
     * 反序列化方法。
     *
     * 把Element转换成java bean。
     */
    @Override
    public Map<String, String> unmarshal(Object datas) throws Exception {
        // TODO Auto-generated method stub
        if(datas==null){
            return null;
        }
        NodeList rowlist = ((Element)datas).getChildNodes();
        if(rowlist == null){
            return null;
        }
        int rowCount = rowlist.getLength();
        if(rowCount == 0){
            return null;
        }

        List<Map<String, String>> result = new ArrayList<Map<String,String>>();
        for(int i = 0; i<rowCount; i++){
            Node rowNode = rowlist.item(i);
            if(!"detail".equals(rowNode.getNodeName())){
                continue;
            }

            NodeList detailList = rowNode.getChildNodes();
            if(detailList == null){
                continue;
            }
            int detailCount = detailList.getLength();
            if(detailCount == 0){
                continue;
            }

            Map<String, String> detailMap = new HashMap<String, String>();
            for(int j = 0; j < detailCount; j++){
                Node detailNode = detailList.item(j);
                String key = detailNode.getNodeName();
                String value = detailNode.getTextContent();
                if(key == null || "".equals(key)){
                    continue;
                }
                detailMap.put(key, value);
            }
            result.add(detailMap);
        }
        return null;
    }

}