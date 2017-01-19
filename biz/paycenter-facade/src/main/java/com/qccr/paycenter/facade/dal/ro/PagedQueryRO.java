package com.qccr.paycenter.facade.dal.ro;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 分页查询
 * @author: denghuajun
 * @date: 2016/3/7 14:37
 * @version: v1.0.0
 */
public class PagedQueryRO implements Serializable{
    private static final long serialVersionUID = -3595519579880925766L;

    public static final int defaultPageSize = 20;
    public static final int defaultPageNo = 1;

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
