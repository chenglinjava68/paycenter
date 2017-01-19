package com.qccr.paycenter.biz.third.bocom.utils;

/**
 * Created by user on 2016/8/31.
 */

import com.qccr.paycenter.biz.third.alipay.utils.Base64;
import com.qccr.paycenter.biz.third.alipay.utils.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class RSATool {

    /**
     *
     * @param k
     * @param data
     * @param encrypt
     *            1 加密 0解密
     * @return
     * @throws NoSuchPaddingException
     * @throws Exception
     */
    private static byte[] handleData(Key k, byte[] data, int encrypt)
            throws Exception {

        if (k != null) {

            Cipher cipher = Cipher.getInstance("RSA");

            if (encrypt == 1) {
                cipher.init(Cipher.ENCRYPT_MODE, k);
                byte[] resultBytes = cipher.doFinal(data);
                return resultBytes;
            } else if (encrypt == 0) {
                cipher.init(Cipher.DECRYPT_MODE, k);
                byte[] resultBytes = cipher.doFinal(data);
                return resultBytes;
            } else {
                System.out.println("参数必须为: 1 加密 0解密");
            }
        }
        return null;
    }

    public static void main2(String[] args) throws Exception {

        String prifile = "d:/data/html/paycenter/certs/qccr4bocom-private-rsa.pfx";
        String pubfile = "d:/data/html/paycenter/certs/qccr4bocom-public-rsa.cer";
        String pwdA = "qccr2016nb";

        X509Certificate encryptCert = RSA.initCert(pubfile);
        PublicKey pubkey = encryptCert.getPublicKey();
        PrivateKey prikey 				= getPrikeyFromPfx(prifile, pwdA);
        // 使用公钥加密
        String msg = "~O(∩_∩)O哈哈~";
        String enc = "UTF-8";

        msg = "嚯嚯";
        // 使用私钥加密公钥解密
        System.out.println("原文: " + msg);
        byte[] result2 = handleData(prikey, msg.getBytes(enc), 1);
//        System.out.println("加密: " + new String(result2, enc));
        String str1 = Base64.encode(result2);
        System.out.println("加密: " + str1);

//        byte[] deresult2 = handleData(pubkey, result2, 0);
        byte[] deresult2 = handleData(pubkey, Base64.decode(str1), 0);
        System.out.println("解密: " + new String(deresult2, enc));

//        System.out.println("解密: " + Base64.encode(deresult2));

    }

    /**
     * 私钥加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String prifile, String pwdA) throws Exception {
        PrivateKey prikey = getPrikeyFromPfx(prifile, pwdA);
        String enc = "UTF-8";
        // 使用私钥加密公钥解密
        byte[] result2 = handleData(prikey, data.getBytes(enc), 1);
        String str1 = Base64.encode(result2);
        return str1;
    }

    /**
     * 公钥解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, String pubfile) throws Exception {
        X509Certificate encryptCert = RSA.initCert(pubfile);
        PublicKey pubkey = encryptCert.getPublicKey();
        String enc = "UTF-8";
        // 使用私钥加密公钥解密
        byte[] deresult2 = handleData(pubkey, Base64.decode(data), 0);
        String result =  new String(deresult2, enc);
        return result;
    }

    public static void main(String[] args) throws Exception {
//        String ss = encryptByPrivateKey("22222222224");
//        System.out.println(ss);
//        System.out.println(decryptByPublicKey(ss));

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
        } catch (IOException|CertificateException |UnrecoverableKeyException |NoSuchAlgorithmException|KeyStoreException e){
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
    private static final Logger LOGGER = LoggerFactory.getLogger(RSATool.class);

}