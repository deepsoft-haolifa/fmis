package com.ruoyi.fmis.common;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

@Data
public class BizSuppliersImport {

    @Excel(name = "序号")
    private String seqNum;

    @Excel(name = "区域")
    private String area;

    @Excel(name = "供应商名称（代号）")
    private String name;

    @Excel(name = "供应商代码")
    private String code;


}
