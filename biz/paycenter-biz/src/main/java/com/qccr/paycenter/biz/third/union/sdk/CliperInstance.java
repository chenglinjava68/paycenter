package com.qccr.paycenter.biz.third.union.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class CliperInstance {
	private CliperInstance() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CliperInstance.class);
	private static ThreadLocal<Cipher> cipherTL = new ThreadLocal<Cipher>() {
		@Override
		protected Cipher initialValue() {
			try {
				return Cipher
						.getInstance(
								"RSA/ECB/PKCS1Padding",
								new org.bouncycastle.jce.provider.BouncyCastleProvider());
			} catch (Exception e) {
				LOGGER.error("cipherTL",e);
				return null;
			}
		}
	};

	public static Cipher getInstance() throws NoSuchAlgorithmException,
			NoSuchPaddingException {
		return cipherTL.get();
	}
}
