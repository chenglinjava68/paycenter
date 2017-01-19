package com.qccr.paycenter.common.utils;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.qccr.paycenter.common.exception.BeanTransException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 潘成忠 ~ 2015年6月12日 下午7:21:08
 * @version 1.0 添加对象的序列化和反序列化，处理redis缓存
 */
public class ObjectUtils {
	private ObjectUtils() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectUtils.class);

	private static Map<String,BeanContext> contexts = new HashMap<String,BeanContext>();

	public static byte[] serialize(Object object) {
		HessianOutput hos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			hos = new HessianOutput(baos);
			hos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new byte[0];
		}
	}

	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		if(bytes==null||bytes.length==0)
			return null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			HessianInput hi = new HessianInput(bais);
			return hi.readObject();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}


	/**
	 *
	 * @param paramStr
	 * @param clazz
	 * @param <T>
     * @return
     */
	public static <T> T paramStr2Bean(String paramStr,Class<T> clazz) throws BeanTransException{
		BeanContext context = contexts.get(clazz.getName());
		try{
			if(context==null){
				context = fillBeanContext(clazz);
				contexts.put(clazz.getName(),context);
			}
			T obj = clazz.newInstance();
			String[] kvArr =  paramStr.split("&");
			for(int index=0;index<kvArr.length;index++){
				setValue(kvArr[index],context,obj);
			}
			return obj;
		} catch (InstantiationException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (InvocationTargetException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (IntrospectionException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (NoSuchFieldException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return null;
	}

	public static BeanContext fillBeanContext(Class clazz) throws IntrospectionException {
		BeanContext context = new BeanContext(clazz.getName(),clazz);
		Field[] fields =  clazz.getDeclaredFields();
		for(int index = 0;index<fields.length;index++){
			Field field =  fields[index];
			context.getFieldCache().put(field.getName(),field);
		}
		return context;
	}

	public static void setValue(String kv,BeanContext context,Object obj) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, IntrospectionException {
		int  index =  kv.indexOf("=");
		String key = kv.substring(0,index);
		String value = kv.substring(index+1);
		Field field = context.getFieldCache().get(key);
		if(field!=null){
			FieldUtils.writeField(field,obj,trans(value,field),true);
		}
	}

	public static Object trans(String value,Field field){
		Class type = field.getType();
		LogUtil.info(LOGGER, type.getName());
		if(String.class.isAssignableFrom(type)) {
			return  value;
		} else if(Date.class.isAssignableFrom(type)) {
			Date date = null;
			try {
				if(value.length()==14){
					date = DateUtil.parse(value);
				}else if(value.length()==19){
					date = DateUtil.parseToSecond(value);
				}
			} catch (ParseException e) {
				LOGGER.error("时间转化异常,请核对时间格式是否正确");
			}
			return date;
		} else if(Boolean.class.isAssignableFrom(type)) {
			Boolean bool=true;

			if("false".equals(value)) {
				bool=false;
			}
			return bool;
		} else if(Integer.class.isAssignableFrom(type)) {
			return Integer.valueOf(value);
		} else if(Float.class.isAssignableFrom(type)) {
			return Float.valueOf(value);
		} else if(Long.class.isAssignableFrom(type)) {
			return Long.valueOf(value);
		} else if(Double.class.isAssignableFrom(type)) {
			return Double.valueOf(value);
		}
		return null;
	}

	/**
	 * 转化bean上下文，使用本地存储
	 */
	static class BeanContext{

		private String beanName;

		private Map<String,Field> fieldCache = new HashMap<String,Field> ();

		private Class clazz;

		public BeanContext(String beanName, Class clazz) {
			this.beanName = beanName;
			this.clazz = clazz;
		}

		public String getBeanName() {
			return beanName;
		}

		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}

		public Class getClazz() {
			return clazz;
		}

		public void setClazz(Class clazz) {
			this.clazz = clazz;
		}

		public Map<String, Field> getFieldCache() {
			return fieldCache;
		}

		public void setFieldCache(Map<String, Field> fieldCache) {
			this.fieldCache = fieldCache;
		}
	}

}
