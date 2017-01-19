package com.qccr.paycenter.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

public class JsonUtil {
	private JsonUtil() {
	}

	public static String toJSONString(Object object){
		
		return JSON.toJSONString(object);
	}
		
	public static <T> T parseObject(String text,Class<T> clazz){
		
		return  JSON.parseObject(text, clazz);
		
	}

	/**
	 * 保持顺序
	 * @param text
	 * @param clazz
	 * @param feature= Feature.OrderedField
	 * @param <T>
     * @return
     */
	public static <T> T parseObject(String text,Class<T> clazz, Feature feature){

		return  JSON.parseObject(text, clazz, feature);

	}

}
