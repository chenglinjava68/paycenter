package com.qccr.paycenter.biz.third.wechat.util;

import com.qccr.commons.base.Source;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/8/11 10:44 $
 */
public class BWechatAppPaySource {

    private static Map<String,String> bpartners = new HashMap();

    static {
        bpartners.put(Source.CASHCOW.toString().toLowerCase(),Source.CASHCOW.toString().toLowerCase());
        bpartners.put(Source.BORDERPROD.toString().toLowerCase(),Source.BORDERPROD.toString().toLowerCase());
       // bpartners.put(Source.BTRADE.toString().toLowerCase(),Source.BTRADE.toString().toLowerCase());
    }

    public static boolean  lookup(String partner){
        return bpartners.containsKey(partner);
    }

}
