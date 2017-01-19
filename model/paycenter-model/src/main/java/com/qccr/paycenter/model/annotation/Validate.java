package com.qccr.paycenter.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: denghuajun
 * date: 2016/3/7 15:49
 * version: v1.0.0
 */
@Inherited
@Target({ElementType.PARAMETER,ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {

    boolean validate() default true;

    String name() default "";
    /**
     * 允许为空
     * @return
     */
    boolean isEmpty() default false;

    /**
     * 正则匹配
     * @return
     */
    String regexp() default "";

    /**
     * 正则模板,ip,phone,tele,email;
     * @return
     */
    String refexpTemplate() default "";

    /**
     * 数值类型，数值区间最小
     * @return
     */
    String minValue() default "";

    /**
     * 数值类型，数值区间最小
     * @return
     */
    String maxValue () default "";

    /**
     * 添加默认值
     * @return
     */
    String defaultValue() default "";
}
