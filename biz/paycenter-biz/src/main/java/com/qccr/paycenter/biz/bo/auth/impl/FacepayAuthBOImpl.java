package com.qccr.paycenter.biz.bo.auth.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.qccr.paycenter.biz.bo.auth.FacepayAuthBO;
import com.qccr.paycenter.common.utils.DateUtil;
import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.dal.dao.auth.FacepayAuthDao;
import com.qccr.paycenter.dal.domain.auth.FacepayAuthDO;
import com.qccr.paycenter.model.dal.so.notify.AuthNotifySO;
import com.qccr.paycenter.model.enums.FacepayAuthEnum;
import com.qccr.paycenter.model.exception.NotifyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@SuppressWarnings({"unchecked","rawtypes"})
public class FacepayAuthBOImpl implements FacepayAuthBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(FacepayAuthBOImpl.class);

	@Resource
	private FacepayAuthDao facepayAuthDao;


	/**
	 * 授权成功回调处理
	 */
	@Override
	public void authSuccess(final AuthNotifySO notifySO) {
		doAuthSuccess(notifySO);
	}

	/**
	 * 授权回来流程
	 * 先判断是否第一次授权
	 * 1.第一次授权：
	 * 2.非第一次授权：
	 * @param notifySO
     */
	private void  doAuthSuccess(final AuthNotifySO notifySO){
		FacepayAuthDO facepayAuthDO = facepayAuthDao.findBySuperUserId(Long.parseLong(notifySO.getUserId()));
		if(facepayAuthDO == null){//没有记录，说明被篡改了
			LOGGER.info("userId:"+notifySO.getUserId()+"被篡改了");
			return;
		}else {//
			if(FacepayAuthEnum.NORMAL.getValue() == facepayAuthDO.getStatus()) {
//				非第一次授权，需要把原来的过期掉
				LOGGER.info("userId:"+notifySO.getUserId()+"非第一次授权");
				updateStatusBySuperUserId(notifySO.getUserId());
				FacepayAuthDO entity = new FacepayAuthDO();
				entity.setAppRefreshToken(notifySO.getAppRefreshToken());
				entity.setAppAuthToken(notifySO.getAppAuthToken());
				entity.setAppAuthCode(notifySO.getAppAuthCode());
				entity.setAppId(notifySO.getAppId());
				entity.setChannel(notifySO.getAuthChannel());
				Date curDate = new Date();
				entity.setCodeCreateTime(curDate);
				entity.setCreateTime(curDate);
				entity.setExpiresIn(Long.parseLong(notifySO.getExpiresIn()));
				entity.setOutAuthAppId(notifySO.getAuthAppId());
				entity.setOutUserId(notifySO.getAuthUserId());
				entity.setReExpiresIn(Long.parseLong(notifySO.getReExpiresIn()));
				entity.setStatus(1);//正常状态
				if(notifySO.getUserId() != null) {
					entity.setSuperUserId(Long.parseLong(notifySO.getUserId()));
				}
				entity.setTokenCreateTime(curDate);
				entity.setUpdateTime(curDate);
				if(!StringUtil.isNullOrEmpty(notifySO.getTimestamp())) {
					try {
						entity.setNotifyurlCreateTime(DateUtil.parse(notifySO.getTimestamp()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				facepayAuthDao.insert(entity);
			}else if(FacepayAuthEnum.CREATED.getValue() == facepayAuthDO.getStatus()) {
				//第一次授权，更新记录
				Map authMap = new HashMap();
				authMap.put("appRefreshToken",notifySO.getAppRefreshToken());
				authMap.put("appAuthToken",notifySO.getAppAuthToken());
				authMap.put("appAuthCode",notifySO.getAppAuthCode());
				authMap.put("appId",notifySO.getAppId());
				authMap.put("channel",notifySO.getAuthChannel());
				Date curDate = new Date();
				authMap.put("codeCreateTime",curDate);
				authMap.put("expiresIn",notifySO.getExpiresIn());
				authMap.put("outAuthAppId",notifySO.getAuthAppId());
				authMap.put("outUserId",notifySO.getAuthUserId());
				authMap.put("reExpiresIn",notifySO.getReExpiresIn());
				authMap.put("status",1);//正常状态
				authMap.put("superUserId",notifySO.getUserId());
				authMap.put("tokenCreateTime",curDate);
				int row = facepayAuthDao.updateBySuperUserId(authMap);
				if(row<1){
					LOGGER.error("auth status change，need auth again");
					throw new NotifyException("auth status change，need auth again");
				}
			}
		}
		LOGGER.info("userId:"+notifySO.getUserId()+"授权记录保存成功");
	}

	/**
	 * userId查授权记录
	 *
	 * @param superUserId
	 * @return
	 */
	@Override
	public FacepayAuthDO findBySuperUserId(String superUserId) {
		return facepayAuthDao.findBySuperUserId(Long.parseLong(superUserId));
	}

	/**
	 * 更新状态为过期
	 *
	 * @param superUserId
	 * @return
	 */
	@Override
	public int updateStatusBySuperUserId(String superUserId) {
		return facepayAuthDao.updateStatusBySuperUserId(Long.parseLong(superUserId));
	}
}
