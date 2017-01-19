package com.qccr.paycenter.model.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author: denghuajun
 * date: 2016/3/9 11:34
 * version: v1.0.0
 */
public class SpringConvert {
    private SpringConvert() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConvert.class);

    /**
     * 使用spring beanutils  进行对象转化
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    public static<T> T convert(Object source,Class<T> clazz){
        if(source==null){
            return null;
        }
        T target = null;
        /*可容错*/
        try{
            target = BeanUtils.instantiate(clazz);
            BeanUtils.copyProperties(source,target);
        }catch (RuntimeException e){
            LOGGER.error(source.getClass().getName()+"can not convert to "+clazz.getName(), e);
        }
        return target;
    }

    public static <T> List<T> convertList(List<?> source, Class<T> clazz) {
        if (source == null) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<T>();
        T obj = null;
        for (Object element : source) {
            obj =  convert(element, clazz);
            if(obj!=null){
                list.add(obj);
            }
        }
        return list;
    }

}
