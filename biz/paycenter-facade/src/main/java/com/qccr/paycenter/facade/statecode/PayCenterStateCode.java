package com.qccr.paycenter.facade.statecode;

import com.qccr.knife.result.StateCode;
/**  
 * 自定义的stateCode, code统一采用7位int表示，xxx(应用码),xx(模块码),xx(状态码码)
 * 使用204，01-支付 02退款,03支付补偿
 * @author denghuajun
 * @date 2015年12月2日 下午5:34:20
 * @version: v1.0.0
 */
public class PayCenterStateCode {
	private PayCenterStateCode() {
	}

	public static final StateCode CHANNEL_NOT_EXIST = new StateCode(-2040003, "对应渠道不存在");
	public static final StateCode PAY_ERROR = new StateCode(-2040101, "支付失败");
	public static final StateCode PAY_OVER = new StateCode(-2040102, "该笔订单过期");
	public static final StateCode PAY_REPEAT = new StateCode(-2040103, "订单已经完成支付");
	public static final StateCode PAY_CLOSE = new StateCode(-2040108, "该笔订单已关闭");
	public static final StateCode PAY_CLOSE_PARAM_ERROR = new StateCode(-2040109, "订单关闭参数异常");
	public static final StateCode PAY_CHECK_ERROR = new StateCode(-2040104, "支付订单查询失败，参数不足");
	public static final StateCode PAY_PARAM_ERROR = new StateCode(-2040112, "支付参数异常");


	public static final StateCode PAY_QUERY_ERROR = new StateCode(-2040105, "支付订单,第三方回查失败");
	public static final StateCode PAY_CLOSE_REPEAT = new StateCode(-2040111, "订单已经关闭");
	public static final StateCode PAY_CLOSE_ERROR = new StateCode(-2040106, "支付订单关闭失败");
	public static final StateCode PAY_CLOSE_SOON = new StateCode(-2040107, "支付订单,即将关闭请勿支付");
	public static final StateCode PAY_CLOSE_NO_BUSINESS_NO = new StateCode(-2040110, "关闭订单，找不到订单号");
	public static final StateCode PAY_CLOSE_PAYED = new StateCode(-2040114, "订单支付完成，不可关闭");
	public static final StateCode PAY_TRADE_CANCEL_ERROR = new StateCode(-2040115, "交易撤销异常");

	public static final StateCode REFUND_ERROR = new StateCode(-2040201, "退款失败");
	public static final StateCode REFUND_PARAM_ERROR = new StateCode(-2040202, "退款参数异常");
	public static final StateCode REFUND_REPEAT = new StateCode(-2040203, "已经退款，请勿重复操作");
	public static final StateCode REFUND_TIMING = new StateCode(-2040204, "退款中，请等待退款通知");
	public static final StateCode REFUND_CHECK_ERROR = new StateCode(-2040205, "退款订单查询失败，参数不足");

	public static final StateCode PAYCOMPENSATE_ERROR = new StateCode(-2040301, "补偿退款失败");
	public static final StateCode PAYCOMPENSATE_PARAM_ERROR = new StateCode(-2040302, "退款参数异常");
	public static final StateCode PAYCOMPENSATE_REPEAT = new StateCode(-2040303, "补偿已经退款，请勿重复操作");

	public static final StateCode MARK_PARAM_ERROR = new StateCode(-2040402, "参数异常");

	public static final StateCode SIGN_PARAM_ERROR = new StateCode(-2040502, "参数异常");
	public static final StateCode UN_SIGNED_ERROR = new StateCode(-2040503, "未签约");
	public static final StateCode CARD_EXIST_ERROR = new StateCode(-2040504, "卡号已经存在");

	public static final StateCode OPEN_AUTH_TOKEN_ERROR = new StateCode(-2040602, "换取应用授权令牌异常");
	public static final StateCode NULL_AUTH_TOKEN_ERROR = new StateCode(-2040603, "当前用户未授权");
	public static final StateCode FACE_PAY_ERROR = new StateCode(-2040604, "当面付支付宝返回异常");
	public static final StateCode TIMEOUT_AUTH_TOKEN_ERROR = new StateCode(-2040608, "当前用户TOKEN已过期");

	public static final StateCode FACE_PAY_ERROR_WAIT = new StateCode(-2040605, "等待用户付款");
	public static final StateCode FACE_PAY_ERROR_FAIL = new StateCode(-2040606, "支付失败");
	public static final StateCode FACE_PAY_ERROR_UNKNOW = new StateCode(-2040607, "未知异常");

}
