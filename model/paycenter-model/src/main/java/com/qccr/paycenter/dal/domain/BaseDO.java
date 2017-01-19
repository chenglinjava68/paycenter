package com.qccr.paycenter.dal.domain;

import java.io.Serializable;
import java.util.Date;
/**
 * @author denghuajun
 * date:2015年11月16日 下午2:05:44
 */
public abstract class BaseDO implements Serializable{

	private static final long serialVersionUID = -6166068813912247420L;
	
	private Date createTime;
	
	private Date  updateTime;
	
	private String createPerson;
	
	private String  updatePerson;

	public Date getCreateTime() {
		if(createTime != null) {
			return (Date) createTime.clone();
		}else {
			return null;
		}
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		if(updateTime != null) {
			return (Date) updateTime.clone();
		}else {
			return null;
		}
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getUpdatePerson() {
		return updatePerson;
	}

	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}

	
	

}
