package com.qccr.paycenter.biz.service.auth;

import javax.servlet.http.HttpServletRequest;

import com.qccr.paycenter.model.dal.so.notify.AuthNotifySO;

public interface AuthNotifyService {
	AuthNotifySO authNotify(String authChannel, String authType, HttpServletRequest request);
}
