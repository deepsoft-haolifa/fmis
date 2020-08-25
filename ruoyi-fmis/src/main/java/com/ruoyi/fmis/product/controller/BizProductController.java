package com.ruoyi.fmis.product.controller;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelProduct;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.common.BizProductExcel;
import com.ruoyi.fmis.common.BizProductImport;
import com.ruoyi.fmis.dict.domain.BizDict;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 业务产品Controller
 *
 * @author Xianlu Tech
 * @date 2020-02-26
 */
@Controller
@RequestMapping("/fmis/product")
public class BizProductController extends BaseController {
    private String prefix = "fmis/product";

    @Autowired
    private IBizProductService bizProductService;

    @Autowired
    private IBizDictService bizDictService;

    @Autowired
    private IBizSuppliersService bizSuppliersService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    @RequiresPermissions("fmis:product:view")
    @GetMapping()
    public String product(ModelMap mmap) {
        mmap.put("seriesSelect",bizDictService.selectBizDictByProductType(BizConstants.productTypeCode));
        mmap.put("suppliers",bizSuppliersService.selectAllList());
        return prefix + "/product";
    }

    /**
     * 查询业务产品列表
     */
    @RequiresPermissions("fmis:product:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizProduct bizProduct) {
        startPage();
        List<BizProduct> list = bizProductService.selectBizProductList(bizProduct);
        return getDataTable(list);
    }

    @PostMapping("/listNoAuth")
    @ResponseBody
    public TableDataInfo listNoAuth(BizProduct bizProduct) {
        startPage();
        List<BizProduct> list = bizProductService.selectBizProductListNoAuth(bizProduct);
        return getDataTable(list);
    }

    /**
     * 导出业务产品列表
     */
    @RequiresPermissions("fmis:product:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizProduct bizProduct) {
        List<BizProduct> list = bizProductService.selectBizProductList(bizProduct);
        ExcelUtil<BizProduct> util = new ExcelUtil<BizProduct>(BizProduct.class);
        return util.exportExcel(list, "product");
    }

    /**
     * 新增业务产品
     */
    @GetMapping("/add")
        public String add(ModelMap mmap) {
        mmap.put("seriesSelect",bizDictService.selectBizDictByProductType(BizConstants.productTypeCode));
        mmap.put("suppliers",bizSuppliersService.selectAllList());
        return prefix + "/add";
    }



    /**
     * 新增保存业务产品
     */
    @RequiresPermissions("fmis:product:add")
    @Log(title = "业务产品", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizProduct bizProduct) {
        return toAjax(bizProductService.insertBizProduct(bizProduct));
    }

    @PostMapping("/findProductTypeDict")
    @ResponseBody
    public JSONObject findProductTypeDict(Long dictId) {
        return bizDictService.selectProductTypeDict(dictId);
    }


    /**
     * 修改业务产品
     */
    @GetMapping("/edit/{productId}")
    public String edit(@PathVariable("productId") Long productId, ModelMap mmap) {
        BizProduct bizProduct = bizProductService.selectBizProductById(productId);
        mmap.put("bizProduct", bizProduct);

        List<BizDict> bizDictList = bizDictService.selectBizDictByProductType(BizConstants.productTypeCode);

        for (BizDict bizDict : bizDictList) {
            String dictId = bizDict.getDictId().toString();
            if (dictId.equals(bizProduct.getSeries())) {
                bizDict.setFlag(true);
            }
        }

        mmap.put("seriesSelect",bizDictList);

        List<BizSuppliers> suppliersList = bizSuppliersService.selectAllList();
        for (BizSuppliers suppliers : suppliersList) {
            String supplierId = bizProduct.getSupplier();
            if (supplierId.equals(suppliers.getSuppliersId().toString())) {
                suppliers.setFlag(true);
            }
        }
        mmap.put("suppliers",suppliersList);
        return prefix + "/edit";
    }

    /**
     * 修改保存业务产品
     */
    @RequiresPermissions("fmis:product:edit")
    @Log(title = "业务产品", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizProduct bizProduct) {
        return toAjax(bizProductService.updateBizProduct(bizProduct));
    }

    /**
     * 删除业务产品
     */
    @RequiresPermissions("fmis:product:remove")
    @Log(title = "业务产品", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizProductService.deleteBizProductByIds(ids));
    }


    @GetMapping("/upload")
    public String upload(ModelMap mmap) {
        return prefix + "/upload";
    }

    public static Long processTotal = 0L;
    public static Long processNum = 0L;
    @PostMapping("/processBar")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject processBar(){
        com.alibaba.fastjson.JSONObject retJson = new com.alibaba.fastjson.JSONObject();
        retJson.put("processTotal",processTotal.toString());
        retJson.put("processNum",processNum.toString());
        return retJson;
    }

    @PostMapping("/importExcel")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject importExcel(){
        processTotal = 0L;
        processNum = 0L;
        com.alibaba.fastjson.JSONObject retJson = new com.alibaba.fastjson.JSONObject();
        JSONArray dataArray = new JSONArray();
        JSONArray errorArray = new JSONArray();
        String url = getRequest().getParameter("url");
        String realPath = Global.getFilePath() + url;
        List<BizProductImport> list = new ArrayList<>();
        Date now = new Date();
        int insertCount = 0;
        int updateCount = 0;
        try {
            ExcelProduct<BizProductImport> excelUtil = new ExcelProduct(BizProductImport.class);
            int rows = excelUtil.getRowLength("",realPath);
            if (rows > 50000) {
                JSONObject json = new JSONObject();
                json.put("msg","一次不能超过50000条数据！");
                errorArray.add(json);
                retJson.put("error",errorArray);
            } else {
                list = excelUtil.importExcel("",realPath);
                processTotal = new Long(list.size());

                List<BizSuppliers> suppliersList = bizSuppliersService.selectAllList();
                Map<String,BizSuppliers> suppliersMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(suppliersList)) {
                    for (BizSuppliers bizSuppliers : suppliersList) {
                        String nickName = bizSuppliers.getNickName();
                        if (StringUtils.isNotEmpty(nickName)) {
                            suppliersMap.put(nickName,bizSuppliers);
                        }
                    }
                }
                //类别
                List<BizDict> seriesList = bizDictService.selectBizDictByProductType(BizConstants.productTypeCode);
                Map<String,BizDict> seriesMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(seriesList)) {
                    for (BizDict bizDict : seriesList) {
                        seriesMap.put(bizDict.getName(),bizDict);
                    }
                }
                Map<Long,JSONObject> bizDictMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(seriesList)) {
                    for (BizDict bizDict : seriesList) {
                        Long dictId = bizDict.getDictId();
                        JSONObject jsonObject = bizDictService.selectProductTypeDict(dictId);
                        bizDictMap.put(dictId,jsonObject);
                    }
                }
                //等级
                List<SysDictData> dictDataList = sysDictDataService.selectDictDataByType("product_level");
                Map<String,SysDictData> levelMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(dictDataList)) {
                    for (SysDictData sysDictData : dictDataList) {
                        levelMap.put(sysDictData.getDictLabel(),sysDictData);
                    }
                }
                //所有 产品
                List<BizProduct> existProductList = bizProductService.selectBizProductListNoAuth(new BizProduct());
                Map<String,BizProduct> existProductMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(existProductList)) {
                    for (BizProduct bizProduct : existProductList) {
                        String model = bizProduct.getModel();
                        String specifications = bizProduct.getSpecifications();
                        String supplierNickName = bizProduct.getSupplier();
                        if (StringUtils.isNotEmpty(model)) {
                            existProductMap.put(model + "_" + specifications + "_" + supplierNickName,bizProduct);
                        }
                    }
                }


                logger.info("list size=" + list.size());
                Map<String,String> errorSeriesTypeMap = new HashMap<>();
                Map<String,String> errorSpecificationMap = new HashMap<>();
                Map<String,String> errorNominalPressureMap = new HashMap<>();
                Map<String,String> errorConnectionTypeMap = new HashMap<>();
                Map<String,String> errorStructuralStyleMap = new HashMap<>();
                Map<String,String> errorValvebodyMaterialMap = new HashMap<>();
                Map<String,String> errorValveElementMap = new HashMap<>();
                Map<String,String> errorSealingMaterialMap = new HashMap<>();
                Map<String,String> errorDriveFormMap = new HashMap<>();
                String delFlag = "9";
                for (int i = 0; i < list.size(); i++) {
                    BizProductImport product = list.get(i);
                    logger.info("product import num=" + i);
                    processNum = new Long(i + 2);
                    //供应商
                    String supplier = product.getSupplier();
                    String supplierCode = product.getSupplierCode();
                    BizSuppliers bizSuppliers = null;
                    if (!suppliersMap.containsKey(supplierCode)) {
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 供应商代码=【" + supplierCode + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        break;
                    } else {
                        bizSuppliers = suppliersMap.get(supplierCode);
                    }

                    String series = product.getSeries();
                    BizDict seriesBizDict = seriesMap.get(series);
                    if (seriesBizDict == null) {
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 类别=【" + series + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        break;
                    }
                    Long seriesId = seriesBizDict.getDictId();

                    //等级
                    String productLevel = product.getString2();
                    SysDictData levelDict = levelMap.get(productLevel);
                    if (levelDict == null) {
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 等级=【" + productLevel + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        break;
                    }

                    //系列 string1
                    String seriesType = product.getString1();
                    Long seriesTypeId = getBizDictIdForSeries(seriesType,"seriesType",seriesBizDict,bizDictMap);
                    if (seriesTypeId == 0L) {
                        String errorKey = series + "_" + seriesType;
                        BizDict bizDict = null;
                        if (!errorSeriesTypeMap.containsKey(errorKey)) {
                            bizDict = new BizDict();
                            bizDict.setCode(seriesType);
                            bizDict.setName(seriesType);
                            bizDict.setParentId(seriesBizDict.getDictId());
                            bizDict.setParentType(BizConstants.seriesType);
                            bizDict.setStatus("0");
                            bizDict.setDelFlag(delFlag);
                            bizDict.setCreateBy(ShiroUtils.getUserId().toString());
                            bizDictService.insertBizDict(bizDict);
                            seriesTypeId = bizDict.getDictId();
                            errorSeriesTypeMap.put(errorKey,bizDict.getDictId().toString());
                        } else {
                            seriesTypeId = Long.parseLong(errorSeriesTypeMap.get(errorKey));
                        }
                        /*
                        String errorKey = series + "_" + seriesType;
                        if (errorSeriesTypeMap.containsKey(errorKey)) {
                            continue;
                        }
                        errorSeriesTypeMap.put(errorKey,errorKey);
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 类别=【" + series + "】 的系列=【" + seriesType + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        continue;*/
                    }

                    //规格 specifications
                    String specifications = product.getSpecifications();
                    Long specificationsId = getBizDictIdForSeries(specifications,"specification",seriesBizDict,bizDictMap);
                    if (specificationsId == 0L) {
                        String errorKey = series + "_" + specifications;
                        if (!errorSpecificationMap.containsKey(errorKey)) {
                            BizDict bizDict = new BizDict();
                            bizDict.setCode(specifications.trim());
                            bizDict.setName(specifications.trim());
                            bizDict.setParentId(seriesBizDict.getDictId());
                            bizDict.setParentType(BizConstants.specificationCode);
                            bizDict.setStatus("0");
                            bizDict.setDelFlag(delFlag);
                            bizDict.setCreateBy(ShiroUtils.getUserId().toString());
                            bizDictService.insertBizDict(bizDict);
                            specificationsId = bizDict.getDictId();
                            errorSpecificationMap.put(errorKey,specificationsId.toString());
                        } else {
                            specificationsId = Long.parseLong(errorSpecificationMap.get(errorKey));
                        }


                        /*
                        String errorKey = series + "_" + specifications;
                        if (errorSpecificationMap.containsKey(errorKey)) {
                            continue;
                        }
                        errorSpecificationMap.put(errorKey,errorKey);
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 类别=【" + series + "】 的规格=【" + specifications + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        continue;*/
                    }
                    //公称压力 nominalPressure
                    String nominalPressure = product.getNominalPressure();
                    Long nominalPressureId = getBizDictIdForSeries(nominalPressure,"nominalPressure",seriesBizDict,bizDictMap);
                    if (nominalPressureId == 0L) {
                        String errorKey = series + "_" + nominalPressure;
                        if (!errorNominalPressureMap.containsKey(errorKey)) {
                            BizDict bizDict = new BizDict();
                            bizDict.setCode(nominalPressure.trim());
                            bizDict.setName(nominalPressure.trim());
                            bizDict.setParentId(seriesBizDict.getDictId());
                            bizDict.setParentType(BizConstants.nominalPressure);
                            bizDict.setStatus("0");
                            bizDict.setDelFlag(delFlag);
                            bizDict.setCreateBy(ShiroUtils.getUserId().toString());
                            bizDictService.insertBizDict(bizDict);
                            nominalPressureId = bizDict.getDictId();
                            errorNominalPressureMap.put(errorKey,nominalPressureId.toString());
                        } else {
                            nominalPressureId = Long.parseLong(errorNominalPressureMap.get(errorKey));
                        }


                        /*
                        String errorKey = series + "_" + nominalPressure;
                        if (errorNominalPressureMap.containsKey(errorKey)) {
                            continue;
                        }
                        errorNominalPressureMap.put(errorKey,errorKey);
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 类别=【" + series + "】 的公称压力=【" + nominalPressure + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        continue;*/
                    }
                    //连接方式 connectionType
                    String connectionType = product.getConnectionType();
                    Long connectionTypeId = getBizDictIdForSeries(connectionType,"linkType",seriesBizDict,bizDictMap);
                    if (connectionTypeId == 0L) {
                        String errorKey = series + "_" + connectionType;
                        if (!errorConnectionTypeMap.containsKey(errorKey)) {
                            BizDict bizDict = new BizDict();
                            bizDict.setCode(connectionType.trim());
                            bizDict.setName(connectionType.trim());
                            bizDict.setParentId(seriesBizDict.getDictId());
                            bizDict.setParentType(BizConstants.linkType);
                            bizDict.setStatus("0");
                            bizDict.setDelFlag(delFlag);
                            bizDict.setCreateBy(ShiroUtils.getUserId().toString());
                            bizDictService.insertBizDict(bizDict);
                            connectionTypeId = bizDict.getDictId();
                            errorConnectionTypeMap.put(errorKey,connectionTypeId.toString());
                        } else {
                            connectionTypeId = Long.parseLong(errorConnectionTypeMap.get(errorKey));
                        }


                        /*
                        String errorKey = series + "_" + connectionType;
                        if (errorConnectionTypeMap.containsKey(errorKey)) {
                            continue;
                        }
                        errorConnectionTypeMap.put(errorKey,errorKey);
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 类别=【" + series + "】 的连接方式=【" + connectionType + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        continue;*/

                    }
                    //结构形式 structuralStyle
                    String structuralStyle = product.getStructuralStyle();
                    Long structuralStyleId = getBizDictIdForSeries(structuralStyle,"structuralStyle",seriesBizDict,bizDictMap);
                    if (structuralStyleId == 0L) {
                        String errorKey = series + "_" + structuralStyle;
                        if (!errorStructuralStyleMap.containsKey(errorKey)) {
                            errorStructuralStyleMap.put(errorKey,errorKey);
                            BizDict bizDict = new BizDict();
                            bizDict.setCode(structuralStyle.trim());
                            bizDict.setName(structuralStyle.trim());
                            bizDict.setParentId(seriesBizDict.getDictId());
                            bizDict.setParentType(BizConstants.structuralType);
                            bizDict.setStatus("0");
                            bizDict.setDelFlag(delFlag);
                            bizDict.setCreateBy(ShiroUtils.getUserId().toString());
                            bizDictService.insertBizDict(bizDict);
                            structuralStyleId = bizDict.getDictId();
                            errorStructuralStyleMap.put(errorKey,structuralStyleId.toString());
                        } else {
                            structuralStyleId = Long.parseLong(errorStructuralStyleMap.get(errorKey));
                        }

                        /*
                        String errorKey = series + "_" + structuralStyle;
                        if (errorStructuralStyleMap.containsKey(errorKey)) {
                            continue;
                        }
                        errorStructuralStyleMap.put(errorKey,errorKey);
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 类别=【" + series + "】 的结构形式=【" + structuralStyle + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        continue;*/
                    }
                    //阀体材质 valvebodyMaterial
                    String valvebodyMaterial = product.getValvebodyMaterial();
                    Long valvebodyMaterialId = getBizDictIdForSeries(valvebodyMaterial,"bodyMaterial",seriesBizDict,bizDictMap);
                    if (valvebodyMaterialId == 0L) {
                        String errorKey = series + "_" + valvebodyMaterial;
                        if (!errorValvebodyMaterialMap.containsKey(errorKey)) {
                            BizDict bizDict = new BizDict();
                            bizDict.setCode(valvebodyMaterial.trim());
                            bizDict.setName(valvebodyMaterial.trim());
                            bizDict.setParentId(seriesBizDict.getDictId());
                            bizDict.setParentType(BizConstants.bodyMaterial);
                            bizDict.setStatus("0");
                            bizDict.setDelFlag(delFlag);
                            bizDict.setCreateBy(ShiroUtils.getUserId().toString());
                            bizDictService.insertBizDict(bizDict);
                            valvebodyMaterialId = bizDict.getDictId();
                            errorValvebodyMaterialMap.put(errorKey,valvebodyMaterialId.toString());
                        } else {
                            valvebodyMaterialId = Long.parseLong(errorValvebodyMaterialMap.get(errorKey));
                        }


                        /*
                        String errorKey = series + "_" + valvebodyMaterial;
                        if (errorValvebodyMaterialMap.containsKey(errorKey)) {
                            continue;
                        }
                        errorValvebodyMaterialMap.put(errorKey,errorKey);
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 类别=【" + series + "】 的阀体材质=【" + valvebodyMaterial + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        continue;*/
                    }
                    //阀芯材质 valveElement
                    String valveElement = product.getValveElement();
                    Long valveElementId = getBizDictIdForSeries(valveElement,"spoolMaterial",seriesBizDict,bizDictMap);
                    if (valveElementId == 0L) {
                        String errorKey = series + "_" + valveElement;
                        if (!errorValveElementMap.containsKey(errorKey)) {
                            BizDict bizDict = new BizDict();
                            bizDict.setCode(valveElement.trim());
                            bizDict.setName(valveElement.trim());
                            bizDict.setParentId(seriesBizDict.getDictId());
                            bizDict.setParentType(BizConstants.spoolMaterial);
                            bizDict.setStatus("0");
                            bizDict.setDelFlag(delFlag);
                            bizDict.setCreateBy(ShiroUtils.getUserId().toString());
                            bizDictService.insertBizDict(bizDict);
                            valveElementId = bizDict.getDictId();
                            errorValveElementMap.put(errorKey,valveElementId.toString());
                        } else {
                            valveElementId = Long.parseLong(errorValveElementMap.get(errorKey));
                        }


                        /*
                        String errorKey = series + "_" + valveElement;
                        if (errorValveElementMap.containsKey(errorKey)) {
                            continue;
                        }
                        errorValveElementMap.put(errorKey,errorKey);
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 类别=【" + series + "】 的阀芯材质=【" + valveElement + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        continue;*/
                    }
                    //密封材质 sealingMaterial
                    String sealingMaterial = product.getSealingMaterial();
                    Long sealingMaterialId = getBizDictIdForSeries(sealingMaterial,"sealingMaterial",seriesBizDict,bizDictMap);
                    if (sealingMaterialId == 0L) {
                        String errorKey = series + "_" + sealingMaterial;
                        if (!errorSealingMaterialMap.containsKey(errorKey)) {
                            BizDict bizDict = new BizDict();
                            bizDict.setCode(sealingMaterial.trim());
                            bizDict.setName(sealingMaterial.trim());
                            bizDict.setParentId(seriesBizDict.getDictId());
                            bizDict.setParentType(BizConstants.sealingMaterial);
                            bizDict.setStatus("0");
                            bizDict.setDelFlag(delFlag);
                            bizDict.setCreateBy(ShiroUtils.getUserId().toString());
                            bizDictService.insertBizDict(bizDict);
                            sealingMaterialId = bizDict.getDictId();
                            errorSealingMaterialMap.put(errorKey,sealingMaterialId.toString());
                        } else {
                            sealingMaterialId = Long.parseLong(errorSealingMaterialMap.get(errorKey));
                        }


                        /*
                        String errorKey = series + "_" + sealingMaterial;
                        if (errorSealingMaterialMap.containsKey(errorKey)) {
                            continue;
                        }
                        errorSealingMaterialMap.put(errorKey,errorKey);
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 类别=【" + series + "】 的密封材质=【" + sealingMaterial + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        continue;*/
                    }
                    //驱动形式 driveForm
                    String driveForm = product.getDriveForm();
                    Long driveFormId = getBizDictIdForSeries(driveForm,"driveMode",seriesBizDict,bizDictMap);
                    if (driveFormId == 0L) {
                        String errorKey = series + "_" + driveForm;
                        if (!errorDriveFormMap.containsKey(errorKey)) {
                            BizDict bizDict = new BizDict();
                            bizDict.setCode(driveForm.trim());
                            bizDict.setName(driveForm.trim());
                            bizDict.setParentId(seriesBizDict.getDictId());
                            bizDict.setParentType(BizConstants.driveMode);
                            bizDict.setStatus("0");
                            bizDict.setDelFlag(delFlag);
                            bizDict.setCreateBy(ShiroUtils.getUserId().toString());
                            bizDictService.insertBizDict(bizDict);
                            driveFormId = bizDict.getDictId();
                            errorDriveFormMap.put(errorKey,driveFormId.toString());
                        } else {
                            driveFormId = Long.parseLong(errorDriveFormMap.get(errorKey));
                        }


                        /*
                        String errorKey = series + "_" + driveForm;
                        if (errorDriveFormMap.containsKey(errorKey)) {
                            continue;
                        }
                        errorDriveFormMap.put(errorKey,errorKey);
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行 类别=【" + series + "】 的驱动形式=【" + driveForm + "】不存在！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        continue;*/
                    }

                    String model = product.getModel().trim();
                    /*BizProduct removeBizProduct = new BizProduct();
                    removeBizProduct.setModel(model);
                    List<BizProduct> removeBizProducts = bizProductService.selectBizProductList(removeBizProduct);
                    if (!CollectionUtils.isEmpty(removeBizProducts)) {
                        for (BizProduct bizProduct : removeBizProducts) {
                            bizProductService.deleteBizProductById(bizProduct.getProductId());
                        }
                    }*/

                    //插入产品数据
                    String specifications_ = product.getSpecifications();
                    String supplierNickName_ = product.getSupplierCode();
                    String existKey = model + "_" + specifications_ + "_" + supplierNickName_;
                    BizProduct bizProduct = new BizProduct();
                    if (existProductMap.containsKey(existKey)) {
                        bizProduct = existProductMap.get(existKey);
                    }
                    bizProduct.setName(product.getName());
                    bizProduct.setString1(seriesTypeId.toString());
                    bizProduct.setSeries(seriesId.toString());
                    bizProduct.setString2(levelDict.getDictValue());
                    bizProduct.setModel(model);
                    bizProduct.setSpecifications(specificationsId.toString());
                    bizProduct.setNominalPressure(nominalPressureId.toString());
                    bizProduct.setConnectionType(connectionTypeId.toString());
                    bizProduct.setStructuralStyle(structuralStyleId.toString());
                    bizProduct.setValvebodyMaterial(valvebodyMaterialId.toString());
                    bizProduct.setValveElement(valveElementId.toString());
                    bizProduct.setSealingMaterial(sealingMaterialId.toString());
                    bizProduct.setDriveForm(driveFormId.toString());
                    bizProduct.setPrice(Double.parseDouble(product.getPrice()));
                    bizProduct.setSupplier(bizSuppliers.getSuppliersId().toString());
                    if (existProductMap.containsKey(existKey)) {
                        bizProductService.updateBizProduct(bizProduct);
                        updateCount++;
                    } else {
                        bizProduct.setCreateBy(ShiroUtils.getUserId().toString());
                        bizProduct.setCreateTime(now);
                        bizProductService.insertBizProduct(bizProduct);
                        insertCount++;
                    }

                }
                JSONObject json = new JSONObject();
                json.put("msg","成功导入" + list.size() + "条数据，增加" + insertCount + "条，更新" + updateCount + "条！");
                dataArray.add(json);
                retJson.put("data",dataArray);
                processTotal = 0L;
                processNum = 0L;
            }

        } catch (Exception e)
        {
            processTotal = 0L;
            processNum = 0L;
            e.printStackTrace();
            throw new BusinessException("导出失败，请联系网站管理员！");
        }

        return retJson;
    }


    public Long getBizDictIdForSeries (String columnName,String dictTypeName,BizDict seriesBizDict,Map<Long,JSONObject> bizDictMap) {
        JSONObject jsonObject = bizDictMap.get(seriesBizDict.getDictId());
        Long seriesTypeId = 0L;
        List<BizDict> seriesTypeDict = (List<BizDict>)jsonObject.get(dictTypeName);
        for (BizDict bizDict : seriesTypeDict) {
            if (columnName.equals(bizDict.getName())) {
                seriesTypeId = bizDict.getDictId();
                break;
            }
        }
        return seriesTypeId;
    }
}
