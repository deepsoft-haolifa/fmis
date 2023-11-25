package com.ruoyi.fmis.file.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class BizManageFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String fileName;

    private String fileType;

    private String fileClassfiy;

    private Integer delFlag;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;

}
