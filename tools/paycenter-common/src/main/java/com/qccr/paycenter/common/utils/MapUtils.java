package com.qccr.paycenter.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/9/21 11:21 $
 */
public class MapUtils {

    protected MapUtils() {

    }
    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String[]> sArray) {
        Map<String, String> result = new HashMap();
        if (sArray == null || sArray.isEmpty()) {
            return result;
        }
        for ( Map.Entry< String, String[] > entry : sArray.entrySet() ) {
            String value = entry.getValue()[0];
            if (value == null || "".equals(value) || "sign".equalsIgnoreCase(entry.getKey())
                    || "sign_type".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            result.put(entry.getKey(), value);
        }
        return result;
    }

    /**
     * 将Map<String, String[]> 转化为Map<String, String>
     * @param sArray
     * @return
     */
    public static Map<String, String> paraConvert(Map<String, String[]> sArray) {
        Map<String, String> result = new HashMap();
        if (sArray == null || sArray.isEmpty()) {
            return result;
        }
        for ( Map.Entry< String, String[] > entry : sArray.entrySet() ) {
            String value = entry.getValue()[0];
            result.put(entry.getKey(), value);
        }
        return result;
    }

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paramFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap();
        if (sArray == null || sArray.isEmpty()) {
            return result;
        }
        for ( Map.Entry< String, String> entry : sArray.entrySet() ) {
            String value = entry.getValue();
            if (value == null || "".equals(value) || "sign".equalsIgnoreCase(entry.getKey())
                    || "sign_type".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            result.put(entry.getKey(), value);
        }

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList(params.keySet());
        Collections.sort(keys);

        StringBuilder prestr = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr.append(key).append("=").append(value).toString();
            } else {
                prestr.append(key).append("=").append(value).append("&").toString();
            }
        }

        return prestr.toString();
    }

}
