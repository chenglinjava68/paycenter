package com.qccr.paycenter.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: denghuajun
 * date: 2016/3/7 15:08
 * version: v1.0.0
 */
@Inherited
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validated {
    boolean validate() default true;

    String alias() default "";
}
