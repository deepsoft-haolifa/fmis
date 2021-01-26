package com.ruoyi.quartz.task;

import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.finance.service.IBizBillAmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;

import java.util.logging.Logger;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask {

    @Autowired
    private IBizCustomerService bizCustomerService;

    @Autowired
    private IBizBillAmountService billAmountService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }

    /**
     * 客户类别执行
     * 合同通过的客户
     * 超过12个自然月的 成单 老客户
     * 不超过12个月的  成单 新客户
     */
    public void ryCustomerLevel() {
        System.out.println("客户类别start...");

        bizCustomerService.updateCustomerLeverJob();

        System.out.println("客户类别end...");
    }

    /**
     * 日记账的金额
     * 每个月25号0点，记录上一个月的余额
     */
    public void ryMonthBillAmount() {
        billAmountService.updateBillAmountJob();
    }
}
