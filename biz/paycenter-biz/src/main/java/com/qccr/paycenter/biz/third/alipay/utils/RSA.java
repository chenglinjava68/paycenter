
package com.qccr.paycenter.biz.third.alipay.utils;

import com.qccr.paycenter.common.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

import javax.crypto.Cipher;

public class RSA{
	private static final Logger LOGGER = LoggerFactory.getLogger(RSA.class);
	public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";

	private RSA() {
	}


	/**
	 * RSA签名
	 * @param content 待签名数据
	 * @param privateKey 商户私钥
	 * @param inputCharset 编码格式
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey, String inputCharset)
	{
		try
		{
			PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec( Base64.decode(privateKey) );
			KeyFactory keyf 				= KeyFactory.getInstance("RSA");
			PrivateKey priKey 				= keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update( content.getBytes(inputCharset) );

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		}
		catch (Exception e)
		{
			LOGGER.error("sign", e);
		}

		return null;
	}

	/**
	 * RSA验签名检查
	 * @param content 待签名数据
	 * @param sign 签名值
	 * @param aliPublicKey 支付宝公钥
	 * @param inputCharset 编码格式
	 * @return 布尔值
	 */
	public static boolean verify(String content, String sign, String aliPublicKey, String inputCharset)
	{
		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(aliPublicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update( content.getBytes(inputCharset) );

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		}
		catch (Exception e)
		{
			LOGGER.error("verify", e);
		}

		return false;
	}

	/**
	 * RSA验签名检查
	 * @param content 待签名数据
	 * @param sign 签名值
	 * @param aliPublicKey 支付宝公钥
	 * @param inputCharset 编码格式
	 * @return 布尔值
	 */
	public static boolean verifyBase64(String content, String sign, String aliPublicKey, String inputCharset)
	{
		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(aliPublicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update( content.getBytes(inputCharset) );

			boolean bverify = signature.verify( Base64.decodeBase64(sign.getBytes()) );
			return bverify;

		}
		catch (Exception e)
		{
			LOGGER.error("verify", e);
		}

		return false;
	}

	/**
	 * 解密
	 * @param content 密文
	 * @param privateKey 商户私钥
	 * @param inputCharset 编码格式
	 * @return 解密后的字符串
	 */
	public static String decrypt(String content, String privateKey, String inputCharset) throws Exception {
		PrivateKey prikey = getPrivateKey(privateKey);

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, prikey);

		InputStream ins = new ByteArrayInputStream(Base64.decode(content));
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		//rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
		byte[] buf = new byte[128];
		int bufl;

		while ((bufl = ins.read(buf)) != -1) {
			byte[] block = null;

			if (buf.length == bufl) {
				block = buf;
			} else {
				block = new byte[bufl];
				for (int i = 0; i < bufl; i++) {
					block[i] = buf[i];
				}
			}

			writer.write(cipher.doFinal(block));
		}

		return new String(writer.toByteArray(), inputCharset);
	}


	/**
	 * 得到私钥
	 * @param key 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {

		byte[] keyBytes;

		keyBytes = Base64.decode(key);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		return privateKey;
	}

	/**
	 * RSA签名
	 * @param content 待签名数据
	 * @param strPfx pfx文件路径
	 * @param strPassword 私钥密码
	 * @param inputCharset 编码格式
	 * @return 签名值
	 */
	public static String sign(String content, String strPfx, String strPassword, String inputCharset)
	{
		try
		{
			PrivateKey priKey 				= getPrikeyFromPfx(strPfx, strPassword);
			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update( content.getBytes(inputCharset) );
			byte[] signed = signature.sign();
			return Base64.encode(signed);
		}
		catch (Exception e)
		{
			LOGGER.error("sign", e);
		}

		return null;
	}

	/**
	 * Java从pfx中获取公私钥
	 * @param strPfx pfx文件路径
	 * @param strPassword 私钥密码
	 * @return
	 */
	private static  PrivateKey getPrikeyFromPfx(String strPfx, String strPassword){
		FileInputStream fis = null;
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			fis = new FileInputStream(strPfx);
			// If the keystore password is empty(""), then we have to set
			// to null, otherwise it won't work!!!
			char[] nPassword = null;
			if ((strPassword == null) || "".equals(strPassword.trim())){
				nPassword = null;
			}
			else
			{
				nPassword = strPassword.toCharArray();
			}
			ks.load(fis, nPassword);


			Enumeration enumas = ks.aliases();
			String keyAlias = null;
			if (enumas.hasMoreElements())// we are readin just one certificate.
			{
				keyAlias = (String)enumas.nextElement();
			}
			// Now once we know the alias, we could get the keys.
			PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
			return prikey;
		} catch (IOException|CertificateException|UnrecoverableKeyException|NoSuchAlgorithmException|KeyStoreException e){
			LOGGER.error("getPrikeyFromPfx", e);
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(),e);
			}
		}
		return null;
	}

	/**
	 * RSA验签名检查
	 * @param content 待签名数据
	 * @param strPfx pfx文件路径
	 * @param strPassword 私钥密码
	 * @param inputCharset 编码格式
	 * @return 签名值
	 */
	public static boolean verifyFromPfx(String content, String sign, String strPfx, String strPassword, String inputCharset)
	{
		try
		{
			PublicKey pubKey 				= getPubkeyFromPfx(strPfx, strPassword);
			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update( content.getBytes(inputCharset) );
			LogUtil.info(LOGGER, "sign+++>>>"+sign);
			boolean bverify = signature.verify( Base64.decode(sign) );
			return bverify;
		}
		catch (Exception e)
		{
			LOGGER.error("verifyFromPfx", e);
		}
		return false;
	}

	/**
	 * Java从pfx中获取公私钥
	 * @param strPfx pfx文件路径
	 * @param strPassword 私钥密码
	 * @return
	 */
	private static  PublicKey getPubkeyFromPfx(String strPfx, String strPassword){
		FileInputStream fis = null;
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			fis = new FileInputStream(strPfx);
			char[] nPassword = null;
			if ((strPassword == null) || "".equals(strPassword.trim())){
				nPassword = null;
			}
			else
			{
				nPassword = strPassword.toCharArray();
			}
			ks.load(fis, nPassword);

			Enumeration enumas = ks.aliases();
			String keyAlias = null;
			if (enumas.hasMoreElements())// we are readin just one certificate.
			{
				keyAlias = (String)enumas.nextElement();
			}
			// Now once we know the alias, we could get the keys.
			Certificate cert = ks.getCertificate(keyAlias);
			PublicKey pubkey = cert.getPublicKey();
			return pubkey;
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (CertificateException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (KeyStoreException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(),e);
			}
		}
		return null;
	}


	/**
	 * RSA验签名检查
	 * @param content 待签名数据
	 * @param sign 签名值
	 * @param aliPublicKeyPath 支付宝公钥
	 * @param inputCharset 编码格式
	 * @return 布尔值
	 */
	public static boolean verifyByPath(String content, String sign, String aliPublicKeyPath, String inputCharset)
	{
		try
		{
			X509Certificate encryptCert = initCert(aliPublicKeyPath);
			PublicKey pubKey = encryptCert.getPublicKey();
			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update( content.getBytes(inputCharset) );
			boolean bverify = signature.verify( Base64.decode(sign) );
			return bverify;
		}
		catch (Exception e)
		{
			LOGGER.error("verifyByPath", e);
		}
		return false;
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public static X509Certificate initCert(String path) {
		X509Certificate encryptCertTemp = null;
		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			in = new FileInputStream(path);
			encryptCertTemp = (X509Certificate) cf.generateCertificate(in);
			// 打印证书加载信息,供测试阶段调试
			LogUtil.info(LOGGER, "[" + path + "][CertId="
					+ encryptCertTemp.getSerialNumber().toString() + "]");
		} catch (CertificateException e) {
			LOGGER.error("InitCert Error", e);
		} catch (FileNotFoundException e) {
			LOGGER.error("InitCert Error File Not Found", e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					LOGGER.error("initCert", e);
				}
			}
		}
		return encryptCertTemp;
	}
}
