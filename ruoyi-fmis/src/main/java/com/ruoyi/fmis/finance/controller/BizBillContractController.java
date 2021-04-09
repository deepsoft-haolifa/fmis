package com.ruoyi.fmis.finance.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.finance.domain.BizBankBill;
import com.ruoyi.fmis.finance.domain.BizBillContract;
import com.ruoyi.fmis.finance.service.IBizBankBillService;
import com.ruoyi.fmis.finance.service.IBizBillContractService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 合同收款(合同分解)Controller
 *
 * @author hedong
 * @date 2020-10-22
 */
@Controller
@RequestMapping("/finance/billContract")
public class BizBillContractController extends BaseController {
    private String prefix = "finance/billContract";

    @Autowired
    private IBizBillContractService bizBillContractService;
    @Autowired
    private IBizBankBillService bizBankBillService;
    @Autowired
    private IBizProcessDataService bizProcessDataService;
    @Autowired
    private IBizProcessDefineService bizProcessDefineService;


    @RequiresPermissions("finance:billContract:view")
    @GetMapping()
    public String billContract() {
        return prefix + "/billContract";
    }

    /**
     * 查询合同收款列表(银行记账-收款，收款类别是货款)
     */
    @RequiresPermissions("finance:billContract:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizBankBill bizBankBill) {
        startPage();
        bizBankBill.setType("1");
        bizBankBill.setCollectionType("2");
        List<BizBankBill> list = bizBankBillService.selectBizBankBillList(bizBankBill);
        return getDataTable(list);
    }

    /**
     * 合同分解编辑
     */
    @GetMapping("/edit/{billId}")
    public String edit(@PathVariable("billId") Long billId, ModelMap mmap) {
        BizBankBill bizBankBill = bizBankBillService.selectBizBankBillById(billId);
        mmap.put("bizBill", bizBankBill);
        return prefix + "/edit";
    }


    @PostMapping("/contractList")
    @ResponseBody
    public TableDataInfo contractList(BizProcessData bizProcessData) {
        // 获取银行日记账中 收款 中的付款单位，未付款的完成采购订单
        Long billId = bizProcessData.getBillId();
        BizBankBill bizBankBill = bizBankBillService.selectBizBankBillById(billId);
        //采购池
        BizProcessData newBizProcessData = new BizProcessData();
        newBizProcessData.setString2(bizBankBill.getPayCompany());
        newBizProcessData.setBizId(BizConstants.BIZ_contract);
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefBill(newBizProcessData);
        return getDataTable(list);
    }


    @PostMapping("/selectBizBillChildList")
    @ResponseBody
    public TableDataInfo selectBizBillChildList(BizBillContract billContract) {
        BizBillContract queryBizBill = new BizBillContract();
        queryBizBill.setDataId(billContract.getDataId());
        queryBizBill.setBillId(billContract.getBillId());
        List<BizBillContract> bizProcessChildList = bizBillContractService.selectBizBillContractList(queryBizBill);
        return getDataTable(bizProcessChildList);
    }


    @PostMapping("/addContract")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addContract() {
        String bcId = getRequest().getParameter("bcId");
        String dataId = getRequest().getParameter("dataId");//合同id
        String billId = getRequest().getParameter("billId");//业务数据id
        String remark = getRequest().getParameter("remark");
        String amount = getRequest().getParameter("amount");


        //一.判断合同的金额是否够分
        // 1. 查询合同的总价
        BizProcessData bizProcessData = bizProcessDataService.selectBizProcessDataById(Long.parseLong(dataId));
        Double price1 = bizProcessData.getPrice1();
        // 2. 查询该合同已经分解的金额
        BizBillContract query = new BizBillContract();
        query.setDataId(Long.parseLong(dataId));
        List<BizBillContract> bizBillContractList = bizBillContractService.selectBizBillContractList(query);
        double dataAmount = bizBillContractList.stream().mapToDouble(BizBillContract::getAmount).sum();
        double v = dataAmount + Double.parseDouble(amount);
        logger.info("add contract v:{},dataAmount:{},dataId:{}", v, dataAmount, dataId);
        if (v > price1) {
            return error("此合同的已付金额+这次分解的金额 大于合同金额");
        }

        //二.判断银行日记账的金额是否够分
        // 1. 查询银行日记账的总价
        BizBankBill bizBankBill = bizBankBillService.selectBizBankBillById(Long.parseLong(billId));
        Double collectionMoney = bizBankBill.getCollectionMoney();
        // 2. 查询该合同已经分解的金额
        BizBillContract query1 = new BizBillContract();
        query1.setBillId(Long.parseLong(billId));
        List<BizBillContract> bizBillContracts = bizBillContractService.selectBizBillContractList(query1);
        double alreadyAmount = bizBillContracts.stream().mapToDouble(BizBillContract::getAmount).sum();
        double totalAmount = alreadyAmount + Double.parseDouble(amount);
        logger.info("add contract totalAmount:{},dataAmount:{},billId:{}", totalAmount, alreadyAmount, billId);
        if (totalAmount > collectionMoney) {
            return error("已分配金额+这次分解的金额 大于此笔收款金额");
        }


        BizBillContract bizBillContract = new BizBillContract();
        if (!"0".equals(bcId) && StringUtils.isNotEmpty(bcId)) {
            bizBillContract = bizBillContractService.selectBizBillContractById(Long.parseLong(bcId));
        }
        bizBillContract.setBillId(Long.parseLong(billId));
        bizBillContract.setDataId(Long.parseLong(dataId));
        bizBillContract.setAmount(Double.parseDouble(amount));
        bizBillContract.setBookKeeper(ShiroUtils.getUserId().toString());
        bizBillContract.setRemark(remark);
        if ("0".equals(bcId) || StringUtils.isEmpty(bcId)) {
            bizBillContract.setCreateTime(new Date());
            bizBillContract.setCreateBy(ShiroUtils.getUserId().toString());
            bizBillContractService.insertBizBillContract(bizBillContract);
        } else {
            bizBillContractService.updateBizBillContract(bizBillContract);
        }

        BizProcessData updateProcessData = new BizProcessData();
        //判断合同分解是否完成
        BizBankBill updateBill = new BizBankBill();
        if (totalAmount >= collectionMoney) {
            // 将分解状态更改为分解完成
            updateBill.setContractStatus("1");
        } else {
            updateBill.setContractStatus("0");
        }
        updateBill.setBillId(bizBankBill.getBillId());
        bizBankBillService.updateBizBankBill(updateBill);
        return toAjax(1);
    }


    @PostMapping("/removeContract")
    @ResponseBody
    public AjaxResult removeTest() {
        String bcId = getRequest().getParameter("bcId");
        String billId = getRequest().getParameter("billId");//业务数据id
        if ("0".equals(bcId)) {
            return toAjax(1);
        }
        bizBillContractService.deleteBizBillContractById(Long.parseLong(bcId));
        BizBankBill updateBill = new BizBankBill();
        updateBill.setBillId(Long.parseLong(billId));
        bizBankBillService.updateBizBankBill(updateBill);
        updateBill.setContractStatus("0");
        return toAjax(1);
    }
}
