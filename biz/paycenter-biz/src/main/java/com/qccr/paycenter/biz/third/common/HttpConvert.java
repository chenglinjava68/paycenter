package com.qccr.paycenter.biz.third.common;

import com.qccr.paycenter.common.utils.HttpUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * httpclient相关转化
 * author: denghuajun
 * date: 2016/4/6 11:47
 * version: v1.0.0
 */
public class HttpConvert {
    private HttpConvert() {
    }

    public static String post(Map<String,String> param , String url) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(url);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        List<String> keys = new ArrayList<String>(param.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = param.get(key);
            formparams.add(new BasicNameValuePair(key,value));
        }
        UrlEncodedFormEntity uefEntity=null;
        uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
        post.setEntity(uefEntity);
        return HttpUtil.excute(post);
    }

}
