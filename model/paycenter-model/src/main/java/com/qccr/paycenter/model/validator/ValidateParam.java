package com.qccr.paycenter.model.validator;

import com.qccr.paycenter.model.annotation.Validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: denghuajun
 * date: 2016/4/12 20:57
 * version: v1.0.0
 */
public class ValidateParam {

    private String name;

    private Validate validate;

    private String minValue;

    private String maxValue;

    private String regexp;

    private Pattern pattern;

    private String regexpTemplate;

    public ValidateParam(Validate validate,String name){
        this.validate = validate;
        this.name = name;
        maxValue = validate.maxValue();
        minValue = validate.minValue();
        regexp = validate.regexp();
        if(regexp!=null && !"".equals(regexp)){
            pattern = Pattern.compile(regexp);
        }
        regexpTemplate = validate.refexpTemplate();
    }

    public void validate(ValidateState state,Object value){

        if(value==null|| "".equals(value.toString())){
            state.setErrorMsg(name+"为: "+value);
            state.setPass(false);
            return ;
        }

        if(maxValue!=null&&!"".equals(maxValue)&&(Double.parseDouble(value.toString()) >Double.valueOf(maxValue))){
            state.setErrorMsg(name+"大于最大值"+maxValue);
            state.setPass(false);
            return ;
        }

        if(minValue!=null&&!"".equals(minValue)&&(Double.parseDouble(value.toString()) <Double.valueOf(minValue))){
            state.setErrorMsg(name+"小于最小值"+minValue);
            state.setPass(false);
            return;
        }

        if(regexp!=null&&!"".equals(regexp)&& value instanceof String){
            Matcher matcher = pattern.matcher((String)value);
            if(!matcher.matches()){
                state.setErrorMsg(name+"无法匹配:"+regexp);
                state.setPass(false);
                return;
            }
        }
    }

    public Validate getValidate() {
        return validate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValidate(Validate validate) {
        this.validate = validate;
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
