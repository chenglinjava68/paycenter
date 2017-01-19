package com.qccr.paycenter.biz.task.job.timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.qccr.paycenter.biz.task.job.JobContext;
import org.springframework.stereotype.Component;

import com.qccr.paycenter.biz.service.compensate.PayCompensateService;
import com.qccr.paycenter.biz.service.refund.RefundService;
import com.qccr.paycenter.biz.task.compensate.PayCompensateCheckCallable;
import com.qccr.paycenter.biz.task.job.ItemsWorker;
import com.qccr.paycenter.biz.task.refund.RefundCheckBackCallable;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.dao.compensate.PayCompensateDao;
import com.qccr.paycenter.dal.dao.refund.RefundOrderDao;
import com.qccr.paycenter.dal.domain.compensate.PayCompensateDO;
import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;
import com.qccr.paycenter.model.convert.refund.RefundCheckBackConvert;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.enums.RefundTypeEnum;

/**
 * 
  * @ClassName: RefundCheckBackWorker
  * @Description: 退款查询：1小时 
		每间隔1小时查三天内的1到1000元的状态为0，4，6（创建，退款进行中，不确定状态）的退款订单
  * @author Lim
  * @date 2016年4月8日 下午4:58:48
  *
 */
@Component
public class RefundCheckBackLowAmountWorker extends ItemsWorker<RefundCheckBackSO> {

	@Resource
	private RefundOrderDao refundOrderDao;

	@Resource
	private PayCompensateDao payCompensateDao;

	@Resource
	private RefundService refundService;

	@Resource
	private PayCompensateService payCompensateService;
    
	@Override
	public List<RefundCheckBackSO> fetch(List<Integer> integers) {
		//查三天内，金额在1到1000元之间，id对5取摸在integers内的退款订单
		List<RefundOrderDO> refundList = refundOrderDao.queryTimingInDaysBySharding(3, 1, 100000, JobContext.DEFAULT_SHARDING_COUNT, integers);
        RefundCheckBackSO refundCheckBackSO = null;
        List<RefundCheckBackSO> result = new ArrayList<RefundCheckBackSO>();
        RefundOrderDO order = null;
        for(int index=0, len = refundList.size(); index<len; index++){
            order = refundList.get(index);
            refundCheckBackSO = RefundCheckBackConvert.refundOrderDOToRefundCheckBackSO(order);
            refundCheckBackSO.setRefundType(RefundTypeEnum.NORMAL);
            result.add(refundCheckBackSO);
        }
		//查三天内，金额在1到1000元之间，id对5取摸在integers内的支付补偿
        List<PayCompensateDO> compensateList = payCompensateDao.queryTimingInDaysBySharding(3, 1, 100000, JobContext.DEFAULT_SHARDING_COUNT, integers);
        PayCompensateDO payCompensateDO = null;
        for(int index=0,len=compensateList.size(); index<len; index++){
            payCompensateDO = compensateList.get(index);
            refundCheckBackSO = RefundCheckBackConvert.payCompensateDOToRefundCheckBackSO(payCompensateDO);
            refundCheckBackSO.setRefundType(RefundTypeEnum.PAY_COMPENSATE);
            result.add(refundCheckBackSO);
        }
		return result;
	}

	@Override
	public boolean process(RefundCheckBackSO t) {
		if(t.getRefundType() == RefundTypeEnum.NORMAL) {
			RefundCheckBackCallable callable = new RefundCheckBackCallable(refundService,t);
			ExecutorUtil.schedule(callable, 3, TimeUnit.SECONDS);//退款的延时时间不太要求精准
		}else {
			PayCompensateCheckCallable callable= new PayCompensateCheckCallable(payCompensateService,refundService,t);
			ExecutorUtil.schedule(callable, 3, TimeUnit.SECONDS);
		}
		return true;
	}
 
}
