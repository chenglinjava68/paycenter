package com.qccr.paycenter.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qccr.paycenter.model.convert.SpringConvert;
import com.qccr.paycenter.model.dal.so.pay.PayQueryResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.dal.so.refund.bocom.BoComRefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.bocom.BocomCancelParamSO;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.ValidatorHandler;
import org.junit.Test;

import java.util.Date;

/**
 * author: denghuajun
 * date: 2016/3/8 10:31
 * version: v1.0.0
 */
public class ValidateHandlerTest {

    @Test
    public void test(){
        ValidatorHandler validatorHandler = new ValidatorHandler();
        ValidateState state = new ValidateState();
        ValidateState state2 = new ValidateState();
        ValidateModel model = new ValidateModel();
        model.setAge(300);
        Long start = System.currentTimeMillis();
        validatorHandler.volidate(state,model);
        System.out.println(System.currentTimeMillis() - start);
        ValidateModel model2  = new ValidateModel();
        model2.setAge(0);
        Long start2 = System.currentTimeMillis();
        validatorHandler.volidate(state2,model2);
        System.out.println(System.currentTimeMillis() - start2);
        System.out.println(state.isPass());
        System.out.println(state.getErrorMsg());
        System.out.println(model.getName());
        System.out.println(state2.getErrorMsg());
    }

    @Test
    public void convert() throws InterruptedException {
        RefundParamSO refundParamSO =new RefundParamSO();
        refundParamSO.setSerialNo("1111");
        refundParamSO.setRefundNo("222");
        BocomCancelParamSO paramSO =  SpringConvert.convert(refundParamSO, BocomCancelParamSO.class);
        System.out.println(paramSO);
               new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("dsfasfaf");
                JSONObject returnParam =  JSON.parseObject("{\"addData\":\"00000\",\"addDataField\":\"\",\"authNo\":\"769134\",\"hostDate\":\"20160819\",\"hostTime\":\"144036\",\"returnCode\":\"00\",\"version\":\"1.0\"}");
                System.out.println(returnParam);
            }
        }).start();

        System.out.println(System.getProperty("user.dir"));
        Thread.sleep(100000);
    }

    @Test
    public void convert2() throws InterruptedException {
        PayQuerySO refundParamSO =new PayQuerySO();
        refundParamSO.setPayStartTime(new Date());
        refundParamSO.setBillNo("222");

        PayQueryResultSO resultSO = new PayQueryResultSO();
        resultSO.setErrorMsg("dgasdgag");
        resultSO.setBillNo("1111");
        PayQueryResultSO paramSO =  SpringConvert.convert(refundParamSO, PayQueryResultSO.class);
        PayQuerySO payQuerySO = SpringConvert.convert(resultSO, PayQuerySO.class);
        System.out.println(paramSO);
        System.out.println(payQuerySO);
        String str = "asagR00Aasalgj";
         System.out.println(str.indexOf("R00A"));

    }
}
