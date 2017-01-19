package com.qccr.paycenter.common.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * author: denghuajun
 * date: 2016/3/7 17:59
 * version: v1.0.0
 */
public class RegexUtil {
    private RegexUtil() {
    }

    private static ConcurrentMap<String,Pattern> map = new ConcurrentHashMap<String,Pattern>();

    static{

        map.put("ip",Pattern.compile("(25[0-5]|2[0-4]\\\\d|1\\\\d{2}|[1-9]?\\\\d)(\\\\.(25[0-5]|2[0-4]\\\\d|1\\\\d{2}|[1-9]?\\\\d)){3}"));
        map.put("email",Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"));

    }

}
