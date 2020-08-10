package com.test;


import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;

public class Common {

    public static Document jsonpConnect(String url, Boolean isMobile) {
        Document doc = null;
        if (StrUtil.isNotEmpty(url)) {
            try {
                url = ReUtil.get(PatternPool.URL_HTTP, url, 0);
                Connection connect = Jsoup.connect(url);
                if (isMobile) {
                    connect.headers(Common.getMobileHeaders());
                }
                doc = connect.timeout(5000).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return doc;
    }

    public static String getLocationUrl(String url) {
        HashMap headers = getMobileHeaders();
        HttpResponse execute = HttpUtil.createGet(url).addHeaders(headers).execute();
        String redirectUrl = execute.header("Location");
        return redirectUrl;
    }

    public static HashMap<String, String> getMobileHeaders() {
        HashMap headers = MapUtil.newHashMap();
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Mobile Safari/537.36");
        // headers.put("User-Agent", "Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1");
        // headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Mobile Safari/537.36");
        return headers;
    }
}
