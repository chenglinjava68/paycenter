package com.qccr.paycenter.biz.service.track;

import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;

/**
 * 退款操作采用模式,退款订单校验完毕返回受理成功结果给调用方。
 * 后续退款操作会下放跟踪器，跟踪器依旧退款真实结果，回调业务方。
 * @author denghuajun
 * date:2015年12月15日 下午5:55:15
 */
public interface RefundTrackService {
	
	/**
	 * 退款跟踪处理
	 * @param paramSO
	 * @param resultSO
	 */
	void track(RefundParamSO paramSO,RefundResultSO resultSO);


}
