package com.qccr.paycenter.biz.bo.refund;

import java.util.Map;
import com.qccr.paycenter.dal.domain.refund.RefundSerialDO;

/**
 * 退款流水，公共调用组件
 * @author denghuajun
 * date:2015年12月15日 下午6:12:53
 */
public interface RefundSerialBO {
	
	void insert(RefundSerialDO refundSerialDO);
	
	void updateStatusBySerialNo(Map map);

}
