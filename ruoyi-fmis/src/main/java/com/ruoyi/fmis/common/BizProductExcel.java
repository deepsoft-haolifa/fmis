package com.ruoyi.fmis.common;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

@Data
public class BizProductExcel {

    @Excel(name = "产品型号")
    private String model;
    @Excel(name = "等级")
    private String level;
    @Excel(name = "数量")
    private String num;
    @Excel(name = "规格")
    private String specifications;
    /*
    private String series;
    @Excel(name = "系列")
    private String string1;//系列
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
    @Excel(name = "密封材质")
    private String sealingMaterial;
    @Excel(name = "阀芯材质")
    private String valveElement;
    @Excel(name = "驱动形式")
    private String driveForm;*/
}
