package com.qccr.paycenter.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class SerialUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SerialUtil.class);

	private SerialUtil() {
	}

	/**
	 * 创建支付流水号
	 * @param date
	 * @param businessNo
     * @return
     */
	public static String createPayNo(Date date, String businessNo){
		return createNewNo(date,"10",businessNo);
	}

	/**
	 * 创建退款流水号
	 * @param date
	 * @param businessNo
     * @return
     */
	public static String createRefundNo(Date date, String businessNo){
		return createNewNo(date,"20",businessNo);
	}

	/**
	 * 创建操作流水号
	 * @param date
	 * @param businessNo
     * @return
     */
	public static String createSerialNo(Date date, String businessNo){
		return createNewNo(date,"11",businessNo);
	}

	/**
	 * 原来是退款操作流水号，将不会用
	 * @param date
	 * @param businessNo
     * @return
     */
	public static String createRefundSerialNo(Date date, String businessNo){
		return createNewNo(date,"21",businessNo);
	}

	/**
	 * 创建补偿退款流水号
	 * @param date
	 * @param businessNo
     * @return
     */
	public static String createCompensateNo(Date date, String businessNo){
		return createNewNo(date,"30",businessNo);
	}

	/**
	 * 创建主支付单号
	 * @param date
	 * @param businessNo
     * @return
     */
	public static String createTradeNo(Date date, String businessNo){
		return createNewNo(date,"00",businessNo);
	}

	/**
	 * 创建业务子订单号，使用在一个订单多笔支付里面
	 * @param num
	 * @param businessNo
     * @return
     */
	public static String createPayBusinessNo(int num, String businessNo){
		num += 1000;
		StringBuilder bizNo = new StringBuilder(String.valueOf(num));
		bizNo.append(businessNo).toString();
		return bizNo.toString();
	}
	/**
	 * 20160531 lim
	 * 1.pay_no,serial_no,refund_no,refund_serial_no规则 --->>>分库分表做准备
	 * 17位时间+2位标识+10位订单编号后十位=共29位
	 * @param date
	 * @param tag
	 * @param businessNo
	 * @return
	 */
	private static String createNewNo(Date date, String tag, String businessNo){
		businessNo = judgeBusinessNo(businessNo);
		if(StringUtil.isNullOrEmpty(businessNo) || businessNo.length()<10) {
			throw new RuntimeException("参数有误");
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(DateUtil.formatDate(date, "yyyyMMddHHmmssSSS"));
		stringBuilder.append(tag);
		int len = businessNo.length();
		stringBuilder.append(businessNo.substring(len - 10, len));
		return stringBuilder.toString();
	}

	/**
	 * 票据号，6位批次号+6位凭证号，每天唯一(不可重复提交，由0~9数字组成)
	 * 使用在交通银行
	 * @return
     */
	public static String createInvioceNo() {
		StringBuilder stringBuilder = new StringBuilder();
//		stringBuilder.append(DateUtil.formatDate(new Date(), "HHmmssSSS"));
//		stringBuilder.append((int)(Math.random()*900)+100);
		stringBuilder.append((int)((Math.random()*9+1)*100000));
		stringBuilder.append((int)((Math.random()*9+1)*100000));
		return stringBuilder.toString();
	}

	/**
	 * 流水号
	 * @return
     */
	public static String createTraceNo() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append((int)((Math.random()*9+1)*100000));
		return stringBuilder.toString();
	}

	/**
	 * 签约获取验证码时使用
	 * 00(数字0)+W+ N+LL+客户姓名（最大长度不超过 10个字节(客户姓名可包含中文、英文、数字随意组合，但必须包含中文)）
	 * +M+LL+商户名称（最大长度不超过40字节）,
	 */
	public static String createVerifyCodeAddDate(String cardName, String merName) {
		//00WN06测试09M30特维轮网络科技（杭州）有限公司
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("00WN");
		stringBuilder.append(String.format("%02d", calculatePlaces(cardName)));//06是客户姓名的长度
		stringBuilder.append(cardName);
		stringBuilder.append("M");
		stringBuilder.append(String.format("%02d", calculatePlaces(merName)));//30是商户名称的长度
		stringBuilder.append(merName);
		return stringBuilder.toString();
	}

	/**
	 * 00(数字0)+W +N+LL+客户姓名（最大长度不超过 10个字节(客户姓名可包含中文、英文、数字随意组合，但必须包含中文)）
	 * +M+00+P+2位序号+6位动态验证代码+6位批次号+6位凭证号（获取动态密码时的票据号）
	 */
	public static String createSignAddDate(String cardName, String verifyCode, String invioceNo, String serialNo) {
//        00WN06测试12M00P03780375000058812397
//        00WN06测试12M00P03780375000058812397
//        00WN06测试09M00P06973567816784115516
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("00WN");
		stringBuilder.append(String.format("%02d", calculatePlaces(cardName)));
		stringBuilder.append(cardName);
		stringBuilder.append("M00P");
		stringBuilder.append(serialNo);//2位序号
		stringBuilder.append(verifyCode);//6位动态验证代码
		stringBuilder.append(invioceNo);//6位批次号+6位凭证号（获取动态密码时的票据号）
		return stringBuilder.toString();
	}

	/**
	 * 00(数字0)+ Z+M+LL+商户名称+O（大写字母O）+订单编号(20位)
	 * +P+2位序号+6位动态验证代码+6位批次号（对应动态验证码交易）+6位凭证号（对应动态验证码交易）
	 如果无动态密码，则上述黄色部分不需要发送。--- 黄色部分
	 LL为2位长度信息，不足前面补充0
	 商户名称一般不允许超过40个字符位，20个汉字。
	 超过55将报告错误。超过40将截取。
	 */
	public static String createPayAddDate(String merName, String bizNo, String dynamicCode, String invioceNo, String serialNo) {
//        00ZM20杭州卷瓜网络有限公司O00000000897722222791P40575898897722222791
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("00ZM");
		stringBuilder.append(String.format("%02d", calculatePlaces(merName)));
		stringBuilder.append(merName);
		stringBuilder.append("O");
		int size = 20-calculatePlaces(bizNo);
		int index = 0;
		while(index<size) {
			stringBuilder.append("0");
			index++;
		}
		stringBuilder.append(bizNo);
		if(StringUtils.isNotBlank(dynamicCode)) {
			stringBuilder.append("P");
			stringBuilder.append(serialNo);
			stringBuilder.append(dynamicCode).append(invioceNo);
		}
		return stringBuilder.toString();
	}
	/**
	 * 计算位数
	 * @param str
	 * @return
	 */
	public static int calculatePlaces(String str)
	{
		int m = 0;
		char arr[] = str.toCharArray();
		for(int i=0;i<arr.length;i++)
		{
			char c = arr[i];
			if((c >= 0x0391 && c <= 0xFFE5))  //中文字符
			{
				m = m + 2;
			}
			else if((c>=0x0000 && c<=0x00FF)) //英文字符
			{
				m = m + 1;
			}
		}
		return m;
	}

	public static String padding(int targetIndex,String paddingStr,String source){
		int index = 0;
		StringBuilder stringBuilder = new StringBuilder();
		int size =  targetIndex - calculatePlaces(source);
		while(index<size) {
			stringBuilder.append("0");
			index++;
		}
		stringBuilder.append(source);
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		String no = ("B213ZD5091016077|91016074|B213ZD5091016070|B213ZD5091016069|213ZD5091016059|BTWL7001016244_1|BTWL7001016244");
		String[] nos = no.split("\\|");
		for (String buno:nos ) {
//			LogUtil.info(LOGGER, buno, createPayNo(new Date(), buno));
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(createPayNo(new Date(), buno) + "   " + buno);
		}
//		for(int i=0; i<30; i++) {
//			String t = createInvioceNo();
//			LogUtil.info(LOGGER, t);
//		}
	}

	/**
	 * 判断业务单号，防止非数字的进到流水里面
	 * @param businessNo
	 * @return
     */
	private static String judgeBusinessNo(String businessNo) {
		//2.非正常，后12位里面带了下划线，去掉后两位
		if(!StringUtil.isNullOrEmpty(businessNo)) {
			int len = businessNo.length();
			if(len >= 12) {
				String afterTen = businessNo.substring(len - 10, len);
				if(StringUtils.isNumeric(afterTen)) {
					return afterTen;//1.正常的订单号，后十位都是数字
				}else {
					String midTen = businessNo.substring(len - 12, len-2);
					if(StringUtils.isNumeric(midTen)) {
						return midTen;
					}
				}
			}
		}
		//3.其他情况，
		StringBuilder bno = new StringBuilder();
		bno.append((int)((Math.random()*9+1)*10000));
		bno.append((int)((Math.random()*9+1)*10000));
		return bno.toString();
	}
}
