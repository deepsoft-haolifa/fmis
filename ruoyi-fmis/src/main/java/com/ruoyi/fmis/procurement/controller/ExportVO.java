package com.ruoyi.fmis.procurement.controller;

import lombok.Data;

/**
 * @ClassName ExportVO
 * @Description TODO
 * @Author liuyaofei
 * @Date 2024/3/28 10:46
 **/
@Data
public class ExportVO {

    @ExcelHandle(name = "下单日期")
    private String createTime;
    @ExcelHandle(name = "订单号")
    private String orderNo;
    @ExcelHandle(name = "供应商")
    private String supplier;
    @ExcelHandle(name = "型号")
    private String model;
    @ExcelHandle(name = "名称")
    private String name;
    @ExcelHandle(name = "规格")
    private String specifications;
    @ExcelHandle(name = "压力")
    private String pressure;
    @ExcelHandle(name = "数量")
    private String number;
    @ExcelHandle(name = "单价")
    private Double unitPrice;
    @ExcelHandle(name = "总价")
    private Double totalPrice;
    @ExcelHandle(name = "材质要求")
    private String materialRequirements;
    @ExcelHandle(name = "合同编号")
    private String contractNumber;
    @ExcelHandle(name = "发货地点")
    private String shippingLocation;
    @ExcelHandle(name = "订单要求日期")
    private String orderDate;
    @ExcelHandle(name = "备注")
    private String remark;




}
