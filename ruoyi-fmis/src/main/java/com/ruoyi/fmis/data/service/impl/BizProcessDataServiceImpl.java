package com.ruoyi.fmis.data.service.impl;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.config.RedisUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.Constant;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.domain.ProcessDataDTO;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.BizContractLevel;
import com.ruoyi.fmis.data.domain.SaleListExportDTO;
import com.ruoyi.fmis.define.service.IBizProcessDefineService;
import com.ruoyi.fmis.finance.domain.BizPayPlan;
import com.ruoyi.fmis.finance.service.IBizPayPlanService;
import com.ruoyi.fmis.flow.domain.BizFlow;
import com.ruoyi.fmis.flow.mapper.BizFlowMapper;
import com.ruoyi.fmis.invoice.bean.InvoiceReqVo;
import com.ruoyi.fmis.invoice.bean.InvoiceRespVo;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.fmis.status.domain.BizDataStatus;
import com.ruoyi.fmis.status.service.IBizDataStatusService;
import com.ruoyi.fmis.stestn.domain.BizDataStestn;
import com.ruoyi.fmis.stestn.service.IBizDataStestnService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.data.mapper.BizProcessDataMapper;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 合同管理Service业务层处理
 *
 * @author frank
 * @date 2020-05-05
 */
@Service
public class BizProcessDataServiceImpl implements IBizProcessDataService {
    protected final Logger logger = LoggerFactory.getLogger(BizProcessDataServiceImpl.class);

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

    @Autowired
    private IBizDataStestnService bizDataStestnService;
    @Autowired
    private IBizDataStatusService bizDataStatusService;

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

    /**
     * 类别查询
     * 产品信息
     * 备选产品信息
     *
     * @param bizProcessData
     * @return
     */
    @Override
    public List<BizContractLevel> listLevelS(BizProcessData bizProcessData) {
        List<BizContractLevel> bizContractLevels = new ArrayList<>();
        bizProcessData.setLevel("1");
        String editFlag = bizProcessData.getBizEditFlag();
        if (!CollectionUtils.isEmpty(listLevelProductS(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("产品信息");
            bizContractLevel.setLevelType("11");
            bizContractLevel.setLevel("1");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        /*
        bizProcessData.setLevel("-2");
        if (!CollectionUtils.isEmpty(listLevelProductS(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("备选产品信息");
            bizContractLevel.setLevelType("12");
            bizContractLevel.setLevel("2");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }*/
        //执行器
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelActuator(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("执行器信息");
            bizContractLevel.setLevelType("21");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }

        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelRef1(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("法兰信息");
            bizContractLevel.setLevelType("31");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }

        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelRef2(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("螺栓信息");
            bizContractLevel.setLevelType("41");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        //定位器
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelPA(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("定位器信息");
            bizContractLevel.setLevelType("51");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }

        //电磁阀
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelPA1(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("电磁阀信息");
            bizContractLevel.setLevelType("61");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        //回信器数
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelPA2(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("回信器数信息");
            bizContractLevel.setLevelType("71");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }

        //气源三连件
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelPA3(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("气源三连件信息");
            bizContractLevel.setLevelType("81");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        //可离合减速器
        bizProcessData.setLevel("A");
        if (!CollectionUtils.isEmpty(listLevelPA4(bizProcessData).getRows())) {
            BizContractLevel bizContractLevel = new BizContractLevel();
            bizContractLevel.setLevelTypeName("可离合减速器信息");
            bizContractLevel.setLevelType("91");
            bizContractLevel.setLevel("A");
            bizContractLevel.setDataId(bizProcessData.getDataId().toString());
            bizContractLevels.add(bizContractLevel);
        }
        return bizContractLevels;
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

    /**
     * 查询产品信息
     *
     * @param bizProcessData
     * @return
     */
    @Override
    public TableDataInfo listLevelProductS(BizProcessData bizProcessData) {
        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(bizProcessData.getDataId());
        queryBizProcessChild.setString2(bizProcessData.getLevel());
        queryBizProcessChild.setDataStatus(bizProcessData.getDataStatus());
        queryBizProcessChild.setBizEditFlag(bizProcessData.getBizEditFlag());
        queryBizProcessChild.setProcurementId(bizProcessData.getProcurementId());

        queryBizProcessChild.setLevelValue(bizProcessData.getLevel());

        String supplierId = bizProcessData.getSupplierId();
        if (StringUtils.isNotEmpty(supplierId)) {
            queryBizProcessChild.setSupplierId(supplierId);
        }
        List<BizProcessChild> bizProcessChildList;
        if (bizProcessData.getProcurementId() != null && !bizProcessData.getProcurementId().equals("")) {
             bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);
        } else {
            if (bizProcessData.getDataStatus() !=null &&bizProcessData.getDataStatus().equals("2")) {
                bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);
            } else {
                bizProcessChildList = bizProcessChildService.selectBizChildProductListCaigou(queryBizProcessChild);
            }

        }




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

//        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);
        List<BizProcessChild> bizProcessChildList;
        if (bizProcessData.getProcurementId() != null && !bizProcessData.getProcurementId().equals("")) {
            bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);
        } else {
            if (bizProcessData.getDataStatus() !=null &&bizProcessData.getDataStatus().equals("2")) {
                bizProcessChildList = bizProcessChildService.selectBizChildProductList(queryBizProcessChild);
            } else {
                bizProcessChildList = bizProcessChildService.selectBizChildProductListCaigou(queryBizProcessChild);
            }
        }

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
    public TableDataInfo listLevelProductCaigou(BizProcessData bizProcessData) {
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

        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizChildProductListCaigou(queryBizProcessChild);


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
    public List<BizProcessData> selectBizProcessDataListCg(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListCg(bizProcessData);
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataListRef(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRef(bizProcessData);
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataListRefLiu(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRefLiu(bizProcessData);
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataListXs(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListXs(bizProcessData);
    }

    @Override
    public List<BizProcessData> selectBizProcessDataListRefBill(BizProcessData bizProcessData) {
        // 如果已付金额=合同金额，则不显示
        List<BizProcessData> list = bizProcessDataMapper.selectBizProcessDataListRefBill(bizProcessData);
        list = list.stream().filter(e -> !e.getBillAmount().equals(e.getPrice1())).collect(Collectors.toList());
        return list;
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataListRefDelivery(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRefDelivery(bizProcessData);
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataListRefCPayment(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRefCPayment(bizProcessData);
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataListRefInvoice(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRefInvoice(bizProcessData);
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<InvoiceRespVo> selectBizProcessChildListRefInvoice(InvoiceReqVo reqVo) {
        return bizProcessDataMapper.selectBizProcessChildListRefInvoice(reqVo);
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataListRefPayment(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRefPayment(bizProcessData);
    }

    @Override
    public BizProcessData selectBizProcessDataPaymentById(Long dataId) {
        return bizProcessDataMapper.selectBizProcessDataPaymentById(dataId);
    }

    @Override
    public List<BizProcessData> selectBizProcessDataListRefProcurement(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRefProcurement(bizProcessData);
    }

    @Override
    public List<BizProcessData> selectBizProcessDataListRefTrack(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataListRefTrack(bizProcessData);
    }


    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataVoRefBorrowing(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataVoRefBorrowing(bizProcessData);
    }

    @Override
    @DataScope(deptAlias = "dt", userAlias = "u")
    public List<BizProcessData> selectBizProcessDataForTodo(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectBizProcessDataByFlowStatus(bizProcessData);
    }

    @Override
    public List<SaleListExportDTO> selectSaleListExport(Long dataId) {
        List<SaleListExportDTO> saleListExportDTOS = bizProcessDataMapper.selectSaleListExport(dataId);
        AtomicInteger i= new AtomicInteger(1);
        saleListExportDTOS.forEach(e->e.setId(i.getAndIncrement()));
        return saleListExportDTOS;
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
        String currentUserFlowStatus = "";
        if (!CollectionUtils.isEmpty(flowMap)) {
            if (flowMap.size() > 1) {
                currentUserFlowStatus = flowMap.keySet().stream().filter(e -> Integer.parseInt(e) > Integer.parseInt(bizProcessData.getFlowStatus())).findFirst().orElse("");
            } else {
                currentUserFlowStatus = flowMap.keySet().iterator().next();
            }

            if (status.equals("1")) {
                flowStatus = currentUserFlowStatus;
            } else {
                flowStatus = "-" + currentUserFlowStatus;
            }
        }
        if (flowStatus.equals("-")) {
            flowStatus = "0";
        }
        bizProcessData.setFlowStatus(flowStatus);

        /**
         * 采购管理 如果已完成 采购状态修改为 采购中
         */
        if (BizConstants.BIZ_procurement.equals(bizProcessData.getBizId())) {
            if (bizProcessData.getFlowStatus().equals(bizProcessData.getNormalFlag())) {
                bizProcessData.setStatus(Constant.procurementStatus.ING);
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
        bizFlow.setString1(currentUserFlowStatus);
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
        //更新采购池状态
        String[] bizIds = ids.split(",");
        for (String id : bizIds) {
            BizProcessData bizProcessData = bizProcessDataMapper.selectBizProcessDataById(Long.parseLong(id));
            if (bizProcessData.getBizId().equals("procurement")) {
                BizProcessData bizProcessDataxs = new BizProcessData();
                bizProcessDataxs.setString1(bizProcessData.getString3().split(",")[0]);
                bizProcessDataxs.setBizId("contract");
                List<BizProcessData> bizProcessData1 = bizProcessDataMapper.selectBizProcessDataList(bizProcessDataxs);
                for (BizProcessData bizProcessData2 : bizProcessData1) {
                    bizProcessData2.setString30("1");
                    bizProcessDataMapper.updateBizProcessData(bizProcessData2);
                }
            }
        }
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
        // 上报之前清理 所有的审批记录
        bizFlowMapper.deleteBizFlowByBizId(bizProcessData.getDataId());
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
        // 上报之前清理 所有的审批记录
        bizFlowMapper.deleteBizFlowByBizId(bizProcessData.getDataId());
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
        bizFlow.setString1(bizProcessData.getFlowStatus());
        bizFlowMapper.insertBizFlow(bizFlow);
    }

    @Override
    public Long selectProcurementMaxNo() {
        return bizProcessDataMapper.selectProcurementMaxNo();
    }


    /**
     * 检验完成，更新采购合同状态和销售合同状态
     * 1.当采购合同的产品已全部合格入库，其状态为：采购完成
     * 2.当这个销售合同的数量与入库数量相等时：已入库
     *
     * @param dataId 采购合同Id
     */
    @Override
    public void testCompleteUpdateStatus(String dataId) {
        // 判断检验合格数是否等于采购订单的数量，如果等于，将采购合同的状态更改为采购完成
        List<BizDataStestn> stestnList = bizDataStestnService.selectBizDataStestnList(new BizDataStestn() {{
            setString4(dataId);
        }});
        // 该采购合同已经检验合格的数量
        int yesSum = stestnList.stream().mapToInt(a -> Integer.parseInt(a.getString1())).sum();
        List<BizDataStatus> statusList = bizDataStatusService.selectBizDataStatusList(new BizDataStatus() {{
            setString4(dataId);
        }});
        // 该采购合同的数量
        int totalSum = statusList.stream().mapToInt(b -> Integer.parseInt(b.getString1())).sum();
        logger.info("采购合同检验保存，状态变更，yesSum:{}，totalSum:{},dataId:{}", yesSum, totalSum, dataId);
        if (totalSum <= yesSum) {
            updateBizProcessData(new BizProcessData() {{
                setDataId(Long.valueOf(dataId));
                setStatus(Constant.procurementStatus.DONE);
            }});
        }

        // 销售合同状态变更
        BizDataStestn bizDataStestn = stestnList.get(0);
        String string5 = bizDataStestn.getString5();
        BizProcessChild bizProcessChild = bizProcessChildService.selectBizProcessChildById(Long.valueOf(string5));
        if (bizProcessChild != null) {
            List<BizProcessChild> bizProcessChildrenList = bizProcessChildService.selectBizProcessChildList(new BizProcessChild() {{
                setDataId(bizProcessChild.getDataId());
            }});
            int totalSum1 = 0;
            List<String> childList = new ArrayList<>();
            for (BizProcessChild processChild : bizProcessChildrenList) {
                totalSum1 += Integer.valueOf(processChild.getString3());
                childList.add(String.valueOf(processChild.getChildId()));
            }
            List<BizDataStestn> stestnList1 = bizDataStestnService.selectBizDataStestnList(new BizDataStestn() {{
                setString5List(childList);
            }});
            int yesSum1 = stestnList1.stream().mapToInt(a -> Integer.parseInt(a.getString1())).sum();
            logger.info("销售合同检验保存，状态变更，yesSum:{}，totalSum:{},dataId:{}", yesSum1, totalSum1, bizProcessChild.getDataId());
            if (totalSum1 <= yesSum1) {
                updateBizProcessData(new BizProcessData() {{
                    setDataId(bizProcessChild.getDataId());
                    setStatus(Constant.contractStatus.STORED);
                }});
            }
        }
    }

    @Override
    public void deliveryUpdateStatus(String contractNo) {
        // 根据销售合同号，查询该合同号已经发货完成的数量
        Integer deliveryQty = bizProcessChildService.getDeliveryQtyByContractNo(contractNo);
        ProcessDataDTO processDataDTO = bizProcessChildService.getSaleQtyByContractNo(contractNo);
        logger.info("deliveryUpdateStatus contactNo:{},deliverQty:{},saleQty:{},dataId:{}", contractNo, deliveryQty, processDataDTO.getSaleQty(), processDataDTO.getDataId());
        BizProcessData update = new BizProcessData();
        update.setStatus(Constant.contractStatus.PART_DELIVERY);
        if (deliveryQty != null &&processDataDTO.getSaleQty() <= deliveryQty) {
            update.setStatus(Constant.contractStatus.ALL_DELIVERY);
        }
        update.setDataId(processDataDTO.getDataId());
        updateBizProcessData(update);
    }

    /**
     * 添加付款计划（付款申请流程审批完成）
     */
    private void addPayPlan(BizProcessData bizProcessData) {
        Long dataId = bizProcessData.getDataId();

        BizProcessChild queryBizProcessChild = new BizProcessChild();
        queryBizProcessChild.setDataId(dataId);
        List<BizProcessChild> bizProcessChildList = bizProcessChildService.selectBizProcessChildList(queryBizProcessChild);
        if (!CollectionUtils.isEmpty(bizProcessChildList)) {
            for (BizProcessChild child : bizProcessChildList) {
                BizPayPlan bizPayPlan = new BizPayPlan();
                bizPayPlan.setPayDataId(dataId);
                bizPayPlan.setApplyPayCompany(child.getString3());
                bizPayPlan.setApplyCollectionCompany(child.getString4());
                bizPayPlan.setApplyRemark(child.getRemark());
                bizPayPlan.setApplyAmount(child.getPrice1());
                bizPayPlan.setPaymentType("2");// 货款
                bizPayPlan.setDataStatus("1");// 待总经理确认
                bizPayPlan.setApplyDate(bizProcessData.getDatetime1());
                bizPayPlan.setContractNo(child.getString2());
                bizPayPlan.setContractId(child.getString1());
                bizPayPlan.setContractPayWay(child.getString5());
                bizPayPlan.setApplyNo("PP" + DateUtils.dateTimeNow() + RandomStringUtils.randomNumeric(3));
                bizPayPlan.setCreateTime(DateUtils.getNowDate());
                bizPayPlan.setCreateBy(ShiroUtils.getUserId().toString());
                bizPayPlanService.insertBizPayPlan(bizPayPlan);
            }
        }
    }

    @Transactional
    @Override
    public int deleteNewDeliveryById(String dataId) {
        // 查询发货的childId
        BizProcessChild bizProcessChild = new BizProcessChild();
        bizProcessChild.setDataId(Long.parseLong(dataId));
        List<BizProcessChild> bizProcessChildren = bizProcessChildService.selectBizProcessChildInventoryList(bizProcessChild);
        if(Objects.isNull(bizProcessChildren)) {
            bizProcessChildren = new ArrayList<>();
        }
        // 变更原始数据状态
        for (BizProcessChild processChild : bizProcessChildren) {
            String originalChildId = processChild.getString15();
            BizProcessChild bizProcessChild1 = bizProcessChildService.selectBizProcessChildById(Long.parseLong(originalChildId));
            bizProcessChild1.setString20("0");// 1 代表已经操作，0 代表未操作
            bizProcessChildService.updateBizProcessChild(bizProcessChild1);
            // 删除 发货子项
            bizProcessChildService.deleteBizProcessChildById(processChild.getChildId());
        }
        // 删除发货单
        bizProcessDataMapper.deleteBizProcessDataById(Long.parseLong(dataId));
        return 1;
    }

    @Override
    public List<BizProcessData> selectAllBorrowingWithNoPayAndAgree(Long userId) {
        return bizProcessDataMapper.selectAllBorrowingWithNoPayAndAgree(userId);
    }

    @Override
    public int updateBizProcessDataByBizIdAndString12(BizProcessData bizProcessData) {
        bizProcessData.setUpdateTime(DateUtils.getNowDate());
        return bizProcessDataMapper.updateBizProcessDataByBizIdAndString12(bizProcessData);
    }

    @Override
    public List<BizProcessData> selectLastBizProcessDataListRef(BizProcessData bizProcessData) {
        return bizProcessDataMapper.selectLastBizProcessDataListRef(bizProcessData);
    }
}
