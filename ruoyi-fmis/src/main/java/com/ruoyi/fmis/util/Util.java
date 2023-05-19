package com.ruoyi.fmis.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Util {
    public static  JSONObject jsonObject;

    public Util() {
        init();
    }

    public static void init(){
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", "北京好利阀业集团有限公司");
        jsonObject1.put("phone", "010-89215299");
        jsonObject1.put("bank", "中国农业银行北京榆垡支行");
        jsonObject1.put("bankNo", "11111401040001485");
        jsonObject1.put("faxNo", "91110115802867079B");
        jsonObject1.put("address", "北京市大兴区榆垡镇榆顺路6号");

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name", "北京好利时代科技发展有限公司");
        jsonObject2.put("phone", "010-89215233");
        jsonObject2.put("bank", "中行北京幸福大街支行");
        jsonObject2.put("bankNo", "338956035438");
        jsonObject2.put("faxNo", "91110115666294594T");
        jsonObject2.put("address", "北京市大兴区榆垡镇榆顺路6号");

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("name", "上海好利阀门技术有限公司");
        jsonObject3.put("phone", "010-89215233");
        jsonObject3.put("bank", "工行上海市梅陇支行");
        jsonObject3.put("bankNo", "1001715009003459996");
        jsonObject3.put("faxNo", "91310112069349040D");
        jsonObject3.put("address", "上海市闵行区兴梅路375号1楼A区");

        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("name", "山西好利阀机械制造有限公司");
        jsonObject4.put("phone", "0357-3563581");
        jsonObject4.put("bank", "中国建设银行侯马开发区支行");
        jsonObject4.put("bankNo", "14001716254052501038");
        jsonObject4.put("faxNo", "91141000571670143");
        jsonObject4.put("address", "侯马经济开发区旺旺北支路东侧");

        JSONObject jsonObject5 = new JSONObject();
        jsonObject5.put("name", "北京大宇合力科技有限责任公司");
        jsonObject5.put("phone", "010-67182056");
        jsonObject5.put("bank", "中国建设银行北京天坛支行");
        jsonObject5.put("bankNo", "11001014800053004539");
        jsonObject5.put("faxNo", "110101634364383");
        jsonObject5.put("address", "北京市东城区广渠门内大街90号楼办公5层506");
        jsonObject = new JSONObject();
        jsonObject.put("北京好利阀业集团有限公司",jsonObject1);
        jsonObject.put("北京好利时代科技发展有限公司",jsonObject2);
        jsonObject.put("上海好利阀门技术有限公司",jsonObject3);
        jsonObject.put("山西好利阀机械制造有限公司",jsonObject4);
        jsonObject.put("北京大宇合力科技有限责任公司",jsonObject5);
    }

}
