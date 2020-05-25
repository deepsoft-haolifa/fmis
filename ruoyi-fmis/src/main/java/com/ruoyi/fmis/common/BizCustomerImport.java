package com.ruoyi.fmis.common;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class BizCustomerImport {

    //数据字典 customer_area
    @Excel(name = "客户区域")
    private String area;
    //数据字典 customer_level
    @Excel(name = "客户类别")
    private String customerLevel;
    @Excel(name = "客户名称")
    private String name;
    @Excel(name = "客户代号")
    private String codeName;
    @Excel(name = "初次备案日期")
    private Date recordDate;
    //数据字典 industry_division
    @Excel(name = "行业划分")
    private String industryDivision;
    //数据字典 customer_source
    @Excel(name = "客户来源")
    private String source;
    //数据字典 customer_company_code
    @Excel(name = "客户所属商务公司代码")
    private String areaCode;
    @Excel(name = "隶属部门")
    private String organization;
    @Excel(name = "业务负责人")
    private String owner;
    @Excel(name = "分配日期")
    private Date allocationDate;
    //分配次数 assign_number
    @Excel(name = "分配次数")
    private String assignNumber;
    @Excel(name = "档案编号")
    private String fileNumber;
    //数据字典  customer_record_type
    @Excel(name = "备案类别")
    private String recordType;
    @Excel(name = "项目备案号")
    private String recordNum;
    @Excel(name = "项目名称")
    private String projectAme;
    //数据字典 customer_brand
    @Excel(name = "品牌")
    private String brand;
    @Excel(name = "主要产品")
    private String productInfo;
    @Excel(name = "客户联系人")
    private String contactName;
    @Excel(name = "座机电话")
    private String telephone;
    @Excel(name = "联系传真")
    private String fax;
    @Excel(name = "QQ/E-mail")
    private String contactEmail;
    @Excel(name = "手机")
    private String contactPhone;
    @Excel(name = "公司地址")
    private String companyAddress;
    @Excel(name = "开票资料上传")
    private String temp;
    @Excel(name = "备注")
    private String remark;





}
