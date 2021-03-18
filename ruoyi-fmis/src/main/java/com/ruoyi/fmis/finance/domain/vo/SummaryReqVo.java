package com.ruoyi.fmis.finance.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 合计
 *
 * @author murphy.he
 **/
@Data
public class SummaryReqVo {


    private String supplierName;

    private String customerName;


    private String startTime;
    private String endTime;
}
