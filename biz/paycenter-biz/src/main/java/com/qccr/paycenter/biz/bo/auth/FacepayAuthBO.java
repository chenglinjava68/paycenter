package com.qccr.paycenter.biz.bo.auth;


import com.qccr.paycenter.dal.domain.auth.FacepayAuthDO;
import com.qccr.paycenter.model.dal.so.notify.AuthNotifySO;

public interface FacepayAuthBO {
	
	void authSuccess(AuthNotifySO notifySO);
	
	/**
	 * userId查授权记录
	 *
	 * @param superUserId
	 * @return
	 */
	FacepayAuthDO findBySuperUserId(String superUserId);

	/**
	 * 更新状态为过期
	 * @param superUserId
	 * @return
	 */
	int updateStatusBySuperUserId(String superUserId);
}
