package com.qccr.paycenter.biz.service.refund.workflow;

import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.enums.WhichPayEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 15:48 $
 */
@Service
public class RefundWorkflowHandler implements ApplicationContextAware {

    private Map<String,RefundWorkflow> workflows  = new HashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, RefundWorkflow> cores=  applicationContext.getBeansOfType(RefundWorkflow.class);
        Collection c = cores.values();
        Iterator<RefundWorkflow> it = c.iterator();
        RefundWorkflow core;
        while (it.hasNext())  {
            core = it.next();
            workflows.put(core.getCode(), core);
        }
    }

    public  RefundResultSO refund(RefundParamSO paramSO) throws Exception{
        String  workflowCode;
        if(StringUtil.isNullOrEmpty(paramSO.getWhichPay())){
            if(!ChannelEnum.OFFLINE.getName().equals(paramSO.getPayChannel())){
                workflowCode = WhichPayEnum.ONLINE_PAY.getWhichType();
            }else{
                workflowCode = WhichPayEnum.OFFLINE_PAY.getWhichType();
            }
        }else {
            workflowCode = paramSO.getWhichPay();
        }
        return workflows.get(workflowCode).refund(paramSO);
    }

}
