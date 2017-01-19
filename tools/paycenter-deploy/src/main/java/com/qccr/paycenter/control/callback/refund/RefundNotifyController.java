package com.qccr.paycenter.control.callback.refund;

import com.qccr.paycenter.biz.service.refund.RefundNotifyService;
import com.qccr.paycenter.common.utils.JsonUtil;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
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

@Controller
@RequestMapping(value = "/refund/notify")
public class RefundNotifyController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundNotifyController.class);
	
	@Resource
	private RefundNotifyService refundNotifyService;
	
	@RequestMapping(value = "/{refundChannel}/{refundType}/{partner}")
	public void payNotify(@PathVariable(value="refundChannel") String refundChannel,@PathVariable(value="partner") String partner,
			@PathVariable(value="refundType") String refundType,HttpServletRequest request,HttpServletResponse response){
		LogUtil.info(LOGGER, "refund notify refundChannel:"+refundChannel+",refundType:"+refundType+",partner:"+partner);
		RefundNotifySO result =  refundNotifyService.refundNotify(refundChannel,refundType, request,partner);
		if(result.isHasReturn()){
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				out.write(result.getReData().getBytes("utf-8"));
				LogUtil.info(LOGGER, "refund notify success, return content:"+result.getReData());
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("refund notify error ，return  content:"+result.getReData(), e);
			} catch (IOException e) {
				LOGGER.error("refund notify error ，return  content:"+result.getReData(), e);
			}finally{
				try {
					if(out != null) {
						out.close();
					}
				} catch (IOException e) {
					LOGGER.error("refund notify close error ，return  content:"+result.getReData(), e);
				}
			}
		}
	}
	
}
