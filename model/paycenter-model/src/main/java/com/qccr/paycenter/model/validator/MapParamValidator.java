package com.qccr.paycenter.model.validator;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by denghuajun on 2016/1/20.
 */
public class MapParamValidator extends AbstractValidator<Map>{
    @Override
    public ValidateState volidate(ValidateState state, Map param) {
        Set<Map.Entry> set =   param.entrySet();
        for (Map.Entry entry : set) {
            notNull(state, entry.getValue(),entry.getKey()+"is null");
        }
        return state;
    }

    public void doVolidate(ValidateState state,Map.Entry entry, Class type){

        if(String.class.isAssignableFrom(type)) {
            notEmpty(state, (String) entry.getValue(),entry.getKey()+"is null");
        }
        else if(Integer.class.isAssignableFrom(type)) {
            notNull(state, entry.getValue(),entry.getKey()+"is null");
        }
        else if(Long.class.isAssignableFrom(type)) {
            //do sth
        }
        else if(Date.class.isAssignableFrom(type)) {
            //do sth
        }
        else if(Boolean.class.isAssignableFrom(type)) {
            //do sth
        }
        else if(Float.class.isAssignableFrom(type)) {
            //do sth
        }
        else if(Double.class.isAssignableFrom(type)) {
            //do sth
        }
    }
}
