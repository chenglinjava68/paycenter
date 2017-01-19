package com.qccr.paycenter.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {
	private StringUtil() {
	}

	private static final  Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);
	private static char[] hex = "0123456789ABCDEF".toCharArray();
	public static final String ENCODING = "UTF-8";

	
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
    
    
	/**
	 * 参数检测，判断是否为 null 或者 ""
	 * 
	 * @param needCheck
	 *            : 待检测字符串
	 * @return true: 输入字符串为 null 或者trim后为""<br>
	 *         false：输入字符串 不为null 并且 trim后不等于 ""
	 */
	public static final boolean isNullOrEmpty(final String needCheck) {
		return needCheck == null || needCheck.trim().isEmpty();
	}

	/**
	 * 参数检测，判断是否为 null 或者 "" 或者由空白组成
	 * 
	 * @param str1
	 *            : 待检测字符串(一个或多个)
	 * @return true: 输入字符串任一个 为 null 或者 ""<br>
	 *         ———— needCheck==null || needCheck.trim().equals("")<br>
	 *         false：输入字符串 全部 不为null不等于 ""
	 */
	public static boolean isNullOrEmpty(final String str1, final String str2) {
		return str1 == null || str1.trim().isEmpty() || str2 == null || str2.trim().isEmpty();
	}

	/**
	 * 参数检测，判断是否为 null 或者 "" 或者由空白组成
	 * 
	 * @param str1
	 *            : 待检测字符串(一个或多个)
	 * @return true: 输入字符串任一个 为 null 或者 ""<br>
	 *         ———— needCheck==null || needCheck.trim().equals("")<br>
	 *         false：输入字符串 全部 不为null不等于 ""
	 */
	public static boolean isNullOrEmpty(final String str1, final String str2, final String str3) {
		boolean bool = (str1 == null || str1.trim().isEmpty() || str2 == null || str2.trim().isEmpty() || str3 == null || str3.trim().isEmpty());
		return bool;
	}

	/**
	 * 参数检测，判断是否为 null 或者 "" 或者由空白组成
	 * 
	 * @param str1
	 *            : 待检测字符串(一个或多个)
	 * @return true: 输入字符串任一个 为 null 或者 ""<br>
	 *         ———— needCheck==null || needCheck.trim().equals("")<br>
	 *         false：输入字符串 全部 不为null不等于 ""
	 */
	public static boolean isNullOrEmpty(final String str1, final String str2, final String str3, final String str4) {
		boolean bool = (str1 == null || str1.trim().isEmpty() || str2 == null || str2.trim().isEmpty() || str3 == null || str3.trim().isEmpty() || str4 == null || str4.trim().isEmpty());
		return bool;
	}

	/**
	 * 参数检测，判断是否为 null 或者 "" 或者由空白组成
	 * 
	 * @param needCheck
	 *            : 待检测字符串(一个或多个)
	 * @return true: 输入字符串任一个 为 null 或者 ""<br>
	 *         ———— needCheck==null || needCheck.trim().equals("")<br>
	 *         false：输入字符串 全部 不为null不等于 ""
	 */
	public static boolean isNullOrEmpty(String... needCheck) {
		if(needCheck == null || needCheck.length == 0){
			return true;
		}
		for (String curr : needCheck) {
			if (curr == null || curr.trim().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否是数字，含null与""的验证
	 * 
	 * @return true: 数字 false：含有其他字符
	 */
	public static boolean isNumeric(String str) {
		if (str == null || "".equals(str)) {
			return false;
		}
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	

	/**
	 * 判断手机号码是否有效 增加了14x,17x的手机号码判断
	 * 
	 * @param num
	 * @return true 是正确的手机号码;
	 */
	public static boolean isMobileNum(String num) {
		if (isNullOrEmpty(num))
			return false;
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(17[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(num);
		return m.matches();
	}

	public static String getSerialNo() {
		return UUID.randomUUID().toString();
	}

	/**
	 * base64解密，异常返回null
	 * 
	 * @param encoded
	 * @return
	 */
	public static String base64Decode(final String encoded) {

		if (encoded.length() < 77) {
			try {
				return new String(new BASE64Decoder().decodeBuffer(encoded));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(),e);
				return null;
			}
		}
		// 超过76换行
		BufferedReader br = null;
		try {
			br = new BufferedReader(new StringReader(encoded));
			StringBuilder s = new StringBuilder(300);
			for (String line; (line = br.readLine()) != null;) {
				s.append(line);
			}
			return new String(new BASE64Decoder().decodeBuffer(s.toString()));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
			return null;
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(),e);
				}
		}
	}

	public static String base64Encode(final String src) {
		try {
			return new BASE64Encoder().encodeBuffer(src.getBytes(ENCODING));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return null;
	}
	/**
	 * unicode转换成中文
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuilder outBuffer = new StringBuilder(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException(
										"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';

					else if (aChar == 'n')

						aChar = '\n';

					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}
	/**
	 * 加盐加密
	 * @param originStr
	 * @return
	 */
	public static String md5Encrypt(final String originStr){
		MessageDigest messageDigest = null;
		byte[] b = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			b = messageDigest.digest((originStr).getBytes(ENCODING));
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
		}
		StringBuilder s = new StringBuilder();
		if(b != null) {
			for (int i = 0; i < b.length; i++) {
				s.append(hex[(b[i] >>> 4) & 0xf]);
				s.append(hex[b[i] & 0xf]);
			}
		}
		return s.toString();
	}

	private static final char[] LowHex = "0123456789abcdef".toCharArray();
	/**
	 * 不加盐，小写表示
	 * @param originStr
	 * @return
	 */
	public static String md5EncryptLowerCase(final String originStr){
		MessageDigest messageDigest = null;
		byte[] b = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			b = messageDigest.digest((originStr).getBytes(ENCODING));
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
		}
		StringBuilder s = new StringBuilder();
		if(b != null) {
			for (int i = 0; i < b.length; i++) {
				s.append(LowHex[(b[i] >>> 4) & 0xf]);
				s.append(LowHex[b[i] & 0xf]);
			}
		}
		return s.toString();
	}
	
	public static String md5EncryptByEncoding(final String originStr, final String encoding){
		MessageDigest messageDigest = null;
		byte[] b = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			b = messageDigest.digest((originStr).getBytes(encoding));
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
		}
		StringBuilder s = new StringBuilder();
		if(b != null) {
			for (int i = 0; i < b.length; i++) {
				s.append(LowHex[(b[i] >>> 4) & 0xf]);
				s.append(LowHex[b[i] & 0xf]);
			}
		}
		return s.toString();
	}
	
	/**
	 * 不加盐，大写表示
	 * @param originStr
	 * @return
	 */
	public static String md5EncryptWithoutSalt(final String originStr){
		MessageDigest messageDigest = null;
		byte[] b = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			b = messageDigest.digest((originStr).getBytes(ENCODING));
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
		}
		
		StringBuilder s = new StringBuilder();
		if(b != null) {
			for (int i = 0; i < b.length; i++) {
				s.append(hex[(b[i] >>> 4) & 0xf]);
				s.append(hex[b[i] & 0xf]);
			}
		}
		return s.toString();
	}
	
	 public static String hideMiddle(String phone){
		 String localPhone = phone;
		 if(phone.length()>11) {
			 localPhone = phone.substring(phone.length() - 11);
		 }
		 localPhone = localPhone.substring(0, 3) + "****" + localPhone.substring(localPhone.length()-4);
		 return localPhone;
	 }
	 
	 public static int getVersionNum(String version){
		 String localVersion = version;
		 if(version.charAt(0)=='V'){//V1.1.1
			 localVersion = version.substring(1);
		 }
		 if(localVersion.length()>=5){
			 return(localVersion.charAt(0)-'0')*100 + (localVersion.charAt(2)-'0')*10 + (localVersion.charAt(4)-'0');
		 }else{
			 return(localVersion.charAt(0)-'0')*100 + (localVersion.charAt(2)-'0')*10;
		 }
		
	 }
	 
	 /**
	  * 按照“参数参数值”的形式拼接 按照key字母升序排列
      * @param params 需要排序并参与字符拼接的参数组，来自request
      * @return 拼接后字符串
	  */
	 public static String asciiSort4DianPing(final Map<String, String[]> params, final String key) {
		 
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		
		StringBuilder s = new StringBuilder(200);
		for (String currKey : keys) {
			if("sign".equals(currKey))
				continue;
			s.append(currKey).append(params.get(currKey)[0]);
		}
		s.append(key);
		return s.toString();
	 }
	 
	 /**
	  * 按照“参数参数值”的形式拼接 按照key字母升序排列
      * @param params 需要排序并参与字符拼接的参数组，来自request
      * @return 拼接后字符串
	  */
	 public static String asciiSort(final Map<String, String[]> params, final String key) {
		List<String> filterList = Arrays.asList("sign", "_", "callback");
		 
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		
		StringBuilder s = new StringBuilder(200);
		for (String currKey : keys) {
			if(filterList.contains(currKey))
				continue;
			s.append(currKey).append(params.get(currKey)[0]);
		}
		s.append(key);
		return s.toString();
	 }

	public static Integer parseInt(String intStr){
		return Integer.parseInt(intStr);
	}

}
