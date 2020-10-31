package com.ruoyi.fmis.common;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

@Data
public class BizProductImport {

    @Excel(name = "序号")
    private String seqNum;

    @Excel(name = "产品名称")
    private String name;
    @Excel(name = "类别")
    private String series;

    @Excel(name = "等级")
    private String string2;

    @Excel(name = "系列")
    private String string1;
    @Excel(name = "产品型号（ID）")
    private String model;

    @Excel(name = "规格")
    private String specifications;
    @Excel(name = "公称压力")
    private String nominalPressure;
    @Excel(name = "连接方式")
    private String connectionType;
    @Excel(name = "结构形式")
    private String structuralStyle;
    @Excel(name = "阀体材质")
    private String valvebodyMaterial;
    @Excel(name = "阀芯材质")
    private String valveElement;
    @Excel(name = "密封材质")
    private String sealingMaterial;
    @Excel(name = "驱动形式")
    private String driveForm;

    @Excel(name = "产品单价（元）")
    private String price;

    @Excel(name = "供应商名称（代号）")
    private String supplier;

    @Excel(name = "供应商代码")
    private String supplierCode;

    @Excel(name = "备注")
    private String remark;

    @Excel(name = "颜色")
    private String color;

    @Excel(name = "好利型号")
    private String string4;

    @Excel(name = "阀轴材质")
    private String valveShaft;





}
