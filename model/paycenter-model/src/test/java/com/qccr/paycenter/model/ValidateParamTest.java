package com.qccr.paycenter.model;

import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.model.annotation.Validate;
import com.qccr.paycenter.model.annotation.Validated;
import com.qccr.paycenter.model.validator.ValidateParam;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.ValidatorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: denghuajun
 * date: 2016/4/12 20:24
 * version: v1.0.0
 */
public class ValidateParamTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateParamTest.class);
    @Validated(alias = "test")
    public void test(@Validate(name="name") String name, @Validate(name="age",minValue = "65") Integer age ){
        ValidateState state = new ValidateState();
        String key = this.getClass().getName()+"test";
        ValidatorHandler.volidate(key,state,name,age);
        LogUtil.info(LOGGER, "test",state);
    }
    public void test(){

    }
    @Validated(alias = "update")
    public void update(@Validate(name="name",isEmpty = false) String name){

    }

    public static void main(String args[]){
        Long start = System.currentTimeMillis();
        Map<String,List<ValidateParam>> map = new ConcurrentHashMap<String,List<ValidateParam>>();
        Method[] methods =ValidateParamTest.class.getDeclaredMethods();
        for(Method method:methods){
            Validated validated = method.getAnnotation(Validated.class);
            if(validated!=null){
                String key = ValidateParamTest.class.getName()+validated.alias();
                List<ValidateParam> list = new ArrayList<>();
                Annotation[][] annotations =  method.getParameterAnnotations();
                for(Annotation[] annotationArr: annotations){
                    Annotation annotation = annotationArr[0];
                    if(annotation instanceof  Validate){
                        Validate validate = (Validate) annotation;
                       // key+= validate.name();
                        if(validate.validate()){
                            ValidateParam param = new ValidateParam(validate,validate.name());

                            list.add(param);
                        }
                    }
                }
                map.put(key,list);
                LogUtil.info(LOGGER, "test",annotations);
            }
        }

        ValidateParamTest test = new ValidateParamTest();
        ValidatorHandler.pcaches = map;
        test.test("sss",11);
        LogUtil.info(LOGGER, "test",System.currentTimeMillis()-start);
        start = System.currentTimeMillis();
        test.test("sss",11);
        LogUtil.info(LOGGER, "test",System.currentTimeMillis()-start);

    }

}
