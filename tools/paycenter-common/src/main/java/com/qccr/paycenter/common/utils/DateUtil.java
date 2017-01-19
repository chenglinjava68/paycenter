package com.qccr.paycenter.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private DateUtil() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
	/**默认日期格式*/
	public static final String PATTERN_DATE = "yyyy-MM-dd";

	public static final String PATTERN_TO_SECOND = "yyyy-MM-dd HH:mm:ss";

	public static final String  PATTERN = "yyyyMMddHHmmss";

	private static final  String PATTERN_TO_MINUTE = "yyyy-MM-dd HH:m";

	/**
	 * yyyy-MM-dd
	 * @param timeStr
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp parseTime(final String timeStr) throws ParseException{
		return new Timestamp(new SimpleDateFormat(PATTERN_DATE).parse(timeStr).getTime());
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @param timeStr
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp parseTime2Second(final String timeStr) throws ParseException{
		return new Timestamp(new SimpleDateFormat(PATTERN_TO_SECOND).parse(timeStr).getTime());
	}

	public static Date parse(final String timeStr, final String pattern) throws ParseException{
		return new Date(new SimpleDateFormat(pattern).parse(timeStr).getTime());
	}

	/**
	 * yyyy-MM-dd HH:mm:ss,时间格式
	 * @param timeStr
	 * @return
	 * @throws ParseException
     */
	public static Date parseToSecond(final String timeStr) throws ParseException {
		 return new SimpleDateFormat(PATTERN_TO_SECOND).parse(timeStr);
	}

	/**
	 * yyyyMMddHHmmss,时间格式
	 * @param timeStr
	 * @return
	 * @throws ParseException
     */
	public static Date parse(final String timeStr) throws ParseException {
		 return new SimpleDateFormat(PATTERN).parse(timeStr);
	}

	/**
	 * 默认 yyyy-MM-dd HH:mm:ss
	 * @param timestamp
	 * @return
	 */
	public static String format(final Timestamp timestamp){
		return new SimpleDateFormat(PATTERN_TO_SECOND).format(new Date(timestamp.getTime()));
	}

	/**
	 *
	 * @param timestamp
	 * @param pattern
	 * @return
	 */
	public static String format(final Timestamp timestamp, final String pattern){
		return new SimpleDateFormat(pattern).format(new Date(timestamp.getTime()));
	}

	/**
	 * yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String format2Date(final Date date){
		return new SimpleDateFormat(PATTERN_DATE).format(date);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String format2Second(final Date date){
		return new SimpleDateFormat(PATTERN_TO_SECOND).format(date);
	}

	public static String formatDate(final Date date, String pattern){
		return new SimpleDateFormat(pattern).format(date);
	}

	public static String format(final Date date){
		return new SimpleDateFormat(PATTERN).format(date);
	}

	public static Date roundMinute(Date date) throws ParseException {

		return new SimpleDateFormat(PATTERN_TO_MINUTE).parse(format2Second(date));
	}



	public static void main(String args[]){
		Date date = new Date(System.currentTimeMillis());

		long tenminute = date.getTime()/1000/60/10*1000*60*10;
		Date date2 = new Date(tenminute);

		date = new Date((System.currentTimeMillis()/1000/60/10 +1)*1000*60*10);
		try {
			LogUtil.info(LOGGER,format2Second(date2));
			LogUtil.info(LOGGER,format2Second(date));
			LogUtil.info(LOGGER,format2Second(roundMinute(date)));
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
