package com.ruoyi.fmis.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.config.RedisUtil;
import com.ruoyi.common.constant.GenConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.BizContractLevel;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.finance.domain.BizPayPlan;
import com.ruoyi.fmis.finance.service.IBizPayPlanService;
import com.ruoyi.fmis.flow.domain.BizFlow;
import com.ruoyi.fmis.flow.mapper.BizFlowMapper;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.fmis.quotation.domain.BizQuotation;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.data.mapper.BizProcessDataMapper;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 合同管理Service业务层处理
 *
 * @author frank
 * @date 2020-05-05
 */
@Service
public class BizProcessDataServiceImpl implements IBizProcessDataService {

    @Autowired
    private BizProcessDataMapper bizProcessDataMapper;

    @Autowired
    private BizFlowMapper bizFlowMapper;

    @Autowired
    private IBizProcessDefineService bizProcessDefineService;

    @Autowired
    private IBizProcessChildService bizProcessChildService;
    @Autowired
    private IBizPayPlanService bizPayPlanService;

    /**
     * 查询合同管理
     *
     * @param dataId 合同管理ID
     * @return 合同管理
     */
    @Override
    public BizProcessData selectBizProcessDataById(Long dataId) {
        return bizProcessDataMapper.selectBizProcessDataById(dataId);
    }

    @Override
    public BizProcessData selectBizProcessDataBorrowingById(Long dataId) {
        return bizProcessDataMapper.selectBizProcessDataBorrowingById(dataId);
    }


    @Override
    public List<BizContractLevel> listLevel(BizProcessData bizProcessData) {
        List<BizContractLevel> bizContractLevels = new ArrayList<>();
        bizProcessData.setLevel("1");
        String editFlag = bizProcessData.getBizEditFlag();
        if (!CollectionUtils.isEmpty(listLevelProduct(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("产品信息A");
            bizContractLevel.setLevelType("11");
            bizContractLevel.setLevel("1");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("2");
        if (!CollectionUtils.isEmpty(listLevelProduct(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("产品信息B");
            bizContractLevel.setLevelType("12");
            bizContractLevel.setLevel("2");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("3");
        if (!CollectionUtils.isEmpty(listLevelProduct(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("产品信息C");
            bizContractLevel.setLevelType("13");
            bizContractLevel.setLevel("3");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }

        //执行器
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelActuator(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("执行器信息A");
            bizContractLevel.setLevelType("21");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("B");
        if (!CollectionUtils.isEmpty(listLevelActuator(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("执行器信息B");
            bizContractLevel.setLevelType("22");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("C");
        if (!CollectionUtils.isEmpty(listLevelActuator(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("执行器信息C");
            bizContractLevel.setLevelType("23");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelRef1(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("法兰信息A");
            bizContractLevel.setLevelType("31");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("B");
        if (!CollectionUtils.isEmpty(listLevelRef1(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("法兰信息B");
            bizContractLevel.setLevelType("32");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("C");
        if (!CollectionUtils.isEmpty(listLevelRef1(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("法兰信息C");
            bizContractLevel.setLevelType("33");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelRef2(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("螺栓信息A");
            bizContractLevel.setLevelType("41");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("B");
        if (!CollectionUtils.isEmpty(listLevelRef2(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("螺栓信息B");
            bizContractLevel.setLevelType("42");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("C");
        if (!CollectionUtils.isEmpty(listLevelRef2(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("螺栓信息C");
            bizContractLevel.setLevelType("43");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }

        //定位器
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelPA(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("定位器信息A");
            bizContractLevel.setLevelType("51");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("B");
        if (!CollectionUtils.isEmpty(listLevelPA(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("定位器信息B");
            bizContractLevel.setLevelType("52");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("C");
        if (!CollectionUtils.isEmpty(listLevelPA(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("定位器信息C");
            bizContractLevel.setLevelType("53");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }

        //电磁阀
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelPA1(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("电磁阀信息A");
            bizContractLevel.setLevelType("61");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("B");
        if (!CollectionUtils.isEmpty(listLevelPA1(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("电磁阀信息B");
            bizContractLevel.setLevelType("62");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("C");
        if (!CollectionUtils.isEmpty(listLevelPA1(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("电磁阀信息C");
            bizContractLevel.setLevelType("63");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }

        //回信器数
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelPA2(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("回信器数信息A");
            bizContractLevel.setLevelType("71");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("B");
        if (!CollectionUtils.isEmpty(listLevelPA2(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("回信器数信息B");
            bizContractLevel.setLevelType("72");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("C");
        if (!CollectionUtils.isEmpty(listLevelPA2(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("回信器数信息C");
            bizContractLevel.setLevelType("73");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }

        //气源三连件
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelPA3(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("气源三连件信息A");
            bizContractLevel.setLevelType("81");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("B");
        if (!CollectionUtils.isEmpty(listLevelPA3(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("气源三连件信息B");
            bizContractLevel.setLevelType("82");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("C");
        if (!CollectionUtils.isEmpty(listLevelPA3(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("气源三连件信息C");
            bizContractLevel.setLevelType("83");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }

        //可离合减速器
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelPA4(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("可离合减速器信息A");
            bizContractLevel.setLevelType("91");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("B");
        if (!CollectionUtils.isEmpty(listLevelPA4(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("可离合减速器信息B");
            bizContractLevel.setLevelType("92");
            bizContractLevel.setLevel("B");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        bizProcessData.setLevel("C");
        if (!CollectionUtils.isEmpty(listLevelPA4(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("可离合减速器信息C");
            bizContractLevel.setLevelType("93");
            bizContractLevel.setLevel("C");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        return bizContractLevels;
    }

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBizProductService bizProductService;

    @Override
    public TableDataInfo listLevelProduct(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setString2(bizProcessData.getLevel());
        queryBizProcessChild.setDataStatus(bizProcessData.getDataStatus());
        queryBizProcessChild.setBizEditFlag(bizProcessData.getBizEditFlag());
        queryBizProcessChild.setProcurementId(bizProcessData.getProcurementId());

        String supplierId = bizProcessData.getSupplierId();
        if (StringUtils.isNotEmpty(supplierId)) {
            queryBizProcessChild.setSupplierId(supplierId);
        }

        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);


        String pSessionId = bizProcessData.getPSessionId();

        if (!CollectionUtils.isEmpty(bizProcessChildList)) {
            Map<String, String> productNumMap = new HashMap<>();
            for (BizProcessChild bizProcessChild : bizProcessChildList) {
                String productNum = bizProcessChild.getProductNum();
                String model = bizProcessChild.getModel();
                String specifications = bizProcessChild.getSpecifications();
                String k = model + "_" + specifications;
                if (!productNumMap.containsKey(k)) {
                    productNumMap.put(k, productNum);
                } else {
                    productNum = productNumMap.get(k);
                }
                bizProcessChild.setProductNum(productNum);

                if (StringUtils.isNotEmpty(pSessionId)) {
                    Object newProductIdObj = redisUtil.get(pSessionId + "_" + bizProcessChild.getProductId());
                    if (newProductIdObj != null) {
                        String newProductId = newProductIdObj.toString();
                        //替换新的
                        BizProduct queryBizProduct = new BizProduct();
                        queryBizProduct.setProductId(Long.parseLong(newProductId));
                        List<BizProduct> bizProductList = bizProductService.selectBizProductList(queryBizProduct);
                        if (!CollectionUtils.isEmpty(bizProductList)) {
                            BizProduct newBizProduct = bizProductList.get(0);
                            bizProcessChild.setProductName(newBizProduct.getName());
                            bizProcessChild.setNewProductId(newBizProduct.getProductId().toString());
                            bizProcessChild.setModel(newBizProduct.getModel());
                            bizProcessChild.setSupplier(newBizProduct.getSupplier());
                            bizProcessChild.setSpecifications(newBizProduct.getSpecifications());
                            bizProcessChild.setNominalPressure(newBizProduct.getNominalPressure());
                            bizProcessChild.setValvebodyMaterial(newBizProduct.getValvebodyMaterial());
                            bizProcessChild.setValveElement(newBizProduct.getValveElement());
                            bizProcessChild.setDriveForm(newBizProduct.getDriveForm());
                            bizProcessChild.setConnectionType(newBizProduct.getConnectionType());
                        }
                    }
                }

            }
        }
        return BaseController.getDataTableImpl(bizProcessChildList);
    }

    @Override
    public TableDataInfo listLevelActuator(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setLevelLabel(bizProcessData.getLevel());
        queryBizProcessChild.setDataStatus(bizProcessData.getDataStatus());
        queryBizProcessChild.setBizEditFlag(bizProcessData.getBizEditFlag());
        queryBizProcessChild.setProcurementId(bizProcessData.getProcurementId());
        String supplierId = bizProcessData.getSupplierId();
        if (StringUtils.isNotEmpty(supplierId)) {
            queryBizProcessChild.setSupplierId(supplierId);
        }
        List<BizProcessChild> bizProcessChildListActuatorA = bizProcessChildService.selectBizChildActuatorList(queryBizProcessChild);
        return BaseController.getDataTableImpl(bizProcessChildListActuatorA);
    }

    @Override
    public TableDataInfo listLevelRef1(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setLevelLabel(bizProcessData.getLevel());
        queryBizProcessChild.setDataStatus(bizProcessData.getDataStatus());
        queryBizProcessChild.setBizEditFlag(bizProcessData.getBizEditFlag());
        queryBizProcessChild.setProcurementId(bizProcessData.getProcurementId());
        String supplierId = bizProcessData.getSupplierId();
        if (StringUtils.isNotEmpty(supplierId)) {
            queryBizProcessChild.setSupplierId(supplierId);
        }
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildRef1List(queryBizProcessChild);
        return BaseController.getDataTableImpl(bizProcessChildListRefA);
    }

    @Override
    public TableDataInfo listLevelRef2(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setLevelLabel(bizProcessData.getLevel());
        queryBizProcessChild.setDataStatus(bizProcessData.getDataStatus());
        queryBizProcessChild.setBizEditFlag(bizProcessData.getBizEditFlag());
        queryBizProcessChild.setProcurementId(bizProcessData.getProcurementId());
        String supplierId = bizProcessData.getSupplierId();
        if (StringUtils.isNotEmpty(supplierId)) {
            queryBizProcessChild.setSupplierId(supplierId);
        }
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildRef2List(queryBizProcessChild);
        return BaseController.getDataTableImpl(bizProcessChildListRefA);
    }

    @Override
    public TableDataInfo listLevelPA(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setLevelLabel(bizProcessData.getLevel());
        queryBizProcessChild.setDataStatus(bizProcessData.getDataStatus());
        queryBizProcessChild.setBizEditFlag(bizProcessData.getBizEditFlag());
        queryBizProcessChild.setProcurementId(bizProcessData.getProcurementId());
        String supplierId = bizProcessData.getSupplierId();
        if (StringUtils.isNotEmpty(supplierId)) {
            queryBizProcessChild.setSupplierId(supplierId);
        }
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildPAList(queryBizProcessChild);
        return BaseController.getDataTableImpl(bizProcessChildListRefA);
    }

    @Override
    public TableDataInfo listLevelPA1(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setLevelLabel(bizProcessData.getLevel());
        queryBizProcessChild.setDataStatus(bizProcessData.getDataStatus());
        queryBizProcessChild.setBizEditFlag(bizProcessData.getBizEditFlag());
        queryBizProcessChild.setProcurementId(bizProcessData.getProcurementId());
        String supplierId = bizProcessData.getSupplierId();
        if (StringUtils.isNotEmpty(supplierId)) {
            queryBizProcessChild.setSupplierId(supplierId);
        }
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildPA1List(queryBizProcessChild);
        return BaseController.getDataTableImpl(bizProcessChildListRefA);
    }

    @Override
    public TableDataInfo listLevelPA2(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setLevelLabel(bizProcessData.getLevel());
        queryBizProcessChild.setDataStatus(bizProcessData.getDataStatus());
        queryBizProcessChild.setBizEditFlag(bizProcessData.getBizEditFlag());
        queryBizProcessChild.setProcurementId(bizProcessData.getProcurementId());
        String supplierId = bizProcessData.getSupplierId();
        if (StringUtils.isNotEmpty(supplierId)) {
            queryBizProcessChild.setSupplierId(supplierId);
        }
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildPA2List(queryBizProcessChild);
        return BaseController.getDataTableImpl(bizProcessChildListRefA);
    }

    @Override
    public TableDataInfo listLevelPA3(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setLevelLabel(bizProcessData.getLevel());
        queryBizProcessChild.setDataStatus(bizProcessData.getDataStatus());
        queryBizProcessChild.setBizEditFlag(bizProcessData.getBizEditFlag());
        queryBizProcessChild.setProcurementId(bizProcessData.getProcurementId());
        String supplierId = bizProcessData.getSupplierId();
        if (StringUtils.isNotEmpty(supplierId)) {
            queryBizProcessChild.setSupplierId(supplierId);
        }
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildPA3List(queryBizProcessChild);
        return BaseController.getDataTableImpl(bizProcessChildListRefA);
    }

    @Override
    public TableDataInfo listLevelPA4(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setLevelLabel(bizProcessData.getLevel());
        queryBizProcessChild.setDataStatus(bizProcessData.getDataStatus());
        queryBizProcessChild.setBizEditFlag(bizProcessData.getBizEditFlag());
        queryBizProcessChild.setProcurementId(bizProcessData.getProcurementId());
        String supplierId = bizProcessData.getSupplierId();
        if (StringUtils.isNotEmpty(supplierId)) {
            queryBizProcessChild.setSupplierId(supplierId);
        }
        List<BizProcessChild> bizProcessChildListRefA = bizProcessChildService.selectBizChildPA4List(queryBizProcessChild);
        return BaseController.getDataTableImpl(bizProcessChildListRefA);
    }

    /**
     * 查询合同管理列表
     *
     * @param bizProcessData 合同管理
     * @return 合同管理
     */
    @Override
    public List<BizProcessData> selectBizProcessDataList(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataList(bizProcessData);
    }


    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataListRef(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRef(bizProcessData);
    }


    @Override
    public List<BizProcessData> selectBizProcessDataListRefBill(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRefBill(bizProcessData);
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataListRefDelivery(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRefDelivery(bizProcessData);
    }


    @Override
    public List<BizProcessData> selectBizProcessDataListRefProcurement(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRefProcurement(bizProcessData);
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataVoRefBorrowing(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataVoRefBorrowing(bizProcessData);
    }

    /**
     * 审批
     *
     * @param dataId
     * @param status
     * @param remark
     * @return
     */
    @Override
    public int doExamine(String dataId, String status, String remark, String bizId) {

        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizId);
        //Map<String, SysRole> flowAllMap = bizProcessDefineService.getFlowAllMap(bizId);

        BizProcessData bizProcessData = bizProcessDataMapper.selectBizProcessDataById(Long.parseLong(dataId));
        bizProcessData.setUpdateTime(DateUtils.getNowDate());
        bizProcessData.setUpdateBy(ShiroUtils.getUserId().toString());
        //1=同意 -1=不同意

        //1=销售 2=销售经理 3=区域经理 4=副总 5=总经理
        //流程状态0=未上报  1=销售员上报  2=销售经理同意 -2=销售经理不同意 3=区域经理同意 -3=区域经理不同意 4=副总同意 -4=副总不同意 5=老总同意 -5=老总不同意
        //int roleType = roleService.getRoleType(ShiroUtils.getUserId());

        String flowStatus = "";
        if (!CollectionUtils.isEmpty(flowMap)) {
            String currentUserFlowStatus = flowMap.keySet().iterator().next();
            if (status.equals("1")) {
                flowStatus = currentUserFlowStatus;
            } else {
                flowStatus = "-" + currentUserFlowStatus;
            }
        }
        bizProcessData.setFlowStatus(flowStatus);

        /**
         * 采购管理 如果已完成 采购状态修改为 采购中
         */
        if (BizConstants.BIZ_procurement.equals(bizProcessData.getBizId())) {
            if (bizProcessData.getFlowStatus().equals(bizProcessData.getNormalFlag())) {
                bizProcessData.setString11("3");
            }
        }

        /**
         * hedong 付款申请，当审批完成，往付款计划表中插入一条数据
         */
        if (BizConstants.BIZ_cpayment.equals(bizProcessData.getBizId())
                && bizProcessData.getFlowStatus().equals(bizProcessData.getNormalFlag())) {
            this.addPayPlan(bizProcessData);
        }


        int updateCount = bizProcessDataMapper.updateBizProcessData(bizProcessData);
        BizFlow bizFlow = new BizFlow();
        bizFlow.setCreateTime(DateUtils.getNowDate());
        bizFlow.setCreateBy(ShiroUtils.getUserId().toString());
        bizFlow.setBizId(bizProcessData.getDataId());
        bizFlow.setBizTable(bizId);
        bizFlow.setExamineUserId(ShiroUtils.getUserId());
        bizFlow.setRemark(remark);
        bizFlow.setFlowStatus(status);
        bizFlowMapper.insertBizFlow(bizFlow);

        return updateCount;
    }


    /**
     * 新增合同管理
     *
     * @param bizProcessData 合同管理
     * @return 结果
     */
    @Override
    public int insertBizProcessData(BizProcessData bizProcessData) {
        bizProcessData.setCreateTime(DateUtils.getNowDate());
        bizProcessData.setCreateBy(ShiroUtils.getUserId().toString());
        return bizProcessDataMapper.insertBizProcessData(bizProcessData);
    }

    /**
     * 修改合同管理
     *
     * @param bizProcessData 合同管理
     * @return 结果
     */
    @Override
    public int updateBizProcessData(BizProcessData bizProcessData) {
        bizProcessData.setUpdateTime(DateUtils.getNowDate());
        return bizProcessDataMapper.updateBizProcessData(bizProcessData);
    }

    /**
     * 删除合同管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizProcessDataByIds(String ids) {
        return bizProcessDataMapper.deleteBizProcessDataByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除合同管理信息
     *
     * @param dataId 合同管理ID
     * @return 结果
     */
    @Override
    public int deleteBizProcessDataById(Long dataId) {
        return bizProcessDataMapper.deleteBizProcessDataById(dataId);
    }

    @Override
    public int subReportBizQuotation(BizProcessData bizProcessData) {
        bizProcessData.setUpdateTime(DateUtils.getNowDate());
        bizProcessData.setUpdateBy(ShiroUtils.getUserId().toString());
        bizProcessData.setFlowStatus(BizConstants.FLOW_STATUS_1);
        int updateCount = bizProcessDataMapper.updateBizProcessData(bizProcessData);
        insertFlow(bizProcessData);
        return updateCount;
    }

    @Override
    public int subReportBizQuotationBorrowing(BizProcessData bizProcessData) {
        bizProcessData.setUpdateTime(DateUtils.getNowDate());
        bizProcessData.setUpdateBy(ShiroUtils.getUserId().toString());


        Map<String, SysRole> flowMap = bizProcessDefineService.getRoleFlowMap(bizProcessData.getBizId());
        String lastRoleKey = "";
        for (String key : flowMap.keySet()) {
            lastRoleKey = key;
        }
        bizProcessData.setFlowStatus(lastRoleKey);
        int updateCount = bizProcessDataMapper.updateBizProcessData(bizProcessData);
        insertFlow(bizProcessData);
        return updateCount;
    }

    @Override
    public int subTestBizQuotation(BizProcessData bizProcessData) {
        bizProcessData.setUpdateTime(DateUtils.getNowDate());
        bizProcessData.setUpdateBy(ShiroUtils.getUserId().toString());
        bizProcessData.setString10("1");
        int updateCount = bizProcessDataMapper.updateBizProcessData(bizProcessData);
        return updateCount;
    }

    /**
     * 上报报价单
     */
    public void insertFlow(BizProcessData bizProcessData) {
        BizFlow bizFlow = new BizFlow();
        bizFlow.setCreateTime(DateUtils.getNowDate());
        bizFlow.setCreateBy(ShiroUtils.getUserId().toString());
        bizFlow.setBizId(bizProcessData.getDataId());
        bizFlow.setBizTable(bizProcessData.getBizId());
        bizFlow.setExamineUserId(ShiroUtils.getUserId());
        bizFlow.setFlowStatus(BizConstants.FLOW_STATUS_0);
        bizFlowMapper.insertBizFlow(bizFlow);
    }

    @Override
    public Long selectProcurementMaxNo() {
        return bizProcessDataMapper.selectProcurementMaxNo();
    }

    /**
     * 添加付款计划（付款申请流程审批完成）
     */
    private void addPayPlan(BizProcessData bizProcessData) {
        BizPayPlan bizPayPlan = new BizPayPlan();
        bizPayPlan.setPayDataId(bizProcessData.getDataId());
        bizPayPlan.setApplyPayCompany(bizProcessData.getString6());
        bizPayPlan.setApplyCollectionCompany(bizProcessData.getString1());
        bizPayPlan.setApplyRemark(bizProcessData.getRemark());
        bizPayPlan.setApplyAmount(bizProcessData.getPrice2());
        bizPayPlan.setApplyDate(bizProcessData.getDatetime1());
        bizPayPlan.setContractNo(bizProcessData.getString5());
        bizPayPlan.setApplyNo("PP" + DateUtils.dateTimeNow() + RandomStringUtils.randomNumeric(3));
        bizPayPlan.setCreateTime(DateUtils.getNowDate());
        bizPayPlan.setCreateBy(ShiroUtils.getUserId().toString());
        bizPayPlanService.insertBizPayPlan(bizPayPlan);
    }
}
