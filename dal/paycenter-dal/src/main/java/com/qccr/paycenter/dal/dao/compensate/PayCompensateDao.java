package com.qccr.paycenter.dal.dao.compensate;

import com.qccr.paycenter.dal.domain.compensate.PayCompensateDO;
import com.qccr.paycenter.model.dal.dbo.compensate.PayCompensateQueryDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 支付补偿数据库操作
 * author: denghuajun
 * date: 2016/3/5 11:21
 * version: v1.0.0
 */
public interface PayCompensateDao {

    public void insert(PayCompensateDO compensateDO);

    public PayCompensateDO findByPayNoAndChannel(String payNo,String channel);

    public  PayCompensateDO findBycompensateNo(String compentsateNo);

    public void refundUpdate(Map map);

    public Integer refundSuccess(@Param(value = "source") String  source,@Param(value = "compensateNo")String compensateNo,
                                 @Param(value="refundBillNo") String refundBillNo,@Param(value="successTime") Date successTime);

    public Integer refundFail(@Param(value = "compensateNo")String compensateNo, @Param(value="refundBillNo") String refundBillNo);

    public int findPayCompensateCount(PayCompensateQueryDO payCompensateQueryDO);

    public List<PayCompensateDO> findPayCompensateList (PayCompensateQueryDO payCompensateQueryDO);

    public String existsPayNo(@Param(value = "payNo")String payNo,@Param(value = "payChannel")String payChannel);

    public List<PayCompensateDO> queryTimingInThreeDays(@Param(value = "minAmount") int minAmount, @Param(value = "maxAmount") int maxAmount);

    public void  setSourceAndRefundTime(@Param(value = "source") String  source,@Param(value = "compensateNo")String compensateNo);

    void refundMerge(@Param(value = "statusList") List<Integer> statusList,@Param(value="compensateNo") String compensateNo,
                     @Param(value="refundBillNo") String refundBillNo,@Param(value="status") Integer status);


    public void refundAsyncMerge(Map map);

    /**
     *  20160408 lim
     * @Title: queryTimingInDaysBySharding
     * @Description: 查找补偿里面创建时间在days天内的金额在minAmount和maxAmount之间，id对shardingNum取摸在shardingList内。
     * @param @param days 天数
     * @param @param minAmount 最小退款金额
     * @param @param maxAmount 最大退款金额
     * @param @param shardingNum 分片数
     * @param @param shardingList 分配到的分片
     * @param @return    设定文件
     * @return List<RefundOrderDO>    返回类型
     * @throws
     */
    List<PayCompensateDO> queryTimingInDaysBySharding(@Param(value = "days") int days, @Param(value = "minAmount") int minAmount,@Param(value = "maxAmount") int maxAmount, @Param(value = "shardingNum") int shardingNum, @Param(value = "shardingList") List<Integer> shardingList);

    public List<PayCompensateDO> findPayCompensateListByBizNo(String bizNo);

    public List<PayCompensateDO> findPayCompensateByBillNo(String billNo);

}
