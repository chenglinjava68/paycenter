package com.qccr.paycenter.biz.third.alipay.utils;

import com.qccr.paycenter.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* *
 *类名：AlipayFunction
 *功能：支付宝接口公用函数类
 *详细：该类是请求、通知返回两个文件所调用的公用函数核心处理文件，不需要修改
 *版本：3.3
 *日期：2012-08-14
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
public class AlipayCore {
    private AlipayCore() {
    }
	 /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String[]> sArray) {

        Map<String, String> result = new HashMap<String, String>();

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
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paramFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

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

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuilder prestr = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if(!StringUtil.isNullOrEmpty(value)) {
                if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                    prestr.append(key).append("=").append(value).toString();
                } else {
                    prestr.append(key).append("=").append(value).append("&").toString();
                }
            }
        }

        return prestr.toString();
    }
}
