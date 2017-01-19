package com.qccr.paycenter.model.validator;

import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.model.annotation.Validate;
import com.qccr.paycenter.model.annotation.Validated;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 只针对基础对象进行进行验证，不支持循环嵌套
 * author: denghuajun
 * date: 2016/3/5 16:43
 * version: v1.0.0
 */
public class ValidatorHandler {
    public ValidatorHandler() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorHandler.class);
    /**
     * 使用缓存，后续检测时间消耗几乎为0
     */
    public static final ConcurrentMap<String,List<ValidateNode>> caches = new ConcurrentHashMap<String,List<ValidateNode>>();

    public static Map<String,List<ValidateParam>> pcaches = new ConcurrentHashMap();

    /*facade层调用，验证多个常规数据参数，int，string，long等*/
    public static  ValidateState volidate(String key ,Object ...objs){
        ValidateState state = new ValidateState();
        List<ValidateParam> list = pcaches.get(key);
        executeValidateParams(list,state,objs);
        return state;
    }

    /*facade层调用，用于验证RO数据对象等*/
    public static ValidateState volidate(ValidateState state, Object param) {
        Class clazz  =  param.getClass();
        /*针对非基础类型对象数组*/
        if(isArray(clazz)){
            //暂时不处理
        }
        /*针对非基础类型对象List*/
        if(isList(param)){
            //暂时不处理
        }
        /*针对非基础类型对象Set*/
        if(isSet(param)){
            //暂时不处理
        }
        /*针对非基础类型对象Map*/
        if(isMap(param)){
            //暂时不处理
        }
        Validated validated =  param.getClass().getAnnotation(Validated.class);
        if(validated==null || validated.validate()){
            return doValidate(state, param);
        }
        return state;
    }

    /**
     * 貌似进入了误区，我只要负责第一层数据对象识别。对象嵌套对象只要拿到最外层进行验证
     * @param state
     * @param object
     * @return
     */
    private static ValidateState doValidate(ValidateState state, Object object){
        String cacheName = object.getClass().getName();
        List<ValidateNode> list = caches.get(cacheName);
        if(list==null){
            list = fillValidateNodes(object);
            caches.put(cacheName,list);
        }
        executeValidateNodes(list,state,object);
        return null;
    }

    private static List<ValidateNode> fillValidateNodes(Object object){
        List<ValidateNode> list = new ArrayList<ValidateNode>();
        Field[] fields =  object.getClass().getDeclaredFields();
        for(int index=0;index<fields.length;index++){
            Field field = fields[index];
            Validate validate = fields[index].getAnnotation(Validate.class);
            if(validate !=null){
                ValidateNode node = new ValidateNode(validate,field);
                list.add(node);
            }
        }
        return list;

    }

    public static void executeValidateNodes(List<ValidateNode> list,ValidateState state,Object object){
        for(int index=0;index<list.size();index++){
            ValidateNode node = list.get(index);
            try {
                if(!state.isPass()){
                    break;
                }
                node.validate(state,FieldUtils.readField(object,node.getField().getName(),true),object);
            } catch (IllegalAccessException e) {
                LogUtil.info(LOGGER, "validate error,node:"+node,e);
            }
        }
    }

    public static void executeValidateParams(List<ValidateParam> list,ValidateState state,Object... objs){
        for(int index=0;index<list.size();index++){
            ValidateParam  node = list.get(index);
            try {
                if(!state.isPass()){
                    break;
                }
                node.validate(state,objs[index]);
            } catch (Exception e) {
                LogUtil.info(LOGGER, "validate error,node:"+node,e);
            }
        }
    }


    private static  boolean isBaseDataType(Class clazz){
        return
            (
                clazz.equals(String.class) ||
                        clazz.equals(Integer.class)||
                        clazz.equals(Byte.class) ||
                        clazz.equals(Long.class) ||
                        clazz.equals(Double.class) ||
                        clazz.equals(Float.class) ||
                        clazz.equals(Character.class) ||
                        clazz.equals(Short.class) ||
                        clazz.equals(BigDecimal.class) ||
                        clazz.equals(BigInteger.class) ||
                        clazz.equals(Boolean.class) ||
                        clazz.equals(Date.class) ||
                        clazz.equals(DateTime.class) ||
                        clazz.isPrimitive()
            );
    }

    public static boolean isMap(Object obj){
        return (obj instanceof java.util.Map<?,?>);
    }

    public static boolean isList(Object obj){
        return (obj instanceof java.util.List<?>);
    }

    public static boolean isSet(Object obj){
        return (obj instanceof java.util.Set<?>);
    }

    public static boolean isArray(Class clazz){
        return clazz.isArray();
    }

}
