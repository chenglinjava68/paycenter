package com.qccr.paycenter.model.validator;

public abstract class AbstractValidator<T> implements Validator<T>{
	
	public void notEmpty(ValidateState state,String str,String errorMsg){
		if(!state.isPass()){
			return ;
		}
		boolean validate = false;
		validate = (str!=null&& !"".equals(str));
		state.setPass(validate);
		if(!validate){
			state.setErrorMsg(errorMsg);
		}			
	}
	
	
	public void notNegative(ValidateState state,int value,String errorMsg){
		if(!state.isPass()){
			return ;
		}		
		boolean validate = false;
		validate = (value > 0);
		state.setPass(validate);
		if(!validate){
			state.setErrorMsg(errorMsg);
		}	
	}
	
	
	public void notNull(ValidateState state,Object value,String errorMsg){
		if(!state.isPass()){
			return ;
		}		
		boolean validate = false;
		validate = (value != null);
		state.setPass(validate);
		if(!validate){
			state.setErrorMsg(errorMsg);
		}	
	}
	

}
