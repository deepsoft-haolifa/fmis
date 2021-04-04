package com.ruoyi.fmis.index.dto;

import com.ruoyi.common.core.page.PageDomain;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class OrderAuditDTO extends PageDomain {

    /**
     * 工单类型：
     *
     */
    private String orderType;

    private Long userId;


}
