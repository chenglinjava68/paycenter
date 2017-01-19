package com.qccr.paycenter.model.config;

import com.qccr.framework.insight.entry.Insight;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * 动态参数配置，获取
 * @author denghuajun
 * date:2015年12月3日 下午3:36:49
 */
public class PayCenterConfig {
	private PayCenterConfig() {
	}

	private static Map<String,Object> properties = new HashMap();
	private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private static final Lock readLock = readWriteLock.readLock();
	private static final Lock writeLock = readWriteLock.writeLock();

	public static void setProperties(Map<String,Object> properties) {
		PayCenterConfig.properties = properties;		
	}
		
	public static Object getProperty(String name){

		readLock.lock();
	    try {
			return Insight.props().getObject(name);
//	    	return properties.get(name);
	    	
	    }finally { readLock.unlock(); }				
	}
	
	public static String getValue(String name,String defaultValue){
		Object obj = getProperty(name);
		if(obj==null){
			return defaultValue;
		}
		return obj.toString();
	}

	public static Boolean getBool(String name,Boolean defaultValue){
		Object obj = getProperty(name);

		if(obj==null){
			return defaultValue;
		}
		return Boolean.valueOf(obj.toString());
	}

	public static Long getLong(String  name,long defaultValue){
		Object obj = getProperty(name);
		if(obj==null){
			return defaultValue;
		}
		return Long.valueOf(obj.toString());
	}

	public static Integer getInteger(String  name,int defaultValue){
		Object obj = getProperty(name);
		if(obj==null){
			return defaultValue;
		}
		return Integer.valueOf(obj.toString());
	}
	
	public static void setProperty(String name,String value){	
		writeLock.lock();
	    try { 
	    	properties.put(name, value);	
	    	
	    }finally { writeLock.unlock(); }
					
	}
	
	public static void remove(String name){		
		writeLock.lock();
	    try { 
	    	properties.remove(name);		    	
	    }finally { writeLock.unlock(); }			
	}
	/**
	 * 获取商户证书地址
	 * @param name
	 * @return
	 */
	public static String getSSLCertPath(String name,String defaultValue){
		Object obj = getProperty(name);
		if(obj==null){
			return defaultValue;
		}
		return obj.toString();
	}
	
	public static String getCertPassword(String name,String defaultValue){
		Object obj = getProperty(name);
		if(obj==null){
			return defaultValue;
		}
		return obj.toString();
	}
	
}
