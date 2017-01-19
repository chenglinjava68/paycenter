package com.qccr.paycenter.model.dal.dbo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 使用marketcenter方式,直接copy
 * author: denghuajun
 * date: 2016/3/8 15:31
 * version: v1.0.0
 */
public class PagedQueryDO implements Serializable{

    public static final int defaultPageSize = 20;
    public static final int defaultPageNo = 1;
    private static final long serialVersionUID = 4242094973258985730L;

    private Integer pageSize = defaultPageSize;
    private Integer pageNo = defaultPageNo;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize == null || defaultPageSize <= Integer.valueOf(0) ? defaultPageSize : pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo == null || pageNo <= Integer.valueOf(0) ? defaultPageNo : pageNo;
    }

    public Integer getStartRow() {
        return (pageNo - 1) * pageSize;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
