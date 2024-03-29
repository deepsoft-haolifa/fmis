package com.ruoyi.fmis.finance.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.Constant;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.CommonUtils;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.service.IBizCustomerService;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.fmis.datatrack.domain.BizProcessDataTrack;
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

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private IBizCustomerService customerService;

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


    @RequiresPermissions("finance:billContract:saleView")
    @GetMapping("/saleView")
    public String saleBillContract() {
        return prefix + "/saleBillContract";
    }

    @RequiresPermissions("finance:billContract:saleList")
    @PostMapping("/saleList")
    @ResponseBody
    public TableDataInfo saleList(BizBankBill bizBankBill) {
        startPage();
        bizBankBill.setType("1");
        bizBankBill.setCollectionType("2");
        // 根据当前用户负责的客户ID
        BizCustomer bizCustomer =new BizCustomer();
        bizCustomer.setUserId(ShiroUtils.getUserId().toString());
        List<BizCustomer> customersList = customerService.selectBizCustomerSelfList(bizCustomer);
        if(CollectionUtils.isEmpty(customersList)){
            return getDataTable(new ArrayList<>());
        }
        List<String> customerIdList = Optional.ofNullable(customersList).orElse(new ArrayList<>()).stream().map(e->String.valueOf(e.getCustomerId())).collect(Collectors.toList());
        bizBankBill.setPayCompanyIdList(customerIdList);

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
        // 获取银行日记账中 收款 中的付款单位，未付款的完成销售订单
        Long billId = bizProcessData.getBillId();
        BizBankBill bizBankBill = bizBankBillService.selectBizBankBillById(billId);
        //采购池
        BizProcessData newBizProcessData = new BizProcessData();
        newBizProcessData.setString2(bizBankBill.getPayCompanyId());
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
        Double contractAmount = bizProcessData.getPrice1();
        // 2. 查询该合同已经分解的金额
        BizBillContract query = new BizBillContract();
        query.setDataId(Long.parseLong(dataId));
        List<BizBillContract> bizBillContractList = bizBillContractService.selectBizBillContractList(query);
        double dataAmount = bizBillContractList.stream().mapToDouble(BizBillContract::getAmount).sum();
        double splitAmount = dataAmount + Double.parseDouble(amount);
        logger.info("add contract v:{},dataAmount:{},dataId:{}", splitAmount, dataAmount, dataId);
        if (splitAmount > contractAmount) {
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

        //判断合同分解是否完成
        BizBankBill updateBill = new BizBankBill();
        if (totalAmount >= collectionMoney) {
            // 将分解状态更改为分解完成
            updateBill.setContractStatus("2");
        } else {
            updateBill.setContractStatus("0");
        }
        updateBill.setContractUser(ShiroUtils.getSysUser().getUserName());
        updateBill.setBillId(bizBankBill.getBillId());
        bizBankBillService.updateBizBankBill(updateBill);

        // 更新合同的回款状态
        BizProcessData updateProcessData = new BizProcessData();
        updateProcessData.setDataId(Long.parseLong(dataId));
        if (new BigDecimal(splitAmount).compareTo(new BigDecimal(contractAmount)) < 0) {
            updateProcessData.setString17(Constant.collectionStatus.PART);
        } else {
            updateProcessData.setString17(Constant.collectionStatus.ALREADY);
        }
        bizProcessDataService.updateBizProcessData(updateProcessData);
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


    @RequiresPermissions("finance:billContract:detail")
    @GetMapping("/detail/{billId}")
    public String billContract(@PathVariable("billId") Long billId,ModelMap modelMap) {
        modelMap.put("billId",billId);
        return prefix + "/detailList";
    }

    @PostMapping("/detailList/{billId}")
    @ResponseBody
    public TableDataInfo saleList(@PathVariable("billId") Long billId) {
        startPage();
        BizBillContract bizBillContract = new BizBillContract();
        bizBillContract.setBillId(billId);
        List<BizBillContract> list = bizBillContractService.selectBizBillContractList(bizBillContract);
        return getDataTable(list);
    }

    @RequiresPermissions("finance:billContract:audit")
    @GetMapping("/audit/{bcId}")
    public String audit(@PathVariable("bcId")Long bcId,ModelMap mmap) {
        mmap.put("bcId",bcId);
        return prefix + "/audit";
    }

    @PostMapping("/auditContract")
    @ResponseBody
    public AjaxResult auditContract(BizBillContract billContract) {
        return toAjax(bizBillContractService.updateBizBillContract(billContract));
    }


}
