package com.ruoyi.fmis.customertrack.domain;

import lombok.Data;

import java.util.Date;

/**
 * 客户列表（包含追踪）
 *
 * @author murphy.he
 **/
@Data
public class BizCustomerTrackVo {

    /**
     * 追踪反馈
     */
    private String feedback;
    /**
     * 反馈时间
     */
    private Date feedbackDate;


    /** 客户ID */
    private Long customerId;

    /** 备案日期 */
    private Date recordDate;

    /** 商务公司代码 */
    private String companyCode;

    /** 客户所属区域 */
    private String area;

    /** 项目备案号 */
    private String recordCode;

    /** 业务负责人 */
    private Long ownerUserId;

    /** 客户名称 */
    private String name;

    /** 项目名称 */
    private String projectAme;

    /** 联系人姓名 */
    private String contactName;

    /** 联系人职务 */
    private String contactPosition;

    /** 联系人电话 */
    private String contactPhone;

    /** 联系人邮箱 */
    private String contactEmail;

    /** 品牌 */
    private String brand;

    /** 客户/信息 */
    private String info;

    /** 涉及产品 */
    private String productInfo;

    /** 状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
}
