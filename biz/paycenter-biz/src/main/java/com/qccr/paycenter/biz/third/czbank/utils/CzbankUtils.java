package com.qccr.paycenter.biz.third.czbank.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.qccr.paycenter.biz.third.alipay.utils.AlipayCore;
import com.qccr.paycenter.biz.third.alipay.utils.RSA;
import com.qccr.paycenter.biz.third.common.PayUtil;
import com.qccr.paycenter.biz.third.common.XMLProUtil;
import com.qccr.paycenter.biz.third.czbank.core.CzbankConfig;
import com.qccr.paycenter.biz.third.czbank.core.CzbankPayProcessImp;
import com.qccr.paycenter.biz.third.czbank.model.SingleQueryHead;
import com.qccr.paycenter.biz.third.czbank.model.SingleQueryRequest;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.DateUtil;
import com.qccr.paycenter.common.utils.JsonUtil;
import com.qccr.paycenter.common.utils.socket.Connection;
import com.qccr.paycenter.common.utils.socket.ConnectionManager;
import com.qccr.paycenter.model.config.PayCenterConfig;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayQueryResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.exception.NotifyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by limin on 2016/4/11.
 */
public class CzbankUtils {
    private CzbankUtils() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CzbankUtils.class);
    // 字符编码格式 目前支持 gbk 或 utf-8
    private static final String input_charset = "utf-8";

    private static final String  APP_CERT = "czbank_wap_cert";
    private static final String  APP_CERT_PUBLIC = "czbank_wap_cert_online_public";
    private static final String APP_CERT_PWD =  "czbank_wap_cert_pwd";
    private static final String CZBANK_GATEWAY =  "czbank_gateway";//支付网关
    private static final String CZBANK_TRADE_ORDER_QUERY_IP =  "czbank_trade_order_query_ip";//交易状态查询IP
    private static final String CZBANK_TRADE_ORDER_QUERY_IP_PARAM =  new StringBuilder("10.0.").append("68.124").toString();
    private static final String CZBANK_TRADE_ORDER_QUERY_PORT =  "czbank_trade_order_query_port";//交易状态查询端口
    public static String wapPay(final PayParamSO param) throws UnsupportedEncodingException {
        String retain = param.getRetain();
        LogUtil.info(LOGGER, retain);
        if(StringUtils.isBlank(retain)) {
            return null;
        }
        Map<String, String> retainMap = JsonUtil.parseObject(retain, Map.class);
        Map<String, String> sParaTemp = new HashMap<String, String>();
        /**
         * 加密方式：SHA1WithRSA，加密内容：客户ID、订单金额、订单编号
         测试环境的证书见附件，私钥密码：a121212
         */
        sParaTemp.put("custId", retainMap.get("custId"));//客户ID，跟其他字段一样从备注字段里面获取
        sParaTemp.put("serviceType", retainMap.get("serviceType"));////服务类型	serviceType	1 五座洗车；2 七座洗车
        sParaTemp.put("orderAmount", PayUtil.changeF2Y(param.getAmount()));//订单金额
        sParaTemp.put("orderNo", param.getPayNo());//订单编号
        Map<String, String> sPara =  AlipayCore.paramFilter(sParaTemp);
        String appCertFilePath = PayCenterConfig.getSSLCertPath(APP_CERT,"d:/data/html/paycenter/certs/czbank_private.pfx");
        String appCertPwd = PayCenterConfig.getSSLCertPath(APP_CERT_PWD,"9239547");
//        客户ID、订单金额、订单编号这三个值按顺序拼接
        StringBuilder signContent = new StringBuilder(sParaTemp.get("custId")).append(sParaTemp.get("orderAmount")).append(sParaTemp.get("orderNo"));
        LogUtil.info(LOGGER, signContent.toString());
        String sign_ = RSA.sign(signContent.toString(), appCertFilePath, appCertPwd, input_charset);
        LogUtil.info(LOGGER, "sign:::"+sign_);
        sPara.put("sign", URLEncoder.encode(sign_, "utf-8"));
        sPara.put("notifyUrl", param.getLocalNotifyUrl());////后台通知地址
        sPara.put("requestFrontUrl", PayCenterConfig.getSSLCertPath(CZBANK_GATEWAY, "https://e.czbank.com/weixinHTML/carWashPayment.do"));//wap端请求地址
        sPara.put("corpChannel", CzbankConfig.CORP_CHANNEL);//渠道号
        sPara.put("corpBizNo",param.getBusinessNo());
        LogUtil.info(LOGGER, sPara.get("requestFrontUrl")+"?"+AlipayCore.createLinkString(sPara));
//        return AlipayCore.createLinkString(sPara);//本地测试
        return JsonUtil.toJSONString(sPara);//生产上面
    }

    /**
     * 20160413 lim
     * 验证支付回调的签名
     * @param params
     * @return
     */
    public static boolean verify(Map<String, String[]> params) {
        /**
         * sign
         out_trade_no  汽车超人订单号
         trade_status     交易状态
         total_fee          交易金额
         */
        // 交易状态
        String tradeStatus = params.get("trade_status")[0];
        // 商户订单号/汽车超人订单号
        final String outTradeNo = params.get("out_trade_no")[0];
        //交易金额
        final String totalFee = params.get("total_fee")[0];
        //签名字段
        String sign = params.get("sign")[0];
        StringBuilder sb = new StringBuilder();
        sb.append(outTradeNo).append(tradeStatus).append(totalFee);
        String appCertFilePath = PayCenterConfig.getSSLCertPath(APP_CERT_PUBLIC,"d:/data/html/paycenter/certs/czbank_online_public.cer");
        LogUtil.info(LOGGER, ">>>>>>>>>>>>>>>>>>"+appCertFilePath);
        boolean isSign = RSA.verifyByPath(sb.toString(), sign, appCertFilePath, input_charset);
        LogUtil.info(LOGGER, "isSign = "+isSign);
        return isSign;
    }

    /**
     * 浙商交易状态查询，测试过，跟对方开发联调过
     * 开发：101.68.90.120
     * 测试：60.191.15.87
     * 生产：10.0.68.124
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String payQuery(PayQuerySO payQuerySO) throws IOException, JAXBException {
        String queryIp = PayCenterConfig.getSSLCertPath(CZBANK_TRADE_ORDER_QUERY_IP, CZBANK_TRADE_ORDER_QUERY_IP_PARAM);
        String queryPort = PayCenterConfig.getSSLCertPath(CZBANK_TRADE_ORDER_QUERY_PORT,"47511");
        LogUtil.info(LOGGER, CZBANK_TRADE_ORDER_QUERY_IP + ":" + queryIp);
        ConnectionManager manager = new  ConnectionManager(queryIp, Integer.parseInt(queryPort));
        Connection connection = null;
        try {
            connection = manager.getConnection();
        } catch (Exception e) {
            LOGGER.error("浙商状态查询连接服务出错"+ queryIp,e);
        }
        SingleQueryRequest request = new SingleQueryRequest();
        //String corpNo, String cusId, String transCode, String msgId, String reqId, String transDate, String transTime
        String transDate = DateUtil.formatDate(payQuerySO.getPayStartTime(),"yyyyMMdd");
        String transTime = DateUtil.formatDate(payQuerySO.getPayStartTime(),"HHmmss");
        String msgId = getUUID();
        String reqId = getUUID();
        SingleQueryHead head = new SingleQueryHead(CzbankConfig.CORP_NO, CzbankConfig.CUS_ID,CzbankConfig.TRANS_CODE,msgId,reqId,transDate,transTime);
        request.setHead(head);
        HashMap<String, String> body = new HashMap<>();
        body.put("OrderNo",payQuerySO.getPayNo());
        request.setBody(body);
        String requestXml = XMLProUtil.convertToXml(request,"GBK");
        LogUtil.info(LOGGER, "client:"+requestXml);
        if(connection != null) {
            String result = connection.writeAndRead(requestXml, "GBK");
            LogUtil.info(LOGGER, "server:" + result);
            return result;
        }else {
            return "";
        }
    }

    /**
     * 支付通知消息处理
     *  * 回调需要的参数：
     notify_time   通知时间   格式为yyyy-MM-dd HH:mm:ss
     out_trade_no  汽车超人订单号
     trade_no         浙商交易流水号
     trade_status     交易状态
     buyer_id           支付账号
     total_fee          交易金额
     gmt_payment   支付时间
     sign加密下面四个字段，加密方式跟验签是一样的，所以用相同的方式来解密
     out_trade_no  汽车超人订单号
     trade_status     交易状态
     total_fee          交易金额
     * @param request
     * @return
     */
    public static PayNotifySO payNotify(HttpServletRequest request) {
        PayNotifySO result = new PayNotifySO();
        Map<String, String[]> map = request.getParameterMap();
        // 交易状态
        String tradeStatus = map.get("trade_status")[0];
        // 商户订单号/汽车超人订单号
        final String outTradeNo = map.get("out_trade_no")[0];
        //交易金额
        final String totalFee = map.get("total_fee")[0];
        // 计算得出通知验证结果
        boolean verify_result = CzbankUtils.verify(map);
        result.setHasReturn(true);
        result.setVerify(verify_result);
        if (!verify_result) {
            LOGGER.error("浙商,验证签名错误");
            throw new NotifyException("验证签名失败");
        }
        if ("success".equals(tradeStatus)) {
            result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
            result.setReData("success");
            String successTime =  map.get("gmt_payment")[0];
			/*添加支付完成时间*/
            if(successTime!=null && !"".equals(successTime)){
                try {
                    result.setSuccessTime(DateUtil.parseToSecond(successTime));
                } catch (ParseException e) {
                    result.setSuccessTime(new Date());
                }
            }else {
                result.setSuccessTime(new Date());
            }
        } else if ("WAIT_BUYER_PAY".equals(tradeStatus)) {
            result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_CREATED);
            result.setReData("success");
        } else {
            result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_NONE);
            result.setReData("success");
        }
//        result.setBillNo(map.get("trade_no")[0]);//浙商不传他们的流水号过来
        result.setBillNo(map.get("out_trade_no")[0]);//暂时用支付中心的单号
        result.setPayNo(outTradeNo);
        result.setMchId(map.get("mch_id")[0]);
        result.setPayerAccount(map.get("buyer_id")[0]);
        result.setAmount(PayUtil.changeY2F(totalFee));
        String receiptAmount = PayUtil.changeY2F(totalFee);
        result.setReceiptAmount(receiptAmount);
        String notifyTime = map.get("notify_time")[0];
        if(notifyTime!=null && !"".equals(notifyTime)) {
            try {
                result.setFinish(DateUtil.parse(notifyTime, "yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                LOGGER.error("时间格式转换出错",e);
            }
        }
        return result;
    }

    /**
     * 获得一个UUID
     * @return String UUID
     */
    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }

    public static void main(String[] args) {
        try {
            PayQuerySO payQuerySO = new PayQuerySO();
//            payQuerySO.setPayStartTime(DateUtil.parse("2016-05-19","yyyy-MM-dd"));
//            payQuerySO.setPayNo("2016052017584379697303");
//            payQuerySO.setPayStartTime(DateUtil.parse("2016-05-23","yyyy-MM-dd"));
//            payQuerySO.setPayNo("2016052311454716862723");


//            payQuery(payQuerySO);

//            PayQuerySO payQuerySO = new PayQuerySO();
            payQuerySO.setPayStartTime(DateUtil.parse("2016-05-24","yyyy-MM-dd"));
            payQuerySO.setQueryTime(DateUtil.parse("2016-05-24","yyyy-MM-dd"));
            payQuerySO.setPayNo("2016052414423329924677");
//            payQuery(payQuerySO);
            CzbankPayProcessImp cp = new CzbankPayProcessImp();
            PayQueryResultSO so = cp.queryPayOrder(payQuerySO);
            LogUtil.info(LOGGER, so.toString());
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
}
