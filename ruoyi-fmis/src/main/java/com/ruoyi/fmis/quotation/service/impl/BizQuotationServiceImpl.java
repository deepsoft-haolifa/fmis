package com.ruoyi.fmis.quotation.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.customer.domain.BizCustomer;
import com.ruoyi.fmis.customer.mapper.BizCustomerMapper;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.flow.domain.BizFlow;
import com.ruoyi.fmis.flow.mapper.BizFlowMapper;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.service.ISysRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.quotation.mapper.BizQuotationMapper;
import com.ruoyi.fmis.quotation.domain.BizQuotation;
import com.ruoyi.fmis.quotation.service.IBizQuotationService;
import com.ruoyi.common.core.text.Convert;

/**
 * 报价单Service业务层处理
 *
 * @author frank
 * @date 2020-03-18
 */
@Service
public class BizQuotationServiceImpl implements IBizQuotationService {
    @Autowired
    private BizQuotationMapper bizQuotationMapper;

    @Autowired
    private BizFlowMapper bizFlowMapper;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private BizCustomerMapper bizCustomerMapper;
    @Autowired
    private IBizProcessDefineService bizProcessDefineService;
    /**
     * 查询报价单
     *
     * @param quotationId 报价单ID
     * @return 报价单
     */
    @Override
    public BizQuotation selectBizQuotationById(Long quotationId) {

        return bizQuotationMapper.selectBizQuotationById(quotationId);
    }

    /**
     * 查询报价单列表
     *
     * @param bizQuotation 报价单
     * @return 报价单
     */
    @Override
    public List<BizQuotation> selectBizQuotationList(BizQuotation bizQuotation) {
        return bizQuotationMapper.selectBizQuotationList(bizQuotation);
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizQuotation> selectBizQuotationFlowList(BizQuotation bizQuotation) {
        List<BizQuotation> list = bizQuotationMapper.selectBizQuotationFlowList(bizQuotation);
        if (CollectionUtils.isNotEmpty(list)) {
            for (BizQuotation bizQuotation1 : list) {
                String string9 = bizQuotation1.getString9();
                String string20 = bizQuotation1.getString20();
                bizQuotation1.setString9(StringUtils.getDoubleString(string9));
                bizQuotation1.setString20(StringUtils.getDoubleString(string20));
            }
        }
        return list;
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizQuotation> selectBizQuotationByFlowStatus(BizQuotation bizQuotationTodo) {
        return bizQuotationMapper.selectBizQuotationByFlowStatus(bizQuotationTodo);
    }

    @Override
    public List<BizQuotation> selectBizQuotationProductList(BizQuotation bizQuotation) {
        List<BizQuotation> list = bizQuotationMapper.selectBizQuotationProductList(bizQuotation);
        return list;
    }


    /**
     * 新增报价单
     *
     * @param bizQuotation 报价单
     * @return 结果
     */
    @Override
    public int insertBizQuotation(BizQuotation bizQuotation) {
        bizQuotation.setCreateTime(DateUtils.getNowDate());
        bizQuotation.setCreateBy(ShiroUtils.getUserId().toString());
        int insertCount = bizQuotationMapper.insertBizQuotation(bizQuotation);

        return insertCount;
    }

    /**
     * 上报报价单
     * @param bizQuotation
     */
    public void insertFlow (BizQuotation bizQuotation) {
        BizFlow bizFlow = new BizFlow();
        bizFlow.setCreateTime(DateUtils.getNowDate());
        bizFlow.setCreateBy(ShiroUtils.getUserId().toString());
        bizFlow.setBizId(bizQuotation.getQuotationId());
        bizFlow.setBizTable(BizConstants.BIZ_QUOTATION_TABLE);
        bizFlow.setExamineUserId(ShiroUtils.getUserId());
        bizFlow.setFlowStatus(BizConstants.FLOW_STATUS_0);
        bizFlowMapper.insertBizFlow(bizFlow);
    }

    /**
     * 审批
     * @param quotationId
     * @param status
     * @param remark
     * @return
     */
    @Override
    public int doExamine(String quotationId,String status,String remark) {


        BizQuotation bizQuotation = bizQuotationMapper.selectBizQuotationById(Long.parseLong(quotationId));
        bizQuotation.setUpdateTime(DateUtils.getNowDate());
        bizQuotation.setUpdateBy(ShiroUtils.getUserId().toString());
        //1=同意 -1=不同意

        //1=销售 2=销售经理 3=区域经理 4=副总 5=总经理
        //流程状态0=未上报  1=销售员上报  2=销售经理同意 -2=销售经理不同意 3=区域经理同意 -3=区域经理不同意 4=副总同意 -4=副总不同意 5=老总同意 -5=老总不同意
        int roleType = roleService.getRoleType(ShiroUtils.getUserId());
        String flowStatus = "";
        if (roleType == 2) {
            if (status.equals("1")) {
                flowStatus = "2";
            } else {
                flowStatus = "-2";
            }
        } else if (roleType == 3) {
            if (status.equals("1")) {
                flowStatus = "3";
            } else {
                flowStatus = "-3";
            }
        } else if (roleType == 4) {
            if (status.equals("1")) {
                flowStatus = "4";
            } else {
                flowStatus = "-4";
            }
        } else if (roleType == 5) {
            if (status.equals("1")) {
                flowStatus = "5";
            } else {
                flowStatus = "-5";
            }
        }
        bizQuotation.setFlowStatus(flowStatus);
        int updateCount = bizQuotationMapper.updateBizQuotation(bizQuotation);
        BizFlow bizFlow = new BizFlow();
        bizFlow.setCreateTime(DateUtils.getNowDate());
        bizFlow.setCreateBy(ShiroUtils.getUserId().toString());
        bizFlow.setBizId(bizQuotation.getQuotationId());
        bizFlow.setBizTable(BizConstants.BIZ_QUOTATION_TABLE);
        bizFlow.setExamineUserId(ShiroUtils.getUserId());
        bizFlow.setRemark(remark);
        bizFlow.setFlowStatus(status);
        bizFlowMapper.insertBizFlow(bizFlow);

        //修改客户类别状态
        String customerId = bizQuotation.getString2();
        if (StringUtils.isNotEmpty(customerId)) {
            BizCustomer bizCustomer = bizCustomerMapper.selectBizCustomerById(StringUtils.toLong(customerId));
            if (bizCustomer != null) {
                String recordDate = bizCustomer.getRecordDate();
                if (StringUtils.isNotEmpty(recordDate)) {
                    String customerLevel = bizCustomer.getCustomerLevel();
                    //成单客户（新）	=1        成单客户（老）=2
                    Date recordDateD = DateUtils.parseDate(recordDate);
                    if (recordDateD != null) {
                        //如果是  未报价未成单
                        if ("3".equals(customerLevel)) {
                            bizCustomer.setCustomerLevel("4");
                            if (DateUtils.getDatePoorDay(new Date(),recordDateD) >= 365) {
                                bizCustomer.setCustomerLevel("2");
                            }
                            bizCustomerMapper.updateBizCustomer(bizCustomer);
                        }
                    }
                }
            }
        }


        return updateCount;
    }

    @Override
    public int subReportBizQuotation(BizQuotation bizQuotation) {
        bizQuotation.setUpdateTime(DateUtils.getNowDate());
        bizQuotation.setUpdateBy(ShiroUtils.getUserId().toString());
//        bizQuotation.setFlowStatus(BizConstants.FLOW_STATUS_1);
        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap("biz_quotation");
        String lastRoleKey = "";
        for (String key : flowMap.keySet()) {
            lastRoleKey = key;
        }
        bizQuotation.setFlowStatus(lastRoleKey);
        int updateCount = bizQuotationMapper.updateBizQuotation(bizQuotation);
        // 上报之前清理 所有的审批记录
        bizFlowMapper.deleteBizFlowByBizId(bizQuotation.getQuotationId());
        insertFlow(bizQuotation);
        return updateCount;
    }

    /**
     * 修改报价单
     *
     * @param bizQuotation 报价单
     * @return 结果
     */
    @Override
    public int updateBizQuotation(BizQuotation bizQuotation) {
        bizQuotation.setUpdateTime(DateUtils.getNowDate());
        bizQuotation.setUpdateBy(ShiroUtils.getUserId().toString());
        return bizQuotationMapper.updateBizQuotation(bizQuotation);
    }

    /**
     * 删除报价单对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizQuotationByIds(String ids) {
        return bizQuotationMapper.deleteBizQuotationByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除报价单信息
     *
     * @param quotationId 报价单ID
     * @return 结果
     */
    @Override
    public int deleteBizQuotationById(Long quotationId) {
        return bizQuotationMapper.deleteBizQuotationById(quotationId);
    }
}
