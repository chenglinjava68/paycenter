package com.qccr.paycenter.facade.dal.ro;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据集对象
 * @author: denghuajun
 * @date: 2016/3/7 14:37
 * @version: v1.0.0
 */
public class PagedDataRO<T extends Serializable> implements Serializable{
    private static final long serialVersionUID = 392967477100743259L;

    /**
     * 分页大小
     */
    private int pageSize;

    /**
     * 页码
     */
    private int pageNo;

    /**
     * 结果总数
     */
    private int totalSize;

    private ArrayList<T> resultList;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        if(resultList instanceof ArrayList) {
            this.resultList = (ArrayList<T>) resultList;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
