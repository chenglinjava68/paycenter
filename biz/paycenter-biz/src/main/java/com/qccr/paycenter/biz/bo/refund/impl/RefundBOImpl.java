package com.qccr.paycenter.biz.bo.refund.impl;


import com.qccr.paycenter.biz.bo.refund.RefundBO;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.dal.dao.refund.RefundOrderDao;
import com.qccr.paycenter.dal.dao.refund.RefundSerialDao;
import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@SuppressWarnings({"unchecked","rawtypes"})
public class RefundBOImpl implements RefundBO{

	private static final Logger LOGGER = LoggerFactory.getLogger(RefundBOImpl.class);

	@Resource
	public RefundOrderDao refundOrderDao;

	@Resource
	public RefundSerialDao refundSerialDao;
	/*用于可更新状态*/
	private List<Integer> list = Arrays.asList(RefundOrderEnum.CREATED.getValue(),
			RefundOrderEnum.TIMING.getValue(),RefundOrderEnum.NOTSURE.getValue());


	@Override
	public void refundSuccess(final RefundNotifySO notifySO) {
		RefundOrderDO refundOrderDo = null;
		refundOrderDo = refundOrderDao.findByRefundNo(notifySO.getRefundNo());
		notifySO.setPartner(refundOrderDo.getPartner());
		notifySO.setBusinessNo(refundOrderDo.getBusinessNo());
		notifySO.setBusinessType(refundOrderDo.getBusinessType());
		notifySO.setOutRefundNo(refundOrderDo.getOutRefundNo());
		notifySO.setRefundNo(refundOrderDo.getRefundNo());
		RefundOrderEnum statusEnum = RefundOrderEnum.valueOf(refundOrderDo.getStatus());
		if(!statusEnum.equals(RefundOrderEnum.SUCCESS)&&!statusEnum.equals(RefundOrderEnum.FINISH)){
			notifySO.setNeedNotify(true);
			if(notifySO.getSuccessTime() == null){
				notifySO.setSuccessTime(new Date());
			}
			LogUtil.info(LOGGER,"refund success merger  refund order ,set status = 1,the refund no"+notifySO.getRefundNo());
			refundOrderDao.refundSuccess(notifySO.getRefundNo(),notifySO.getRefundBillNo(),refundOrderDo.getRefundSerialNo(),notifySO.getSuccessTime());
			refundSerialDao.refundSuccess(notifySO.getRefundBillNo(),refundOrderDo.getRefundSerialNo(),notifySO.getSuccessTime());
		}

	}

	@Override
	public void refundFail(final RefundNotifySO notifySO) {
		RefundOrderDO refundOrderDo = null;
		refundOrderDo = refundOrderDao.findByRefundNo(notifySO.getRefundNo());
		notifySO.setPartner(refundOrderDo.getPartner());
		notifySO.setBusinessNo(refundOrderDo.getBusinessNo());
		notifySO.setBusinessType(refundOrderDo.getBusinessType());
		notifySO.setOutRefundNo(refundOrderDo.getOutRefundNo());
		notifySO.setRefundNo(refundOrderDo.getRefundNo());
		RefundOrderEnum statusEnum = RefundOrderEnum.valueOf(refundOrderDo.getStatus());
		RefundOrderEnum modifyStatusEnum = RefundOrderEnum.get(notifySO.getStatus());
		if(!statusEnum.equals(RefundOrderEnum.CREATED)&&!statusEnum.equals(RefundOrderEnum.TIMING)){
			LogUtil.info(LOGGER,"refund fail merger  refund order ,set status = 2,the refund no"+notifySO.getRefundNo());
			notifySO.setNeedNotify(true);
			refundOrderDao.refundMerge(list,notifySO.getRefundNo(),notifySO.getRefundBillNo(),modifyStatusEnum.getValue(),refundOrderDo.getRefundSerialNo());
			refundSerialDao.refundMerge(list,notifySO.getRefundBillNo(),modifyStatusEnum.getValue(),refundOrderDo.getRefundSerialNo());

		}
	}

	@Override
	public boolean orderIsSuccess(String refundNo) {

		return false;
	}



}
