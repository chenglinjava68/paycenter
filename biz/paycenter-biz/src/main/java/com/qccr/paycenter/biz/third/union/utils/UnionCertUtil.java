package com.qccr.paycenter.biz.third.union.utils;

import com.qccr.knife.result.CommonStateCode;
import com.qccr.paycenter.biz.third.union.sdk.SDKConstants;
import com.qccr.paycenter.biz.third.union.sdk.SecureUtil;
import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.config.PayCenterConfig;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/8/25 14:53 $
 */

public class UnionCertUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnionCertUtil.class);
    private static final String BC_PROV_ALGORITHM_SHA1RSA = "SHA1withRSA";

    private static Map <String,PrivateKey> priKeys = new HashMap();
    private static Map <String,PublicKey> pubKeys = new HashMap();
    private static Map<String,String > certIds = new HashMap();
    private static final AtomicBoolean inited = new AtomicBoolean(false);
//    static{
//        init();
//    }

    public static void init(){
        if(inited.compareAndSet(false,true)){
            c2bInit();
            b2bInit();
            c2bAppInit();
        }

    }

    private static void b2bInit(){
        String privateKeyPath =  PayCenterConfig.getProperty("union_b2b_private_cert").toString();
        String publicKeyPath =  PayCenterConfig.getProperty("union_b2b_public_cert").toString();
        String certPwd = PayCenterConfig.getProperty("union_b2b_private_cert_pwd").toString();
        X509Certificate encryptCertTemp = null;
        CertificateFactory cf = null;
        FileInputStream in = null;
        FileInputStream fis = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            in = new FileInputStream(publicKeyPath);
            encryptCertTemp = (X509Certificate) cf.generateCertificate(in);
            PublicKey pubKey = encryptCertTemp.getPublicKey();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            fis = new FileInputStream(privateKeyPath);
            ks.load(fis, certPwd.toCharArray());
            Enumeration<String> aliasenum = ks.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias,
                    certPwd.toCharArray());
            X509Certificate cert = (X509Certificate) ks
                    .getCertificate(keyAlias);
            String certId = cert.getSerialNumber().toString();
            priKeys.put(PayConstants.UNION_PAY_B2B_WEB, privateKey);
            pubKeys.put(PayConstants.UNION_PAY_B2B_WEB,pubKey);
            certIds.put(PayConstants.UNION_PAY_B2B_WEB,certId);

        } catch (Exception e) {
            e.printStackTrace();
            throw new PayCenterException(CommonStateCode.FAILED,"银联证书初始化异常");
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    private static void c2bInit(){
        String privateKeyPath =  PayCenterConfig.getProperty("union_c2b_private_cert").toString();
        String publicKeyPath =  PayCenterConfig.getProperty("union_c2b_public_cert").toString();
        String certPwd = PayCenterConfig.getProperty("union_c2b_private_cert_pwd").toString();
        X509Certificate encryptCertTemp = null;
        CertificateFactory cf = null;
        FileInputStream in = null;
        FileInputStream fis = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            in = new FileInputStream(publicKeyPath);
            encryptCertTemp = (X509Certificate) cf.generateCertificate(in);
            PublicKey pubKey = encryptCertTemp.getPublicKey();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            fis = new FileInputStream(privateKeyPath);
            ks.load(fis, certPwd.toCharArray());
            Enumeration<String> aliasenum = ks.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias,
                    certPwd.toCharArray());
            X509Certificate cert = (X509Certificate) ks
                    .getCertificate(keyAlias);
            String certId = cert.getSerialNumber().toString();
            priKeys.put(PayConstants.UNION_PAY_WEB, privateKey);
            pubKeys.put(PayConstants.UNION_PAY_WEB,pubKey);
            certIds.put(PayConstants.UNION_PAY_WEB,certId);
        } catch (Exception e) {
            throw new PayCenterException(CommonStateCode.FAILED,"银联证书初始化异常");
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    private static void c2bAppInit(){
        String privateKeyPath =  PayCenterConfig.getProperty("union_app_c2b_private_cert").toString();
        String publicKeyPath =  PayCenterConfig.getProperty("union_app_c2b_public_cert").toString();
        String certPwd = PayCenterConfig.getProperty("union_app_c2b_private_cert_pwd").toString();
        X509Certificate encryptCertTemp = null;
        CertificateFactory cf = null;
        FileInputStream in = null;
        FileInputStream fis = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            in = new FileInputStream(publicKeyPath);
            encryptCertTemp = (X509Certificate) cf.generateCertificate(in);
            PublicKey pubKey = encryptCertTemp.getPublicKey();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            fis = new FileInputStream(privateKeyPath);
            ks.load(fis, certPwd.toCharArray());
            Enumeration<String> aliasenum = ks.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias,
                    certPwd.toCharArray());
            X509Certificate cert = (X509Certificate) ks
                    .getCertificate(keyAlias);
            String certId = cert.getSerialNumber().toString();
            priKeys.put(PayConstants.UNION_PAY_APP, privateKey);
            pubKeys.put(PayConstants.UNION_PAY_APP,pubKey);
            certIds.put(PayConstants.UNION_PAY_APP,certId);
        } catch (Exception e) {
            throw new PayCenterException(CommonStateCode.FAILED,"银联证书初始化异常");
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }


    public static Map sign(String payType,Map<String,String> data,String encoding){
        String localEncoding = "UTF-8";
        // 设置签名证书序列号
        data.put(SDKConstants.param_certId, certIds.get(payType));
        String stringData = coverMap2String(data);
        /**
         * 签名\base64编码
         */
        byte[] byteSign = null;
        String stringSign = null;
        try {
            // 通过SHA1进行摘要并转16进制
            byte[] signDigest = SecureUtil.sha1X16(stringData, localEncoding);
            byteSign = SecureUtil.base64Encode(signBySoft(
                    priKeys.get(payType), signDigest));
            stringSign = new String(byteSign);
            // 设置签名域值
            data.put(SDKConstants.param_signature, stringSign);
        }catch (Exception e){
            LOGGER.info(e.getMessage(),e);
            throw new PayCenterException(PayCenterStateCode.MARK_PARAM_ERROR,"参数签名异常");
        }
        return data;

    }

    /**
     * 验证签名(SHA-1摘要算法)
     *
     * @param resData
     *            返回报文数据
     * @param encoding
     *            编码格式
     * @return
     */
    public static boolean validate(Map<String, String> resData,String  payType, String encoding) {
        String localEncoding = "UTF-8";

        String stringSign = resData.get(SDKConstants.param_signature);
        // 从返回报文中获取certId ，然后去证书静态Map中查询对应验签证书对象
        String certId = resData.get(SDKConstants.param_certId);
        // 将Map信息转换成key1=value1&key2=value2的形式
        String stringData = coverMap2String(resData);
        try {
            // 验证签名需要用银联发给商户的公钥证书.
            return SecureUtil.validateSignBySoft(pubKeys.get(payType)
                    , SecureUtil.base64Decode(stringSign
                            .getBytes(localEncoding)), SecureUtil.sha1X16(stringData,
                            localEncoding));
        } catch (UnsupportedEncodingException e) {

        } catch (Exception e) {

        }
        return false;
    }


    /**
     * 除去数组中的空值和签名参数
     * @param contentData 签名参数组
     * @return 去掉空值
     */
    public static Map<String, String> paraFilter(Map<String, ?> contentData) {

        Map.Entry<String, String> obj = null;
        Map<String, String> submitFromData = new HashMap<String, String>();
        for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
            obj = (Map.Entry<String, String>) it.next();
            String value = obj.getValue();
            if (!StringUtil.isNullOrEmpty(value)) {
                // 对value值进行去除前后空处理
                submitFromData.put(obj.getKey(), value.trim());
            }
        }
        return submitFromData;
    }

    /**
     * 软签名
     *
     * @param privateKey
     *            私钥
     * @param data
     *            待签名数据
     * @return 结果
     * @throws Exception
     */
    public static byte[] signBySoft(PrivateKey privateKey, byte[] data)throws Exception {
        byte[] result = null;
        Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA1RSA);
        st.initSign(privateKey);
        st.update(data);
        result = st.sign();
        return result;
    }

    /**
     * 除去数组中的空值
     * @param sArray 签名参数组
     * @return 去掉空值
     */
    public static Map<String, String> paraFilterBack(Map<String, String[]> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.isEmpty()) {
            return result;
        }

        for ( Map.Entry< String, String[] > entry : sArray.entrySet() ) {
            String value = entry.getValue()[0];
            if (value == null || "".equals(value)) {
                continue;
            }
            result.put(entry.getKey(), value);
        }
        return result;
    }

    /**
     * 将Map中的数据转换成key1=value1&key2=value2的形式 不包含签名域signature
     *
     * @param data
     *            待拼接的Map数据
     * @return 拼接好后的字符串
     */
    public static String coverMap2String(Map<String, String> data) {
        SortedMap<String, String> tree = new TreeMap<String, String>();
        Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            if (SDKConstants.param_signature.equals(en.getKey().trim())) {
                continue;
            }
            tree.put(en.getKey(), en.getValue());
        }
        it = tree.entrySet().iterator();
        StringBuilder sf = new StringBuilder();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            sf.append(en.getKey() + SDKConstants.EQUAL + en.getValue()
                    + SDKConstants.AMPERSAND);
        }
        return sf.substring(0, sf.length() - 1);
    }

}
