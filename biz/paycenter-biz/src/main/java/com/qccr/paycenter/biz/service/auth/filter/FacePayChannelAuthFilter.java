package com.qccr.paycenter.biz.service.auth.filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.qccr.paycenter.facade.dal.ro.pay.FacepayParamRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayParamRO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.validator.ValidateState;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FacePayChannelAuthFilter extends AbstractAuthFilter<FacepayParamRO>{

	private Set<ChannelEnum> channelSet =  new HashSet<ChannelEnum>();

	public FacePayChannelAuthFilter() {
		super();
		channelSet.add(ChannelEnum.ALIPAY);
	}

	@Override
	public ValidateState doFilter(ValidateState state,FacepayParamRO obj, int next, List<AuthFilter> chain) {
		ChannelEnum channel = ChannelEnum.valueOf(obj.getChannel());
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
