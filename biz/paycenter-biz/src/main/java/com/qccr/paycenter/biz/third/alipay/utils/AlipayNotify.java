package com.qccr.paycenter.biz.third.alipay.utils;

import com.qccr.paycenter.biz.third.alipay.core.AlipayConfig;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;



/* *
 *类名：AlipayNotify
 *功能：支付宝通知处理类
 *详细：处理支付宝各接口通知返回
 *版本：3.3
 *日期：2012-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考

 *************************注意*************************
 *调试通知返回时，可查看或改写log日志的写入TXT里的数据，来检查通知返回是否正常
 */
public class AlipayNotify {

	/**
	 * log
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AlipayNotify.class);
	
    /**
     * 支付宝消息验证地址
     */
    private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&partner=";

    /**
     * 验证消息是否是支付宝发出的合法消息
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public static boolean verify(Map<String, String[]> params) {
        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
    	String responseTxt = "true";
		if(params.get("notify_id") != null) {
			String notify_id = params.get("notify_id")[0];
			/*0513支付回调紧急异常,不再验证这个参数*/
			responseTxt = verifyResponse(notify_id);
			LogUtil.info(LOGGER, "responseTxt = "+responseTxt);
		}
	    String sign = "";
	    if(params.get("sign")[0] != null) {sign = params.get("sign")[0];}
	    boolean isSign = getSignVeryfy(params, sign);
		LogUtil.info(LOGGER, "isSign = "+isSign);
        //写日志记录（若要调试，请取消下面两行注释）
        //String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign + "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
	    //AlipayCore.logResult(sWord);
        if (isSign && responseTxt.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	private static boolean getSignVeryfy(Map<String, String[]> Params, String sign) {
    	//过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = AlipayCore.paraFilter(Params);
        //获取待签名字符串
//    	LogUtil.info(LOGGER, "------------sParaNew----------");
//    	printmap(sParaNew);
        String preSignStr = AlipayCore.createLinkString(sParaNew);
        //LogUtil.info(LOGGER, "解析拼接得到的待签名字符串="+preSignStr);
        //获得签名验证结果
        boolean isSign = false;
        String signType=Params.get("sign_type")[0];
        if(AlipayConfig.SIGN_TYPE.equals(signType)){
        	isSign = RSA.verify(preSignStr, sign, AlipayConfig.ALI_PUBLIC_KEY, AlipayConfig.INPUT_CHARSET);
        }else if(AlipayConfig.MD5_SIGN_TYPE.equals(signType)){
			isSign = sign.equals(MD5.sign(preSignStr, AlipayConfig.MD5_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET));
		}
        return isSign;
    }

    /**
    * 获取远程服务器ATN结果,验证返回URL
    * @param notify_id 通知校验ID
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    public static String verifyResponse(String notify_id) {
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求

        String veryfy_url = HTTPS_VERIFY_URL + AlipayConfig.PARTNER + "&notify_id=" + notify_id;
        LogUtil.info(LOGGER, veryfy_url);
        return checkUrl(veryfy_url);
    }

    /**
    * 获取远程服务器ATN结果
    * @param urlvalue 指定URL路径地址
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    private static String checkUrl(String urlvalue) {
        String inputLine = "";
        try {
        	
        	inputLine = HttpUtil.simpleGet(urlvalue);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            inputLine = "";
        }
        return inputLine;
    }
    
    
    
    
    
    
    public static void main(String[] args) {
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("body", "您于2015-04-18 16:53:39下单的这笔订单，原价：3000.0，实付：2000.0，订单号：TWL222。 请注意确认");
    	 map.put("subject", "TWL222");
//    	 map.put("sign_type", "RSA");
    	 map.put("is_total_fee_adjust", "Y");
    	 map.put("notify_type", "trade_status_sync");
    	 map.put("out_trade_no", "77");
    	 map.put("buyer_email", "18758557037");
    	 map.put("total_fee", "0.01");
    	 map.put("quantity", "1");
    	 map.put("buyer_id", "2088502719676951");
    	 map.put("trade_no", "2015042300001000950050013035");
    	 map.put("notify_time", "2015-04-23 19:22:39");
    	 map.put("use_coupon", "N");
    	 map.put("trade_status", "WAIT_BUYER_PAY");
    	 map.put("discount", "0.00");
//    	 map.put("sign", "n77TP+uiTswj7ixSpf1vCdgumxYfEZJP0bEzB1md7RWIqxDJ7uA03ALDNGutlLcV7ZwcGfPWD/vlzqKCpBArWj+6zW8SqIYhA2pimTxjXiQ4XrLzt43252SfpUF33Z3K6MGyHtvQSwR1e+URuJWlXZqnO8R1uRRI5vGBgyrf7dA=");
    	 map.put("gmt_create", "2015-04-23 19:22:34");
    	 map.put("price", "0.01");
    	 map.put("seller_id", "2088911040582451");
    	 map.put("seller_email", "toowell@toowell.com");
    	 map.put("notify_id", "6bc7e4b4cfe97c2a1d96ef0e4691ec147a");
    	 map.put("payment_type", "1");
    	 
    	 String sign = "n77TP+uiTswj7ixSpf1vCdgumxYfEZJP0bEzB1md7RWIqxDJ7uA03ALDNGutlLcV7ZwcGfPWD/vlzqKCpBArWj+6zW8SqIYhA2pimTxjXiQ4XrLzt43252SfpUF33Z3K6MGyHtvQSwR1e+URuJWlXZqnO8R1uRRI5vGBgyrf7dA=";
    	 String preSignStr = AlipayCore.createLinkString(map);
    	 LogUtil.info(LOGGER, "解析拼接的待签名字符串 = "+preSignStr);
         //获得签名验证结果
         boolean isSign = false;
         if(AlipayConfig.SIGN_TYPE.equals("RSA")){
         	isSign = RSA.verify(preSignStr, sign, AlipayConfig.ALI_PUBLIC_KEY, AlipayConfig.INPUT_CHARSET);
         }
    	 LogUtil.info(LOGGER, "main",isSign);
	}
    
//    private static void printmap(Map<String, String> map){
////    	sun.security.internal.spec.TlsRsaPremasterSecretParameterSpec.getEncodedSecret();
//    	
//		  for (String key : map.keySet()) {
//	            String value = map.get(key);
//	            LogUtil.info(LOGGER, "key = "+key+", value = "+value);
//	        }
//	}
}
