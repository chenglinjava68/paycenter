package com.qccr.paycenter.biz.bo.refund.impl;

import com.qccr.paycenter.biz.bo.refund.RefundNotifyBO;
import com.qccr.paycenter.biz.third.PayProcess;
import com.qccr.paycenter.biz.third.common.Response;
import com.qccr.paycenter.biz.third.wechat.util.WechatUtils;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * author: denghuajun
 * date: 2016/3/11 17:22
 * version: v1.0.0
 */
@Component
public class RefundNotifyBOImpl implements RefundNotifyBO,ApplicationContextAware {

    private Map<String,PayProcess> processMap = new HashMap<String,PayProcess>();

    @Override
    public RefundNotifySO refundNotify(String refundChannel, String refundType, HttpServletRequest request,String partner){
        RefundNotifySO notifySO = null;
        notifySO = processMap.get(refundChannel).refundNotify(refundChannel,refundType,request,partner);
        notifySO.setRefundChannel(refundChannel);
        return notifySO;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

        Map<String, PayProcess> cores=  applicationContext.getBeansOfType(PayProcess.class);
        Collection c = cores.values();
        Iterator it = c.iterator();
        PayProcess core = null;
        while ( it.hasNext())  {
            core = (PayProcess) it.next();
            processMap.put(core.getPayCode(), core);
        }
    }

    /**
     * 退款回调回调发生异常时，包装返回三方内容
     * @return
     */
    @Override
    public RefundNotifySO  refundNotifyExp(String  refundChannel){
        RefundNotifySO notifySO = new RefundNotifySO();
        if(refundChannel.equals(PayCenterConstants.ALIPAY_CALLBACK_CHANNEL)){
            notifySO.setHasReturn(true);
            notifySO.setReData("fail");
        }else if(refundChannel.equals(PayCenterConstants.WECHAT_CALLBACK_CHANNEL)){
            notifySO.setHasReturn(true);
            Response response = new Response();
            response.setErrorCode("FAIL");
            response.setErrorMsg("验证签名失败");
            notifySO.setReData(WechatUtils.responseToXML(response));
        }else if(refundChannel.equals(PayCenterConstants.UNION_CALLBACK_CHANNEL)){
            notifySO.setHasReturn(true);
            notifySO.setReData("fail");
        }
        return notifySO;
    }
}
