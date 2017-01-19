package com.qccr.paycenter.biz.third.union.utils;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.qccr.paycenter.common.utils.StringUtil;


public class UnionpayUtil {
    private UnionpayUtil() {
    }

    /**
     * 除去数组中的空值和签名参数
     * @param contentData 签名参数组
     * @return 去掉空值
     */
    @SuppressWarnings("unchecked")
	public static Map<String, String> paraFilter(Map<String, ?> contentData) {

    	Entry<String, String> obj = null;
		Map<String, String> submitFromData = new HashMap<String, String>();
		for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
			obj = (Entry<String, String>) it.next();
			String value = obj.getValue();
			if (!StringUtil.isNullOrEmpty(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(obj.getKey(), value.trim());
			}
		}
        return submitFromData;
    }
    
    /** 
     * 除去数组中的空值
     * @param sArray 签名参数组
     * @return 去掉空值
     */
    public static Map<String, String> paraFilterBack(Map<String, String[]> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.isEmpty()) {
            return result;
        }

        for ( Map.Entry< String, String[] > entry : sArray.entrySet() ) {
            String value = entry.getValue()[0];
            if (value == null || "".equals(value)) {
                continue;
            }
            result.put(entry.getKey(), value);
        }
        return result;
    }
    

    
    
}
