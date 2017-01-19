package com.qccr.paycenter.biz.service.auth.imp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.qccr.paycenter.biz.bo.auth.FacepayAuthBO;
import com.qccr.paycenter.biz.service.auth.AuthNotifyService;
import com.qccr.paycenter.model.config.PayCenterConfig;
import com.qccr.paycenter.model.dal.so.notify.AuthNotifySO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.qccr.paycenter.biz.bo.base.EventPublisher;
import com.qccr.paycenter.biz.bo.pay.PayBO;
import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.pay.PayNotifyService;
import com.qccr.paycenter.biz.third.PayProcess;
import com.qccr.paycenter.biz.third.common.Response;
import com.qccr.paycenter.biz.third.wechat.util.WechatUtils;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.enums.PayNotifyEnum;
import com.qccr.paycenter.model.enums.PayOrderEnum;

@Service
@SuppressWarnings({"rawtypes","unused"})
public class AuthNotifyServiceImpl implements AuthNotifyService, ApplicationContextAware{

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthNotifyServiceImpl.class);

	private Map<String,PayProcess> processMap = new HashMap<String,PayProcess>();
	
	@Resource
	private ReentrantTransactionBO reentrantTransactionBO;
	
	@Resource
	private FacepayAuthBO facepayAuthBO;

	
	@Override
	public AuthNotifySO authNotify(String authChannel, String authType, HttpServletRequest request) {
		AuthNotifySO notifySO = null;
		try{
			//三方数据进行处理
			notifySO = processMap.get(authChannel).authNotify(authChannel,authType,request);
			notifySO.setAuthChannel(authChannel);
			notifySO.setAuthType(authType);
			LOGGER.info("userId:"+notifySO.getUserId()+",auth success，do authSuccess("+notifySO+")");
			authSuccess(notifySO);
		}catch(Exception e){
			if(notifySO == null) {
				notifySO = new AuthNotifySO();
			}
			//处理失败，需要三方重新推送
			LOGGER.error("auth process error,need auth again",e);
			/**
			 * 授权失败返回的页面
			 */
			StringBuilder errUrl = new StringBuilder();
			errUrl.append(PayCenterConfig.getProperty("face_pay_auth_fail_url").toString());
			try {
				if(e.getMessage() != null) {
//					errUrl.append("?errMsg=").append(URLEncoder.encode(URLEncoder.encode(e.getMessage(), "UTF-8"), "UTF-8"));
					errUrl.append("?errMsg=").append(URLEncoder.encode(e.getMessage(), "UTF-8"));
				}
			} catch (UnsupportedEncodingException e1) {
				LOGGER.info(e1.getMessage());
			}
			LOGGER.info(errUrl.toString());
			notifySO.setHasReturn(true);
			notifySO.setRedirect(true);
			notifySO.setRedirectUrl(errUrl.toString());
			notifySO.setReData("fail");
		}
		return  notifySO;		
	}

	/**
	 * 回调成功时的处理流程
	 * @param notifySO
	 */
	private void authSuccess( final AuthNotifySO notifySO){
		
		reentrantTransactionBO.execute(new TransactionCallback<Void>() {
			@Override
			public Void doTransaction() {
				facepayAuthBO.authSuccess(notifySO);
				return null;
			}
		});

	}

		

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, PayProcess> cores=  applicationContext.getBeansOfType(PayProcess.class);		
        Collection c = cores.values();
        Iterator it = c.iterator();  
        PayProcess core = null;       	
        while (it.hasNext())  {
        	core = (PayProcess) it.next();       	
        	processMap.put(core.getPayCode(), core);       	       
        }
		
	}
	
}
