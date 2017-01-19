package com.qccr.paycenter.biz.third.union.sdk;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.qccr.paycenter.common.utils.LogUtil;

/**
 * 名称： 基础参数<br>
 * 功能： 提供基础方法<br>
 * 日期： 2015-09<br>
 * 版本： 1.0.0 
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class DemoBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoBase.class);
	public DemoBase() {
		super();
	}
	
	public static final String encoding = "UTF-8";
	
	public static final String version = "5.0.0";
	
	//后台服务对应的写法参照 FrontRcvResponse.java
	public static final String frontUrl = new StringBuilder("http://172.18.").append("137.63:8080/ACPSample-PCGate-UTF8/frontRcvResponse").toString();

	//后台服务对应的写法参照 BackRcvResponse.java
	public static final String backUrl = new StringBuilder("http://222.222.").append("222.222:8080/ACPSample-PCGate-UTF8/BackRcvResponse").toString();//受理方和发卡方自选填写的域[O]--后台通知地址

	// 商户发送交易时间 格式:YYYYMMDDhhmmss
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	// AN8..40 商户订单号，不能含"-"或"_"
	public static String getOrderId() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	/**
	 * 构造HTTP POST交易表单的方法示例
	 * 
	 * @param action
	 *            表单提交地址
	 * @param hiddens
	 *            以MAP形式存储的表单键值
	 * @return 构造好的HTTP POST交易表单
	 */
	public static String createHtml(String action, Map<String, String> hiddens) {
		StringBuilder sf = new StringBuilder();
		sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset="+ DemoBase.encoding+"\"/></head><body>");
		sf.append("<form id = \"pay_form\" action=\"" + action
				+ "\" method=\"post\">");
		if (null != hiddens && 0 != hiddens.size()) {
			Set<Entry<String, String>> set = hiddens.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String> ey = it.next();
				String key = ey.getKey();
				String value = ey.getValue();
				sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\""
						+ key + "\" value=\"" + value + "\"/>");
			}
		}
		sf.append("</form>");
		sf.append("</body>");
		sf.append("<script type=\"text/javascript\">");
		sf.append("document.all.pay_form.submit();");
		sf.append("</script>");
		sf.append("</html>");
		return sf.toString();
	}

	/**
	 * java main方法 数据提交 　　 对数据进行签名
	 * 
	 * @param contentData
	 * @return　签名后的map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> signData(Map<String, ?> contentData) {
		Entry<String, String> obj = null;
		Map<String, String> submitFromData = new HashMap<String, String>();
		for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
			obj = (Entry<String, String>) it.next();
			String value = obj.getValue();
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(obj.getKey(), value.trim());
				LogUtil.info(LOGGER,obj.getKey() + "-->" + String.valueOf(value));
			}
		}
		/**
		 * 签名
		 */
		SDKUtil.sign(submitFromData, encoding);
		return submitFromData;
	}
	
	/**
	 * 多证书签名方法
	 * 如果有多个商户号接入银联,每个商户号对应不同的证书可以使用此方法:传入私钥证书和密码(并且在acp_sdk.properties中 配置 acpsdk.singleMode=false) 
	 * @param contentData
	 * @param certPath
	 * @param certPwd
	 * @return　签名后的map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> signData(Map<String, ?> contentData,String certPath, String certPwd) {
		Entry<String, String> obj = null;
		Map<String, String> submitFromData = new HashMap<String, String>();
		LogUtil.info(LOGGER,"打印请求报文域 :");
		for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
			obj = (Entry<String, String>) it.next();
			String value = obj.getValue();
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(obj.getKey(), value.trim());
				LogUtil.info(LOGGER,obj.getKey() + "-->" + String.valueOf(value));
			}
		}
		/**
		 * 签名
		 */
		SDKUtil.signByCertInfo(submitFromData, encoding,certPath, certPwd);

		return submitFromData;
	}
	
	

	/**
	 * 功能：后台交易给银联地址发请求
	 * @param submitFromData
	 * @return 返回报文 map
	 */
	public static Map<String, String> submitUrl(
			Map<String, String> submitFromData,String requestUrl) {
		String resultString = "";
		LogUtil.info(LOGGER, "请求银联地址:" + requestUrl);
		/**
		 * 发送后台请求数据
		 */
		HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
		try {
			int status = hc.send(submitFromData, encoding);
			if (200 == status) {
				resultString = hc.getResult();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		Map<String, String> resData = new HashMap<String, String>();
		/**
		 * 验证签名
		 */
		if (null != resultString && !"".equals(resultString)) {
			// 将返回结果转换为map
			resData = SDKUtil.convertResultStringToMap(resultString);
			if (SDKUtil.validate(resData, encoding)) {
				LogUtil.info(LOGGER, "验证签名成功,可以继续后边的逻辑处理");
			} else {
				LogUtil.info(LOGGER, "验证签名失败,必须验签签名通过才能继续后边的逻辑...");
			}
		}
		return resData;
	}
	
	/**
	 * 功能：获取批量文件内容并返回使用DEFLATE压缩算法压缩，Base64编码后字符串
	 * 适用到的交易：批量代付，批量代收，批量退货
	 * @param filePath 批量文件路径
	 * @return
	 */
	private static String  enCodeFileContent(String filePath){
		String baseFileContent = "";
		
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(),e);
			}
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			int fl = in.available();
			byte[] s = new byte[fl];
			in.read(s, 0, fl);
			// 压缩编码.
			baseFileContent = new String(SecureUtil.base64Encode(SecureUtil.deflater(s)));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(),e);
				}
			}
		}
		return baseFileContent;
	}

	/**
	 * 功能：后台交易给银联地址发请求
	 * @param contentData
	 * @return 返回报文 map
	 */
	public static Map<String, String> submitDate(Map<String, ?> contentData,String requestUrl) {
		Map<String, String> submitFromData = (Map<String, String>) signData(contentData);
		return submitUrl(submitFromData,requestUrl);
	}
	
	/**
	 * 持卡人信息域customerInfo构造
	 * 说明：不勾选对敏感信息加密权限 使用旧的构造customerInfo域方式，不对敏感信息进行加密（对cvn2，有效期不加密），但如果送pin的话需加密
	 * @param customerInfoMap 信息域请求参数 key送域名value送值,必送
	 *        例如：customerInfoMap.put("certifTp", "01");					//证件类型
				  customerInfoMap.put("certifId", "341126197709218366");	//证件号码
				  customerInfoMap.put("customerNm", "互联网");				//姓名
				  customerInfoMap.put("phoneNo", "13552535506");			//手机号
				  customerInfoMap.put("smsCode", "123456");					//短信验证码
				  customerInfoMap.put("pin", "111111");						//密码（加密）
				  customerInfoMap.put("cvn2", "123");           			//卡背面的cvn2三位数字（不加密）
				  customerInfoMap.put("expired", "1711");  				    //有效期 年在前月在后（不加密)
	 * @param  accNo  对密码加密使用到的卡号，必送				  
	 * @return base64后的持卡人信息域字段
	 */
	public static String getCustomerInfo(Map<String,String> customerInfoMap,String accNo) {

		StringBuilder sf = new StringBuilder("{");
		
		Set<Map.Entry<String, String>> allSet=customerInfoMap.entrySet();
		for( Iterator<Map.Entry<String, String>> it = allSet.iterator(); it.hasNext();){
			Map.Entry<String, String> me=it.next();
			String key = me.getKey();
			String value = me.getValue();
			if(key.equals("pin")){
				value = SDKUtil.encryptPin(accNo,value,encoding);
			}
			if(it.hasNext())
				sf.append(key + SDKConstants.EQUAL+ value + SDKConstants.AMPERSAND);
			else
				sf.append(key + SDKConstants.EQUAL + value);
		}
		sf.append("}");
		
		String customerInfo = sf.toString();
		LogUtil.info(LOGGER, "组装的customerInfo明文："+customerInfo);
		try {
			return new String(SecureUtil.base64Encode(sf.toString().getBytes(
					encoding)));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return customerInfo;
	}
	
	/**
	 * 持卡人信息域customerInfo构造，勾选对敏感信息加密权限 适用新加密规范，对pin和phoneNo，cvn2，expired加密
	 * 适用到的交易：
	 * @param customerInfoMap 信息域请求参数 key送域名value送值,必送
	 *        例如：customerInfoMap.put("certifTp", "01");					//证件类型
				  customerInfoMap.put("certifId", "341126197709218366");	//证件号码
				  customerInfoMap.put("customerNm", "互联网");				//姓名
				  customerInfoMap.put("smsCode", "123456");					//短信验证码
				  customerInfoMap.put("pin", "111111");						//密码（加密）
				  customerInfoMap.put("phoneNo", "13552535506");			//手机号（加密）
				  customerInfoMap.put("cvn2", "123");           			//卡背面的cvn2三位数字（加密）
				  customerInfoMap.put("expired", "1711");  				    //有效期 年在前月在后（加密）
	 * @param accNo  对密码加密使用到的卡号，必送
	 * @return base64后的持卡人信息域字段
	 */
	public static String getCustomerInfoWithEncrypt(Map<String,String> customerInfoMap,String accNo) {
		
		StringBuilder sf = new StringBuilder("{");
		//敏感信息加密域
		StringBuilder encryptedInfoSb = new StringBuilder("");

		Set<Map.Entry<String, String>> allSet=customerInfoMap.entrySet();
		for( Iterator<Map.Entry<String, String>> it = allSet.iterator(); it.hasNext();){
			Map.Entry<String, String> me=it.next();
			String key = me.getKey();
			String value = me.getValue();
			if(key.equals("phoneNo") || key.equals("cvn2") || key.equals("expired")){
				encryptedInfoSb.append(key + SDKConstants.EQUAL+ value + SDKConstants.AMPERSAND);
			}else{
				if(key.equals("pin")){
					value = SDKUtil.encryptPin(accNo,value,encoding);
				}
				if(it.hasNext())
					sf.append(key + SDKConstants.EQUAL+ value + SDKConstants.AMPERSAND);
				else
					sf.append(key + SDKConstants.EQUAL + value);
			}
		}
		
		if(!encryptedInfoSb.toString().equals("")){
			//去掉最后一个&符号
			encryptedInfoSb.setLength(encryptedInfoSb.length()-1);
			
			LogUtil.info(LOGGER, "组装的customerInfo encryptedInfo明文："+ encryptedInfoSb.toString());
			if(sf.toString().equals("{")) //如果没有除phoneNo，cvn2，expired之外的元素，不加&
				sf.append("encryptedInfo"+ SDKConstants.EQUAL);
			else
				sf.append(SDKConstants.AMPERSAND + "encryptedInfo"+ SDKConstants.EQUAL);
			
			sf.append(SDKUtil.encryptEpInfo(encryptedInfoSb.toString(), encoding));
		}
		
		sf.append("}");
		
		String customerInfo = sf.toString();
		LogUtil.info(LOGGER, "组装的customerInfo明文："+customerInfo);
		try {
			return new String(SecureUtil.base64Encode(sf.toString().getBytes(
					encoding)));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return customerInfo;
	}
	
	
	
	/**
	 * 有卡交易信息域(cardTransData)构造示例<br>
	 * 所有子域需用“{}”包含，子域间以“&”符号链接。格式如下：{子域名1=值&子域名2=值&子域名3=值}<br>
	 * 说明：本示例仅供参考，开发时请根据接口文档中的报文要素组装
	 * 
	 * @param contentData
	 * @param encoding
	 * @return
	 */
	public static String getCardTransData(Map<String, ?> contentData,
			String encoding) {

		StringBuilder cardTransDataBuffer = new StringBuilder();
		
		// 以下测试数据只是用来说明组装cardTransData域的基本步骤,真实数据请以实际业务为准
		String ICCardData = "uduiadniodaiooxnnxnnada";// IC卡数据
		String ICCardSeqNumber = "123";// IC卡的序列号
		String track2Data = "testtrack2Datauidanidnaidiadiada231";// 第二磁道数据
		String track3Data = "testtrack3Datadadaiiuiduiauiduia312117831";// 第三磁道数据
		String transSendMode = "b";// 交易发起方式

		// 第二磁道数据 加密格式如下：merId|orderId|txnTime|txnAmt|track2Data
		StringBuilder track2Buffer = new StringBuilder();
		track2Buffer.append(contentData.get("merId"))
				.append(SDKConstants.COLON).append(contentData.get("orderId"))
				.append(SDKConstants.COLON).append(contentData.get("txnTime"))
				.append(SDKConstants.COLON).append(contentData.get("txnAmt"))
				.append(SDKConstants.COLON).append(track2Data);

		String encryptedTrack2 = SDKUtil.encryptTrack(track2Buffer.toString(),
				encoding);

		// 第三磁道数据 加密格式如下：merId|orderId|txnTime|txnAmt|track3Data
		StringBuilder track3Buffer = new StringBuilder();
		track3Buffer.append(contentData.get("merId"))
				.append(SDKConstants.COLON).append(contentData.get("orderId"))
				.append(SDKConstants.COLON).append(contentData.get("txnTime"))
				.append(SDKConstants.COLON).append(contentData.get("txnAmt"))
				.append(SDKConstants.COLON).append(track3Data);

		String encryptedTrack3 = SDKUtil.encryptTrack(track3Buffer.toString(),
				encoding);

		// 将待组装的数据装入MAP,进行格式处理
		Map<String, String> cardTransDataMap = new HashMap<String, String>();
		cardTransDataMap.put("ICCardData", ICCardData);
		cardTransDataMap.put("ICCardSeqNumber", ICCardSeqNumber);
		cardTransDataMap.put("track2Data", encryptedTrack2);
		cardTransDataMap.put("track3Data", encryptedTrack3);
		cardTransDataMap.put("transSendMode", transSendMode);

		return cardTransDataBuffer.append(SDKConstants.LEFT_BRACE)
				.append(SDKUtil.coverMap2String(cardTransDataMap))
				.append(SDKConstants.RIGHT_BRACE).toString();
	}
	 
	/**
	 * 功能：解析全渠道商户对账文件中的ZM文件并以List<Map>方式返回
	 * 适用交易：对账文件下载后对文件的查看
	 * @param filePath ZM文件全路径
	 * @return 包含每一笔交易中 序列号 和 值 的map序列
	 */
	public static List<Map> parseZMFile(String filePath){
		int lengthArray[] = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,131};
		return parseFile(filePath,lengthArray);
	}
	
	/**
	 * 功能：解析全渠道商户对账文件中的ZME文件并以List<Map>方式返回
	 * 适用交易：对账文件下载后对文件的查看
	 * @param filePath ZME文件全路径
	 * @return 包含每一笔交易中 序列号 和 值 的map序列
	 */
	public static List<Map> parseZMEFile(String filePath){
		int lengthArray[] = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,131};
		return parseFile(filePath,lengthArray);
	}
	
	/**
	 * 功能：解析全渠道商户 ZM,ZME对账文件
	 * @param filePath
	 * @param lengthArray 参照《全渠道平台接入接口规范 第3部分 文件接口》 全渠道商户对账文件 6.1 ZM文件和6.2 ZME 文件 格式的类型长度组成int型数组
	 * @return
	 */
	 private static List<Map> parseFile(String filePath,int lengthArray[]){
	 	List<Map> ZmDataList = new ArrayList<Map>();
	 	try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	//解析的结果MAP，key为对账文件列序号，value为解析的值
        		 	Map<Integer,String> ZmDataMap = new LinkedHashMap<Integer,String>();
                    //左侧游标
                    int leftIndex = 0;
                    //右侧游标
                    int rightIndex = 0;
                    for(int i=0;i<lengthArray.length;i++){
                    	rightIndex = leftIndex + lengthArray[i];
                    	String filed = lineTxt.substring(leftIndex,rightIndex);
                    	leftIndex = rightIndex+1;
                    	ZmDataMap.put(i, filed);
                    }
                    ZmDataList.add(ZmDataMap);
                }
                read.close();
			}else{
				LogUtil.info(LOGGER,"找不到指定的文件");
			}
        } catch (Exception e) {
            LOGGER.error("读取文件内容出错",e);
        }
	 	for(int i=0;i<ZmDataList.size();i++){
	 		LogUtil.info(LOGGER,"行数: "+ (i+1));
	 		Map<Integer,String> ZmDataMapTmp = ZmDataList.get(i);

			Set<Map.Entry<Integer, String>> allSet=ZmDataMapTmp.entrySet();
			for( Iterator<Map.Entry<Integer, String>> it = allSet.iterator(); it.hasNext();){
				Map.Entry<Integer, String> me=it.next();
				Integer key = me.getKey();
				String value = me.getValue();
		 		LogUtil.info(LOGGER,"序号："+ key + " 值: '"+ value +"'");
		 	}
	 	}
		return ZmDataList;	
	}
	
	public static void main(String[] args) {
		
		SDKConfig.getConfig().loadPropertiesFromSrc();
		
		Map<String,String> customerInfoMap = new HashMap<String,String>();
		//customerInfoMap.put("certifTp", "01");
		//customerInfoMap.put("certifId", "341126197709218366");
		//customerInfoMap.put("customerNm", "互联网");
		//customerInfoMap.put("phoneNo", "13552535506");
		//customerInfoMap.put("smsCode", "123456");
		customerInfoMap.put("pin", "626262");						//密码加密
		//customerInfoMap.put("cvn2", "123");           				//卡背面的cvn2三位数字
		//customerInfoMap.put("expired", "1711");  				    //有效期 年在前月在后
		
		LogUtil.info(LOGGER,getCustomerInfoWithEncrypt(customerInfoMap,"6217001210048797565"));
		
		//parseZMFile("C:\\Users\\wulh\\Desktop\\802310048993424_20150905\\INN15090588ZM_802310048993424");
			
		}
}