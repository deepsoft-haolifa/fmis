package com.ruoyi.fmis.index.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.fmis.index.dto.OrderAuditDTO;
import com.ruoyi.fmis.index.service.ITodoService;
import com.ruoyi.framework.util.ShiroUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/fmis/orderAudit")
public class TodoController extends BaseController {

    @Autowired
    private ITodoService todoService;

    /**
     * 获取待办列表
     * @param orderAuditDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/todo")
    @ResponseBody
    public TableDataInfo myTodo(OrderAuditDTO orderAuditDTO) throws Exception {
        Long userId = ShiroUtils.getUserId();
        orderAuditDTO.setUserId(userId);
        return todoService.getPageListForTodo(orderAuditDTO);
    }

    /**
     * 获取已办列表
     * @param orderAuditDTO
     * @return
     */
    @PostMapping("/done")
    @ResponseBody
    public TableDataInfo myDone(OrderAuditDTO orderAuditDTO) throws Exception {
        Long userId = ShiroUtils.getUserId();
        orderAuditDTO.setUserId(userId);
        return todoService.getPageListForDone(orderAuditDTO);

    }

    /**
     * 获取工单类型
     * @return
     * @throws Exception
     */
    @GetMapping("/orderTypes")
    @ResponseBody
    public HashMap getOrderTypes() throws Exception {
        HashMap<String, String> orderTypeMaps = todoService.getOrderTypeMaps();
        orderTypeMaps.put("-1", "全部");
        return orderTypeMaps;
    }

}
