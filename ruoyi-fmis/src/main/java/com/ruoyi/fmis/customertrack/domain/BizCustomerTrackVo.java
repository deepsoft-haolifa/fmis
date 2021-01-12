package com.ruoyi.fmis.customertrack.domain;

import com.ruoyi.fmis.customer.domain.BizCustomer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 客户列表（包含追踪）
 *
 * @author murphy.he
 **/
@Data
public class BizCustomerTrackVo extends BizCustomer {
    private static final long serialVersionUID = 1L;

    /**
     * 追踪反馈
     */
    private String feedback;
    /**
     * 反馈时间
     */
    private Date feedbackDate;

}
