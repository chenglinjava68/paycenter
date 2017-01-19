package com.qccr.paycenter.control.callback.compensate;

import com.qccr.paycenter.biz.service.compensate.CompensateNotifyServie;
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

/**
 * author: denghuajun
 * date: 2016/3/9 20:23
 * version: v1.0.0
 */
@Controller
@RequestMapping(value = "/paycompensate/notify")
public class PayCompensateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayCompensateController.class);

    @Resource
    private CompensateNotifyServie compensateNotifyServie;

    @RequestMapping(value = "/{refundChannel}/{refundType}/{partner}")
    public void refundNotify(@PathVariable(value="refundChannel") String refundChannel, @PathVariable(value="partner") String partner,
                          @PathVariable(value="refundType") String refundType, HttpServletRequest request, HttpServletResponse response){
        LogUtil.info(LOGGER, "paycompensate notify refundChannel:"+refundChannel+",refundType:"+refundType+",partner:"+partner);
        RefundNotifySO result =  compensateNotifyServie.compensateNotify(refundChannel,refundType, request,partner);
        if(result.isHasReturn()){
            OutputStream out = null;
            try {
                out = response.getOutputStream();
                out.write(result.getReData().getBytes("utf-8"));
                LogUtil.info(LOGGER, "paycompensate notify success, retrun content:"+result.getReData());
            } catch (Exception e) {
                LOGGER.error("paycompensate notify error, retrun content:"+result.getReData(),e);
            }finally{
                try {
                    if(out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("paycompensate notify close error, retrun content:"+result.getReData(),e);
                }
            }
        }
    }

}
