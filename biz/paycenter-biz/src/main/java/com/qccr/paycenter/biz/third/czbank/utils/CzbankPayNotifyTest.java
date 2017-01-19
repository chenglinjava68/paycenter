package com.qccr.paycenter.biz.third.czbank.utils;

import com.qccr.paycenter.biz.third.alipay.utils.RSA;
import com.qccr.paycenter.common.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
/**
 *  调试浙商回调
 * Created by lim on 2016/4/13.
 */
public class CzbankPayNotifyTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CzbankPayNotifyTest.class);
    public static void main(String[] args) {

        /**
         * http://127.0.0.1:8080/paycenter/pay/notify/czbank/czbank_wap/partner?trade_status=TRADE_SUCCESS&out_trade_no=a0001&total_fee=1230.00&trade_no=www222&buyer_id=1221&sign=BQ0YKVcmeR0An7EHZEryhBv2X8dwdgTYw0IpUCUx1nwQreKWTYGaDna/mWMht8au5etaumeYZzw8g1jRbXsFZwj8bGxpy6SKS6w2cKxCHr1dVryla2xvUvKBL/3cR7wKQlNaZcm/T6a85bRzoqADSbIzePolTTbo3zv5NhiXXXo=
         *
         *
         * sign
         out_trade_no  汽车超人订单号
         trade_status     交易状态
         total_fee          交易金额

         * notify_time   通知时间   格式为yyyy-MM-dd HH:mm:ss
            汽车超人订单号
         trade_no         浙商交易流水号
               交易状态
         buyer_id           支付账号
                    交易金额
         gmt_payment   支付时间
         */
        Map<String,String> param = new HashMap<>();
        String out_trade_no = "a0001"; //汽车超人订单号
        String trade_status = "TRADE_SUCCESS";     //交易状态
        String total_fee = "1230.00";          //交易金额
        param.put("out_trade_no", out_trade_no);
        param.put("trade_status", trade_status);
        param.put("total_fee", total_fee);
        StringBuilder signContent = new StringBuilder(out_trade_no).append(trade_status).append(total_fee);
        LogUtil.info(LOGGER, signContent.toString());
        String appCertFilePath = "d:/data/html/paycenter/certs/netpay.pfx";
        String appCertPwd = "a121212";
        String sign_ = RSA.sign(signContent.toString(), appCertFilePath, appCertPwd, "utf-8");
        LogUtil.info(LOGGER, "sign:::"+sign_);
        sign_ = "bggAsN31hGr0Qb7Flpmv+wtOCCA7amC+kUqH+EEvab/ctTPzmNC1vI15kxZTRG+nmVQK1TmX6EAmhwNsnZVzj8At1Ifs00asuB2Pw9NjP0Gj+0djmCtvJtGGvNJ/r1ziEcimnTgMvydSNiGtl5U4kHEpROcSUqwOqg4AjyTLUSs=";
        try {
            param.put("sign", URLEncoder.encode(sign_,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(),e);
        }
        param.put("trade_no","abc123123");
        param.put("buyer_id","bbb333");
        param.put("gmt_payment","2016-04-13 15:12:12");
        param.put("notify_time","2016-04-13 15:12:12");
        param.put("mch_id","123321");
        String testUrl = "http://test.qichechaoren.com/kaifapaycenter/pay/notify/czbank/czbank_wap/partner";
        String result = sendPost(testUrl, createLinkString(param));
        System.out.printf("result+++>>>"+result);
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = new StringBuffer(prestr).append(key).append("=").append(value).toString();
            } else {
                prestr = new StringBuffer(prestr).append(key).append("=").append(value).append("&").toString();
            }
        }

        return prestr;
    }
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (MalformedURLException e) {
            LogUtil.info(LOGGER, "发送 POST 请求出现异常！"+e);
        } catch (IOException e) {
            LogUtil.info(LOGGER, "发送 POST 请求出现异常！"+e);
        } finally{//使用finally块来关闭输出流、输入流
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                LOGGER.error(ex.getMessage(),ex);
            }
        }
        return result.toString();
    }
}
