package com.qccr.paycenter.common.utils;

import org.slf4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 2016/6/29.
 */
public class LogUtil {
    private LogUtil() {
    }

    public static void info(Logger logger, String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public static void info(Logger logger, String message, Object obj) {
        if (logger.isInfoEnabled()) {
            logger.info(message, obj);
        }
    }

    public static void debug(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public static void debug(Logger logger, String message, Throwable throwable) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, throwable);
        }
    }

    /**
     * 将需要保密的数据遮挡打印
     * @param logger
     * @param message
     */
    public static void infoSecurity(Logger logger, String message) {
        if (logger.isInfoEnabled()) {
            logger.info(findAndReturnStar(message));
        }
    }

    public static void logDebugMessage(Logger logger, String name, Object ... params) {
        if (logger.isDebugEnabled())
            logger.debug(toLogMessage(name, params));
    }

    private static String toLogMessage(String name, Object ... params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append("::");
        for (int i = 0 ; i < params.length; i++) {
            stringBuilder.append(params[i]).append("|");
        }
        String message = findAndReturnStar(stringBuilder.toString());
        return message;
    }

    public static String findAndReturnStar(String message) {
//        Pattern pattern = Pattern.compile(".*cardNo>(.+?)</cardNo.*");
        Pattern pattern = Pattern.compile("[0-9]{16}");
        Matcher matcher = pattern.matcher(message);
        if(matcher.find()){
            System.out.println(matcher.group());
            String cardNo = matcher.group();
            message = message.replaceAll(cardNo.substring(0,12),"************");
        }
        return message;
    }
}
