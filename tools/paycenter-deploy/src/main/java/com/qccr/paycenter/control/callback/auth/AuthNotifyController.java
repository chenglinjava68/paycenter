package com.qccr.paycenter.control.callback.auth;

import com.qccr.paycenter.biz.service.auth.AuthNotifyService;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.model.config.PayCenterConfig;
import com.qccr.paycenter.model.dal.so.notify.AuthNotifySO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;


@Controller
@RequestMapping(value = "/auth/notify")
public class AuthNotifyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthNotifyController.class);

	@Resource
	private AuthNotifyService authNotifyService;

	/**
	 * http://test.qichechaoren.com/liminpaycenter/auth/notify/alipay/facepay
	 * @param authChannel
	 * @param authType
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/{authChannel}/{authType}")
	public void authNotify(@PathVariable(value="authChannel") String authChannel,
						   @PathVariable(value="authType") String authType,HttpServletRequest request,HttpServletResponse response) {
		try{
			LOGGER.info("auth notify authChannel:"+authChannel+",authType:"+authType);
			Enumeration rnames = request.getParameterNames();
			for (Enumeration e = rnames ; e.hasMoreElements() ;) {
				String thisName = e.nextElement().toString();
				String thisValue = request.getParameter(thisName);
				LOGGER.info(thisName+"=="+thisValue);
			}
			AuthNotifySO result =  authNotifyService.authNotify(authChannel, authType, request);
			if(result.isHasReturn()){
				if(result.isRedirect()) {
					response.sendRedirect(result.getRedirectUrl());
//					if(!"fail".equals(result.getReData())) {
//						response.sendRedirect("http://bing.com");
//					}else {
//						response.sendRedirect("http://www.qccr.com");
//					}
				}else {
					OutputStream out = null;
					try {
						out = response.getOutputStream();
						out.write(result.getReData().getBytes("utf-8"));
						LogUtil.info(LOGGER, "auth notify success, return content:" + result.getReData());
					} catch (UnsupportedEncodingException e) {
						LOGGER.error("auth notify error, return content:" + result.getReData(), e);
					} catch (IOException e) {
						LOGGER.error("auth notify error,return content:" + result.getReData(), e);
					} finally {
						try {
							if (out != null) {
								out.close();
							}
						} catch (IOException e) {
							LOGGER.error("auth close error ，return  content:" + result.getReData(), e);
						}
					}
				}
			}
		}catch(Exception t){
			try {
				response.sendRedirect(PayCenterConfig.getProperty("face_pay_auth_fail_url").toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
