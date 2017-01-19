package com.qccr.paycenter.control.callback.pay;

import com.qccr.paycenter.biz.service.pay.PayNotifyService;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
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
@RequestMapping(value = "/pay/notify")
public class PayNotifyController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PayNotifyController.class);
	
	@Resource
	private PayNotifyService payNotifyService;

	@RequestMapping(value = "/{payChannel}/{payType}/{partner}")
	public void payNotify(@PathVariable(value="payChannel") String payChannel,@PathVariable(value="partner") String partner,
			@PathVariable(value="payType") String payType,HttpServletRequest request,HttpServletResponse response){
		LogUtil.info(LOGGER, "pay notify payChannel:"+payChannel+",payType:"+payType+",partner:"+partner);
		Enumeration rnames = request.getParameterNames();
		for (Enumeration e = rnames ; e.hasMoreElements() ;) {
			String thisName = e.nextElement().toString();
			String thisValue = request.getParameter(thisName);
			LOGGER.info(thisName+"=="+thisValue);
		}
		PayNotifySO result =  payNotifyService.payNotify(payChannel, payType, request,partner);
		if(result.isHasReturn()){
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				out.write(result.getReData().getBytes("utf-8"));
				LogUtil.info(LOGGER, "pay notify success, return content:"+result.getReData());
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("pay notify error, return content:"+result.getReData(), e);
			} catch (IOException e) {
				LOGGER.error("pay notify error,return content:"+result.getReData(), e);
			}finally{
				try {
					if(out != null) {
						out.close();
					}
				} catch (IOException e) {
					LOGGER.error("pay close error ，return  content:"+result.getReData(), e);
				}
			}
		}
	}

}
