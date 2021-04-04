package com.ruoyi.fmis.index.service;

import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.fmis.index.dto.OrderAuditDTO;

import java.util.List;

public interface ITodoService {
    /**
     * 获取个人待办事项
     * @return
     */
    TableDataInfo getPageListForTodo(OrderAuditDTO orderAuditDTO) throws Exception;

    TableDataInfo getPageListForDone(OrderAuditDTO orderAuditDTO);

}
