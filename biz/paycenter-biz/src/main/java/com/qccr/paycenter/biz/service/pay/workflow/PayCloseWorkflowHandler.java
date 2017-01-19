package com.qccr.paycenter.biz.service.pay.workflow;

import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.biz.service.pay.workflow.impl.NewPayCloseWorkflowImpl;
import com.qccr.paycenter.biz.service.pay.workflow.impl.OldPayCloseWorkflowImpl;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lim on 2016/7/26.
 */
@Service
public class PayCloseWorkflowHandler implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayCloseWorkflowHandler.class);

    private Map<String,PayCloseWorkflow> workflows  = new HashMap();

    @Resource
    private TradeOrderBO tradeOrderBO;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, PayCloseWorkflow> cores=  applicationContext.getBeansOfType(PayCloseWorkflow.class);
        Collection c = cores.values();
        Iterator it = c.iterator();
        PayCloseWorkflow core;
        while (it.hasNext())  {
            core = (PayCloseWorkflow) it.next();
            workflows.put(core.getCode(), core);
        }
    }

    public PayCloseResultSO close(final String businessNo, final String partner) throws Exception {
        String  workflowCode;
        final TradeOrderDO tradeOrderDO = tradeOrderBO.findByBusinessNo(businessNo);
        if(tradeOrderDO == null) {
            workflowCode = OldPayCloseWorkflowImpl.CODE;
        }else {
            workflowCode = NewPayCloseWorkflowImpl.CODE;
        }
        LOGGER.info("businessNo:::"+businessNo+";workflowCode:::"+workflowCode);
        return workflows.get(workflowCode).close(businessNo,partner );
    }

}
