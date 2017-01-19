package com.qccr.paycenter.biz.third;

import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/8/18 15:29 $
 */
@Component
public class ProcessManager implements ApplicationContextAware {

    private Map<String ,ProcessAdapter> processors = new HashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,ProcessAdapter> cores = applicationContext.getBeansOfType(ProcessAdapter.class);
        Collection c = cores.values();
        Iterator<ProcessAdapter> it = c.iterator();
        ProcessAdapter core;
        while (it.hasNext())  {
            core = it.next();
            processors.put(core.getPayCode(), core);
        }
    }

    public ProcessAdapter getProcess(String code){
        return processors.get(code);
    }

}
