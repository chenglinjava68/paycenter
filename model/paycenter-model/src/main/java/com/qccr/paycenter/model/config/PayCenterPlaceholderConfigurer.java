package com.qccr.paycenter.model.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PayCenterPlaceholderConfigurer extends PropertyPlaceholderConfigurer{
	
	@Override
    @SuppressWarnings("rawtypes")
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
                                     Properties props) throws BeansException {

        @SuppressWarnings("unchecked")
        Map<String, Object> map = new HashMap<String, Object>((Map) props);
        PayCenterConfig.setProperties(map);
        super.processProperties(beanFactoryToProcess, props);
    }
}
