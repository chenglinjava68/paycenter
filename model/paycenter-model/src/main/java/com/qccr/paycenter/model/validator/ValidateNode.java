package com.qccr.paycenter.model.validator;

import com.qccr.paycenter.model.annotation.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: denghuajun
 * date: 2016/3/7 17:05
 * version: v1.0.0
 */
public class ValidateNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateNode.class);
    private Field field;

    private Validate validate;

    private String defaultValue;

    private String minValue;

    private String maxValue;

    private String regexp;

    private Pattern pattern;

    private String regexpTemplate;

    public ValidateNode(Validate validate, Field field){
        this.field =field;
        this.validate = validate;
        defaultValue = validate.defaultValue();
        maxValue = validate.maxValue();
        minValue = validate.minValue();
        regexp = validate.regexp();
        if(regexp!=null && !"".equals(regexp)){
            pattern = Pattern.compile(regexp);

        }
        regexpTemplate = validate.refexpTemplate();

    }

    public void validate(ValidateState state,Object value,Object object){

        if(defaultValue!=null && !"".equals(defaultValue)){
            try {
                FieldUtils.writeDeclaredField(object,field.getName(),defaultValue,true);
                value = defaultValue;
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage(),e);
            }
        }

        if(value==null|| "".equals(value.toString())){
            state.setErrorMsg(field.getName()+"为:"+value);
            state.setPass(false);
            return ;
        }

        if(maxValue!=null&&!"".equals(maxValue)&&(Double.parseDouble(value.toString()) >Double.valueOf(maxValue))){
            state.setErrorMsg(field.getName()+"大于最大值"+maxValue);
            state.setPass(false);
            return ;
        }

        if(minValue!=null&&!"".equals(minValue)&&(Double.parseDouble(value.toString()) <Double.valueOf(minValue))){
            state.setErrorMsg(field.getName()+"小于最小值"+minValue);
            state.setPass(false);
            return;
        }

        if(regexp!=null&&!"".equals(regexp)&& value instanceof String){
            Matcher matcher = pattern.matcher((String)value);
            if(!matcher.matches()){
                state.setErrorMsg(field.getName()+"无法匹配:"+regexp);
                state.setPass(false);
                return;
            }
        }

    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Validate getValidate() {
        return validate;
    }

    public void setValidate(Validate validate) {
        this.validate = validate;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getRegexpTemplate() {
        return regexpTemplate;
    }

    public void setRegexpTemplate(String regexpTemplate) {
        this.regexpTemplate = regexpTemplate;
    }
}
