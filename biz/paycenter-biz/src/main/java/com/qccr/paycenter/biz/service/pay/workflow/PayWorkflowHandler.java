package com.qccr.paycenter.biz.service.pay.workflow;

import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.enums.WhichPayEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 14:16 $
 */
@Service
public class PayWorkflowHandler implements ApplicationContextAware {

    private Map<String,PayWorkflow> workflows  = new HashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, PayWorkflow> cores=  applicationContext.getBeansOfType(PayWorkflow.class);
        Collection c = cores.values();
        Iterator it = c.iterator();
        PayWorkflow core;
        while (it.hasNext())  {
            core = (PayWorkflow) it.next();
            workflows.put(core.getCode(), core);
        }
    }

    public PayResultSO pay(PayParamSO propertiesSo) throws Exception {
        String  workflowCode;
        if(StringUtil.isNullOrEmpty(propertiesSo.getWhichPay())){
            if(!ChannelEnum.OFFLINE.getName().equals(propertiesSo.getPayChannel())){
                workflowCode = WhichPayEnum.ONLINE_PAY.getWhichType();
            }else{
                workflowCode = WhichPayEnum.OFFLINE_PAY.getWhichType();
            }
        }else {
            workflowCode = propertiesSo.getWhichPay();
        }
        /**
         * 临时处理一下
         * 交行等快捷支付是同步返回结果的，所以区别处理，之后有同步的也可以加在这里面
         */
        if(ChannelEnum.BOCOM.getName().equals(propertiesSo.getPayChannel())) {
            workflowCode = WhichPayEnum.ONLINE_SYNC_PAY.getWhichType();
        }
        /**
         * 支付宝当面付条码付同步返回
         */
        if(ChannelEnum.ALIPAY.getName().equals(propertiesSo.getPayChannel()) && PayConstants.ALI_PAY_FACEPAY.equals(propertiesSo.getPayType())) {
            workflowCode = WhichPayEnum.ONLINE_SYNC_PAY.getWhichType();
        }
        return workflows.get(workflowCode).pay(propertiesSo);
    }

}
