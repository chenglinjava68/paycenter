package com.qccr.paycenter.biz.third.common;


import com.qccr.paycenter.biz.third.alipay.model.SingleQueryResult;
import com.qccr.paycenter.biz.third.wechat.model.WechatPayCloseResult;
import com.qccr.paycenter.biz.third.wechat.model.WechatPayQueryResult;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * xml处理工具
 * author: denghuajun
 * date: 2016/4/5 16:13
 * version: v1.0.0
 */
public class XMLProUtil {

	/**
	 * log
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(XMLProUtil.class);
	private static JAXBContext context;
	private static Marshaller marshaller;

	static{
		try {
			context = JAXBContext.newInstance(SingleQueryResult.class,WechatPayQueryResult.class, WechatPayCloseResult.class);
			marshaller = context.createMarshaller();
		} catch (JAXBException e) {
			LOGGER.error("xml工具初始化异常:"+e.getMessage(), e);
		}
	}

	/**
	 * xml转化为bean
	 * @param xml
	 * @param <T>
     * @return
     */
	public static <T> T parseBean(String xml) throws JAXBException {
		Unmarshaller unmarshaller = context.createUnmarshaller();
		T obj = (T) unmarshaller.unmarshal(new StringReader(xml));
		return obj;
	}

	/**
	 * 将bean转为xml
	 * @param target
	 * @return
     */
	public static String parseXml(Object target) throws JAXBException {
		Writer writer =  new StringWriter();
		String result = null;
		marshaller.marshal(target,writer);
		result = writer.toString();
		return  result;
	}

	/**
	 * 将XML格式的数据转换成MAP,用于 微信支付回调接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> domToMap(HttpServletRequest request) throws Exception {

		SAXReader sb = new SAXReader();

		Document doc = sb.read(getRequestText(request));
		// map中直存取节点元素
		Map<String, String> map = new HashMap<String, String>();
		if (doc == null)
			return map;
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();

			List list = e.elements();
			if (list.size() > 0) {
				continue;
			}
			map.put(e.getName(), e.getText());
		}
		return map;
	}

	/**
	 * 将XML格式的数据转换成MAP,用于 微信支付回调接口
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> strToMap(String str) {
		// map中直存取节点元素
		Map<String, String> map = new HashMap<String, String>();
		try {
			Document doc = DocumentHelper.parseText(str);
			if (doc == null) {
				throw new RuntimeException("doc==null");
			}
			Element root = doc.getRootElement();
			for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
				Element e = (Element) iterator.next();

				List list = e.elements();
				if (list.size() > 0) {
					continue;
				}
				map.put(e.getName(), e.getText());
			}
		} catch (DocumentException e1) {
			LOGGER.error(e1.getMessage(),e1);
			map.put("return_code", "0000");
			map.put("return_msg", "本地xml解析失败");
			throw new RuntimeException(e1);
		}
			
		return map;
	}

	/**
	 * 获取请求内容，解析，返回响应内容
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static InputStream getRequestText(HttpServletRequest request) throws IOException {
		ByteArrayInputStream stream = null;
		request.setCharacterEncoding("utf-8");
		try(ServletInputStream sis = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(sis);
			BufferedReader br = new BufferedReader(isr);) {
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			final String sbstr = sb.toString();
			LOGGER.info("sbstr:::"+sbstr);
			stream = new ByteArrayInputStream(sbstr.getBytes("utf-8"));
		}
		return stream;
	}

	/**
	 * 相关节点的数据
	 */
	public static Map noteMap(Element e) {
		Map map = new HashMap();
		List list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();

				if (iter.elements().size() > 0) {
					Map m = noteMap(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if(! (obj instanceof ArrayList)) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj instanceof ArrayList) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), m);
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!(obj instanceof ArrayList)) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj instanceof ArrayList) {
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), iter.getText());
				}
			}
		} else
			map.put(e.getName(), e.getText());
		return map;
	}
	
	public static String parseXML(Map<String,String> map, String firstElement){
		Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
		Document doc = DocumentHelper.createDocument();
		Element xml = DocumentHelper.createElement(firstElement);
		doc.setRootElement(xml);
		while (iterator.hasNext()) {
			Map.Entry<String, String> entity = iterator.next();
			if(entity!=null && entity.getValue()!=null){
				xml.addElement(entity.getKey()).setText(entity.getValue().toString());;
			}
		}
		return xml.asXML();
	}

	/**
	 * JavaBean转换成xml
	 * @param obj
	 * @param encoding
	 * @return
	 */
	public static String convertToXml(Object obj, String encoding) {
		String result = null;
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

			StringWriter writer = new StringWriter();
			marshaller.marshal(obj, writer);
			result = writer.toString();
		} catch (PropertyException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (JAXBException e) {
			LOGGER.error(e.getMessage(),e);
		}

		return result;
	}

	public static Map<String, Object> dom2Map(Document doc){
		Map<String, Object> map = new HashMap<String, Object>();
		if(doc == null)
			return map;
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			//LogUtil.info(LOGGER, e.getName());
			List list = e.elements();
			if(list.size() > 0){
				map.put(e.getName(), noteMap(e));
			}else
				map.put(e.getName(), e.getText());
		}
		return map;
	}

	/**
	 * String 转换为 Document 对象
	 *
	 * @param xml 字符串
	 * @return Document 对象
	 */
	public static Document string2Doc(String xml) {
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(new ByteArrayInputStream(xml.getBytes()));
//			Element incomingForm = document.getRootElement();
			return document;
		} catch (DocumentException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 *
	 * @param xml
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws JAXBException
     */
	public static <T> T parseXmlBean(String xml,Class<T> clazz) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		T obj = (T) unmarshaller.unmarshal(new StringReader(xml));
		return obj;
	}

}
