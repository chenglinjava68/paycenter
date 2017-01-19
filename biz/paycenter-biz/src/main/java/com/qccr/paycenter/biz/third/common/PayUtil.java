package com.qccr.paycenter.biz.third.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * 参照支付示例代码
 * @author user
 *
 */
public class PayUtil {
    private PayUtil() {
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

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr.append(key).append("=").append(value).toString();
            } else {
                prestr.append(key).append("=").append(value).append("&").toString();
            }
        }

        return prestr.toString();
    }
    
    public static String changeY2F(String amount){    
        String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额    
        int index = currency.indexOf(".");    
        int length = currency.length();    
        Long amLong = 0l;    
        if(index == -1){    
            amLong = Long.valueOf(currency+"00");    
        }else if(length - index >= 3){    
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));    
        }else if(length - index == 2){    
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);    
        }else{    
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");    
        }    
        return amLong.toString();    
    }

    /**
     * 分转元，都会有两位小数
     * @param amount
     * @return
     */
    public static String changeF2Y(int amount){
        BigDecimal amountY = BigDecimal.valueOf(amount).divide(new BigDecimal(100));
        DecimalFormat format = new DecimalFormat("0.00");
        String a = format.format(amountY);
        return a;
    }

	/***
	 * @description:获取IP
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getRemoteAddr(); 
	    } 
	    if(ip.contains(",")){
	    	ip = ip.split(",")[0];
	    }
	    if(isIpv4(ip))
	    	return ip;
	    else
	    	return "";
	    
	   }   
	
	public static boolean isIpv4(String ipAddress) {  
		  
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."  
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";  
  
        Pattern pattern = Pattern.compile(ip);  
        Matcher matcher = pattern.matcher(ipAddress);  
        return matcher.matches();  
  
    }  
	
	/**
	 * @description 生成随机字符串，比较低效
	 * @param length
	 *            生成字符串的长度
	 * @return
	 */
	public static String getRandom(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
        StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	

	public static String getSign(Map<String, String> params,String key){
		String strA =PayUtil.createLinkString(params); 
		params.put("key", key);	
		String signTemp =strA+"&key="+key;
		String sign = MD5.MD5Encode(signTemp).toUpperCase(); // 签名		
		return sign;
	}
	
	public static String getMD5Sign(Map<String, String> params,String key){
		String strA =PayUtil.createLinkString(params); 		
		String signTemp =strA+"&key="+key;
		String sign = MD5.MD5Encode(signTemp).toUpperCase(); 
		return sign;
	}
}
