package com.test;


import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.cn;

@Slf4j
public class LiveResource {

    public static void main(String[] args) {
        yy("54880976");

        //huYa("52513");

        //yingKe("727419703");
    }

    private static String yingKe(String roomNumber) {
        String interfaceUrl = StrFormatter.format("https://webapi.busi.inke.cn/web/live_share_pc?uid={}", roomNumber);
        String jsonStr = HttpUtil.get(interfaceUrl);
        log.debug(JSONUtil.toJsonPrettyStr(jsonStr));
        JSON json = JSONUtil.parse(jsonStr);
        String hls = JSONUtil.getByPath(json, "data.live_addr[0].hls_stream_addr").toString();
        log.debug("映客直播间: [{}] , 直播源: [{}] ", roomNumber, hls);
        return hls;
    }

    /**
     * 获取虎牙直播间直播源
     *
     * @param roomNumber 直播间号
     * @return
     */
    private static String huYa(String roomNumber) {
        String interfaceUrl = StrFormatter.format("https://m.huya.com/{}", roomNumber);
        Document doc = Common.jsonpConnect(interfaceUrl, true);
        Elements select = doc.select("video[id=html5player-video]");
        String src = select.get(0).attr("src");
        String hls = "https:" + StrUtil.subBefore(src, "?", true);
        log.debug("虎牙直播间: [{}] , 直播源: [{}] ", roomNumber, hls);
        return hls;
    }

    /**
     * 获取虎牙直播间直播源
     *
     * @param roomNumber 直播间号
     * @return
     */
    private static String yy(String roomNumber) {
        HashMap<String, String> headers = Common.getMobileHeaders();
        headers.put("referer", StrFormatter.format("http://wap.yy.com/mobileweb/{}", roomNumber));
        String interfaceUrl = StrFormatter.format("http://interface.yy.com/hls/new/get/{}/{}/1200?source=wapyy&callback=jsonp3", roomNumber, roomNumber);
        String body = HttpUtil.createPost(interfaceUrl).addHeaders(headers).execute().body();
        String jsonStr = StrUtil.sub(body, 7, body.length() - 1);
        JSON json = JSONUtil.parse(jsonStr);
        String hls = JSONUtil.getByPath(json, "hls").toString();
        System.out.println(StringUtils.concat(roomNumber, hls));
        return hls;
    }
}