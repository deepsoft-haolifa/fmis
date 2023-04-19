package com.ruoyi.fmis.common;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

@Data
public class BizActuatorImport {

    @Excel(name = "序号")
    private String xh;

    @Excel(name = "产品名称")
    private String name;

    @Excel(name = "品质等级")
    private String qualityLevel;

    @Excel(name = "系列")
    private String string3;

    @Excel(name = "型号")
    private String string1;

    @Excel(name = "执行器品牌")
    private String brand;

    @Excel(name = "安装形式")
    private String setupType;

    @Excel(name = "输出力距")
    private String outputTorque;

    @Excel(name = "开启时间")
    private String actionType;

    @Excel(name = "控制电路")
    private String controlCircuit;

    @Excel(name = "适用电压")
    private String adaptableVoltage;

    @Excel(name = "防护等级")
    private String protectionLevel;

    @Excel(name = "防爆等级")
    private String explosionLevel;

    @Excel(name = "生产厂家")
    private String manufacturer;

    @Excel(name = "厂家代码")
    private String string4;

    @Excel(name = "价格")
    private String price;
    @Excel(name = "采购价")
    private String string6;
    @Excel(name = "成本价")
    private String string9;

    @Excel(name = "单价")
    private String areaPrice;

    @Excel(name = "备注")
    private String remark;

    //气动
    @Excel(name = "等级")
    private String areaQualityLevel;

    @Excel(name = "产品型号（ID）")
    private String areaString1;

    @Excel(name = "类别")
    private String areaString3;

    @Excel(name = "气缸形式")
    private String areaString4;

    @Excel(name = "材质")
    private String string5;

    @Excel(name = "供应商代码")
    private String string8;
    @Excel(name = "颜色")
    private String color;
    @Excel(name = "压力")
    private String press;
    @Excel(name = "面价（元）")
    private String facePrice;

}
