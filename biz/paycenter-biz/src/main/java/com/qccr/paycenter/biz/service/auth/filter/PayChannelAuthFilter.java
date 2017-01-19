package com.qccr.paycenter.biz.service.auth.filter;

import com.qccr.paycenter.facade.dal.ro.pay.PayParamRO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.validator.ValidateState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PayChannelAuthFilter extends AbstractAuthFilter<PayParamRO>{
	
	private Set<ChannelEnum> channelSet =  new HashSet<ChannelEnum>();

	public PayChannelAuthFilter() {
		super();
		channelSet.add(ChannelEnum.ALIPAY);
		channelSet.add(ChannelEnum.WECHAT);
		channelSet.add(ChannelEnum.UNION);
		channelSet.add(ChannelEnum.CZBANK);
		channelSet.add(ChannelEnum.OFFLINE);
		channelSet.add(ChannelEnum.BOCOM);
	}

	@Override
	public ValidateState doFilter(ValidateState state,PayParamRO obj, int next, List<AuthFilter> chain) {
		ChannelEnum channel = ChannelEnum.valueOf(obj.getPayChannel());
		if(channelSet.contains(channel)){
			state.setPass(true);
		}else{
			state.setPass(false);
			state.setErrorMsg("channel not exists");
		}
		return doNextFilter(state,obj,next,chain);
	}

	public Set<ChannelEnum> getChannelSet() {
		return channelSet;
	}

	public void setChannelSet(Set<ChannelEnum> channelSet) {
		this.channelSet = channelSet;
	}
}
