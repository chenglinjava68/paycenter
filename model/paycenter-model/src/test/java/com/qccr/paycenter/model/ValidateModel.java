package com.qccr.paycenter.model;

import com.qccr.paycenter.model.annotation.Validate;
import com.qccr.paycenter.model.annotation.Validated;

/**
 * author: denghuajun
 * date: 2016/3/8 10:31
 * version: v1.0.0
 */
@Validated
public class ValidateModel {

    @Validate(defaultValue = "123456")
    private String name;
    @Validate(minValue = "1",maxValue = "200")
    private Integer age;

    private String addr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
