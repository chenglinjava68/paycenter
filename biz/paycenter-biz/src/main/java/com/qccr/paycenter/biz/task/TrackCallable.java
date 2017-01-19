package com.qccr.paycenter.biz.task;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 内部业务跟踪线程 
 * @author denghuajun
 * date:2015年12月17日 下午3:46:44
 */
public class TrackCallable implements Callable{
	
	
	private Object handle;
	
	private Method method;
		
	private Object[] args;
	


	public TrackCallable( Object handle,Method method, Object[] args) {
		super();
		this.method = method;
		this.handle = handle;
		this.args = args;
	}



	@Override
	public Object call() throws Exception {
		method.invoke(handle, args);
		return null;
	}

}
