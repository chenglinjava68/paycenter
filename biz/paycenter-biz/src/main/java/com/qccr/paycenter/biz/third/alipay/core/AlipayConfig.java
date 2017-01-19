package com.qccr.paycenter.biz.third.alipay.core;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

import com.qccr.paycenter.model.constants.PayCenterConstants;

public class AlipayConfig {
	private AlipayConfig() {
	}

	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static final String PARTNER = "2088911040582451";
	// 商户的私钥
	public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMiQDg01/T7ulYoWC+Vg44VH0tii2MG/1C3FnkxTiSkTdQp5TYpVGdnRsrizFpxekdt3gRqY7bjjiiPZo0ZIdljwDe3FM8+IAP8/z9vZsOLkT2jW0m/8NoV2TCHlgt5HX752D2HMp9Adn697UWkudFsI5/alFLIMJqdIYIDwj/H1AgMBAAECgYBSz8g9hDFQrspn/bpjt6G6AnH4qSS+uyoszBAUi9lycGo/LN7rY0ANRBx7TtbjMdkfuTRBeyuMz8bWDqLv/jN+wmCAjcNh2pVMWpafgNwK8y74wCN2DemdpjJ87FPrsCrC3OFFfkW5aey95Vt/cErlIz5pUEhvJm4GU6E7T5FYAQJBAONHIkKN+3k0+O4H6x26LWO/Pd17qTfjKRecOUHeQho/gvsS5u2S3JwqvjYxDTg375P3VgS8PwRH1ijDLmDt4nUCQQDh6KK7tr9NSCfskFS3nb6hIHWbQx2jhRbRf3rB+/Lor0ida8nGpLcdCsKNp/BhJ6s85oS2IDjD24R6ZroNCOGBAkA3AFv2J04YQw2K7Tek9g1N+lYKZ4bIE506LYBdxF/S4lPcVvKzw7rHwEBP9Qbx9/duo4iZlMTbUFcvfiyXIHqRAkAZED1VxC2vPk0nos+zW2315GsH4cddB/wDHDibYv+NLz1IMdg+ELI8J6B0JOi3brZB2HVq22JQ4H7vMYx8u7qBAkEApikar7KW3GuHsl2m4kDwoACTDsH9Zmkqp26ZiA3G+c+21DcSKvJRZvJswkAgLE5xk+8rZDrfYxxCe+RekMW5nA==";

	public static final String APP_ID = "2016060101467661";

	public static final String APP_PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIkA4NNf0+7pWKFgvlYOOFR9LYotjBv9QtxZ5MU4kpE3UKeU2KVRnZ0bK4sxacXpHbd4EamO2444oj2aNGSHZY8A3txTPPiAD/P8/b2bDi5E9o1tJv/DaFdkwh5YLeR1++dg9hzKfQHZ+ve1FpLnRbCOf2pRSyDCanSGCA8I/x9QIDAQAB";

	// 支付宝的公钥，无需修改该值
	public static final String ALI_PUBLIC_KEY  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	public static final String ALI_APP_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static final String INPUT_CHARSET = "utf-8";
	
	// 签名方式 不需修改
	public static final String SIGN_TYPE = "RSA";
			
	// 收款支付宝账号
	public static final String SELLER_EMAIL = "toowell@toowell.com";
	
	// 商户的私钥
	public static final String MD5_PRIVATE_KEY = "uo3290ftr2zzbgeznqepgudceyn5j3sd";
	
	// 签名方式 不需修改
	public static final String MD5_SIGN_TYPE = "MD5";
	
	// 调试用，创建TXT日志文件夹路径
	public static final String LOG_PATH = "D:\\log\\alipay";

	/**
	 * 当面付相关
	 */
	public static final String FACEPAY_APPID = "2016102702363976";
	public static final String FACEPAY_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL3tcR3lO5IVmD8eEPcv4lFKJ46FjQwVAWXpk2opv5Epu2kGou/uhYClu/eRbpj+IKIGPCnFFQOYVrC/OU6L28JFTtUFsc3IDKRnSPrfR+k+xvzLk/7b/ItQdueOUyapKkzfVhjloUEV2zuIm0FktE2oE8RRnjKV0Dwb3+sqQenbAgMBAAECgYEAocR3sErrLkYOYtQtBx1V2n8aqh2+QSgzBKap1Kayb+XnplDsvwed1jFTpE3OsajiCp7c54bhmypJzElTej9NcPcMuBw+HoPxo3QqA2R1QIO1ElvjRdKtucCA1lALwWEOI64u827SZH19zb6jsmy2tWeegc6sa1rN67UAQFLxEMECQQDeNI6y3m6NJgKgZ+IfNU2OPaSCfGkBC50FTfXj3yr5BmdiGaTLj4hCYWYp/jphOCFyQmniY8PA2PX8XuMHYFbDAkEA2tAqgjMjDNGAawCdaF0jXMkoEkCdVLcWjMkLH8tjlq4VYrv/JXijh0eqDkaCTmr5WR0EvbcFTGutgBITu9nfCQJAYMXG+AODZhmNv0o+4pSWSEJ0aRfBq2Dha7P+SNWio320fLbqXHUQRMiic9tylXCasGZYMu58DHrw9o5klYvRfQJAV+00V7NyzzDUeKr7lbdvgVZ32rXKQwN7dUFIThNtxlvXziAbTBRpKfA7PpdIv3gvEIcfo5iK8YWHZ6Jb+a13+QJBAK954H/hDmwWPDSp2qOwsr173dO0tc+KOWvfgz+jq8FLjhhpn3Yjy2OY+Ql8Px+AyNsE0vvtaSPP3T/dUXwf9F4=";
	public static final String FACEPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC97XEd5TuSFZg/HhD3L+JRSieOhY0MFQFl6ZNqKb+RKbtpBqLv7oWApbv3kW6Y/iCiBjwpxRUDmFawvzlOi9vCRU7VBbHNyAykZ0j630fpPsb8y5P+2/yLUHbnjlMmqSpM31YY5aFBFds7iJtBZLRNqBPEUZ4yldA8G9/rKkHp2wIDAQAB";
	public static final String FACEPAY_ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	public static final String FACEPAY_APP2APP_AUTH = "https://openauth.alipay.com/oauth2/appToAppAuth.htm";
	public static final String FACEPAY_NOTIFY_URL = PayCenterConstants.OUT_IP + "/auth/notify/alipay/facepay";

	/**
	 * 下面两个使用于对当面付授权URL签名和验签
	 */
	public static final String FACEPAY_URL_PRIVATE_KEY= "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCTb2oY0H+/QkdFiyT2Crst5AJidYcHZurVUSX9j21do95sabuThOVN3U2WfLkKhN8uUalBqetdhtEh8rVumnJbxV/fUzC/Vil0zX+98F7MYkE/bU+Umm2OF5Tnl6VRqNxhC9JbGSQ0Dv5p7REIrE3vv8iyAK6k0MtkRePhIz96ivi28234ht0LrD4wstqABBQ/8WigVGUWXDM+0Int/ajCJVnuazFW1HkTSK6JvWNAS5e3P3fv6bXg9GdV6LJbCu6i2d6vXPpbfb0Fvs32CiiYujUUUwxqyc7I9FJwFeSUWG/+cy0X+XQdYIA/OYsAWXTPU9+8/9zPhs/La8/unZZ3AgMBAAECggEAB+7yWBR8E0gXOrRCkGTCbrU6tZ6aXfkZQNJFh4/ctDG5qb7Ew6reheKXJrZeZKUCt2TYhTqF+jnjbf636ZQw/CQyRPuef2U9BNZ7/bX9++R7m1Sf+CCZLZCySAX+dNbvyN8Qeitvqcd2xC9n/7350aFRxNZJj3/WufYSzY4Of+L+kUu5HeEDys6UjUTNFl6XVjCqGChZLlwdN0+BFkrGh+X08SqD2ITVhD3ZU3Zj/ZTGDz2cRsSz36LKctjNtc/2vZdxSRVo9fscPu5PodkEyQdENfNKvF8MQd8MH9id94dMbyyUwMCFLTa4UZMUFEBjsKYa6QJ6ee9lXXi+0kOr7QKBgQDL/sFXtiMIxWkH5Ab4iFS2NQYalCrxS1wrv8XQ/T0i9lPUioQRvuVBeD2+xqh+EvXymiw0420UbAm/nMo7h++d1ka9o4zUJh/YD58pyFuIghm9uBEXpuB2i5vak8HwCS6twJSNs2jP0E2aAJ/MSG30A5tnSV280SV6Ko3ntDCWXQKBgQC5BWnq4KI17N+NRvJ9cyc0LBgcTDrHCtMNYbwJ7taalN3DvSjIeqqT5wVzbG0G3MXvvQWmQZcd7fQx7j0qvQ/g2zmqt6CGXbkzQA5AK3UDUeU7IOmYvdRR0i0Ff21pqYYsMYcUm1EXdAcQeO/LCQuOqIcaU4TVqhvXShwicoEq4wKBgQCZhSKqSNc63Z46owAgADGIYoUycXmT2DwIbK2Jp1tgOXNDS+8yj7Bmwf/t2AGrx8cyiZZlQxavPNNlUbDcdmP9K+pXA4OTkRF7/92viypfpFGM9r5eR2c7r7teFr7oT3DCsGDb9z4Uz1XnZTrdeVDOR/ynEY6q79c9hlL/IhtCvQKBgCqNrTOhII2ogi37EFB8CJs+PPldKvOWcL4WKWpbbS42aN7ZBaU+WTcVHjLS0CyQ4FFoNwFq+neev9gwBA9tVlaP9S5nd1sP+UVpSR3HIO13i3JS+obvugJrhYFLMn3nyH0nkvrRUhBD1lAakIQw+2B/MDuE0/tUWXzlhZ0CmuSjAoGAKklJ1kM63VMLAr0Zva4T8/CBWs8yZq3s4eI4cOMZt7LZiHLnQpVY+IBXURpbT660torOzgoS2ifIZJowgMV2q+25bcf6YRThYYR8qIE/7KZeA0xF7UcBAD0HESd8BW55Pm1aGn8Q1pNSvaF5ZR1TcYUhoT65SXv9X6gKz8+CaeE=";
	public static final String FACEPAY_URL_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk29qGNB/v0JHRYsk9gq7LeQCYnWHB2bq1VEl/Y9tXaPebGm7k4TlTd1Nlny5CoTfLlGpQanrXYbRIfK1bppyW8Vf31Mwv1YpdM1/vfBezGJBP21PlJptjheU55elUajcYQvSWxkkNA7+ae0RCKxN77/IsgCupNDLZEXj4SM/eor4tvNt+IbdC6w+MLLagAQUP/FooFRlFlwzPtCJ7f2owiVZ7msxVtR5E0iuib1jQEuXtz937+m14PRnVeiyWwruotner1z6W329Bb7N9goomLo1FFMMasnOyPRScBXklFhv/nMtF/l0HWCAPzmLAFl0z1PfvP/cz4bPy2vP7p2WdwIDAQAB";

}
