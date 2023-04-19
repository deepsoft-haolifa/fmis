package com.ruoyi.fmis.actuator.controller;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.actuatorref.service.IBizActuatorRefService;
import com.ruoyi.fmis.common.BizActuatorImport;
import com.ruoyi.fmis.dict.domain.BizDict;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.actuator.service.IBizActuatorService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 执行器Controller
 *
 * @author frank
 * @date 2020-03-17
 */
@Controller
@RequestMapping("/fmis/actuator")
public class BizActuatorController extends BaseController {
    private String prefix = "fmis/actuator";

    @Autowired
    private IBizActuatorService bizActuatorService;

    @Autowired
    private IBizDictService bizDictService;

    @Autowired
    private IBizActuatorRefService bizActuatorRefService;

    @RequiresPermissions("fmis:actuator:view")
    @GetMapping()
    public String actuator() {
        return prefix + "/actuator";
    }

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Autowired
    private IBizProductService bizProductService;

    @Autowired
    private IBizSuppliersService bizSuppliersService;
    /**
     * 查询执行器列表
     */
    @RequiresPermissions("fmis:actuator:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizActuator bizActuator) {
        startPage();
        List<BizActuator> list = bizActuatorService.selectBizActuatorList(bizActuator);
        return getDataTable(list);
    }

    @PostMapping("/listForProductId")
    @ResponseBody
    public TableDataInfo listForProductId(BizActuator bizActuator) {


        String productId = getRequest().getParameter("productId");

        BizProduct bizProduct = bizProductService.selectBizProductById(Long.parseLong(productId));

        //bizProduct = null;

        if (bizProduct != null) {
            String specifications = bizProduct.getSpecifications();

            if (StringUtils.isNotEmpty(specifications) && bizActuator.getString7() != null && bizActuator.getString7().equals("-1")) {
                BizDict bizDict = bizDictService.selectBizDictById(Long.parseLong(specifications));
               /* if (bizDict != null) {
                    bizActuator.setString1(bizDict.getName());
                }*/
                bizActuator.setString10(specifications);
            }

            String nominalPressure = bizProduct.getNominalPressure();
            if (StringUtils.isNotEmpty(nominalPressure)) {
                BizDict bizDict = bizDictService.selectBizDictById(Long.parseLong(nominalPressure));
                if (bizDict != null) {
                    bizActuator.setString3(bizDict.getName());
                }
            }
        }

        //startPage();
        List<BizActuator> list = bizActuatorService.selectBizActuatorForRefList(bizActuator);
        return getDataTable(list);
    }

    /**
     * 导出执行器列表
     */
    @RequiresPermissions("fmis:actuator:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizActuator bizActuator) {
        List<BizActuator> list = bizActuatorService.selectBizActuatorList(bizActuator);
        ExcelUtil<BizActuator> util = new ExcelUtil<BizActuator>(BizActuator.class);
        return util.exportExcel(list, "actuator");
    }

    /**
     * 新增执行器
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("suppliers",bizSuppliersService.selectAllList());
        return prefix + "/add";
    }

    /**
     * 新增保存执行器
     */
    @RequiresPermissions("fmis:actuator:add")
    @Log(title = "执行器", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizActuator bizActuator) {
        return toAjax(bizActuatorService.insertBizActuator(bizActuator));
    }

    /**
     * 修改执行器
     */
    @GetMapping("/edit/{actuatorId}")
    public String edit(@PathVariable("actuatorId") Long actuatorId, ModelMap mmap) {
        BizActuator bizActuator = bizActuatorService.selectBizActuatorById(actuatorId);
        mmap.put("bizActuator", bizActuator);


        List<BizSuppliers> suppliersList = bizSuppliersService.selectAllList();
        for (BizSuppliers suppliers : suppliersList) {
            String supplierId = bizActuator.getString10();
            if (supplierId.equals(suppliers.getSuppliersId().toString())) {
                suppliers.setFlag(true);
            }
        }
        mmap.put("suppliers",suppliersList);

        return prefix + "/edit";
    }

    /**
     * 修改保存执行器
     */
    @RequiresPermissions("fmis:actuator:edit")
    @Log(title = "执行器", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizActuator bizActuator) {
        return toAjax(bizActuatorService.updateBizActuator(bizActuator));
    }

    /**
     * 删除执行器
     */
    @RequiresPermissions("fmis:actuator:remove")
    @Log(title = "执行器", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizActuatorService.deleteBizActuatorByIds(ids));
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
        String typeParamter = getRequest().getParameter("type");
        String realPath = Global.getFilePath() + url;
        List<BizActuatorImport> list = new ArrayList<>();
        String type = "1";
        if (StringUtils.isNotEmpty(typeParamter)) {
            type = typeParamter;
        }
        Date now = new Date();
        try {
            ExcelUtil<BizActuatorImport> excelUtil = new ExcelUtil(BizActuatorImport.class);
            int rows = excelUtil.getRowLength("",realPath);
            if (rows > 50000) {
                JSONObject json = new JSONObject();
                json.put("msg","一次不能超过50000条数据！");
                errorArray.add(json);
                retJson.put("error",errorArray);
            } else {
                Map<String,SysDictData> setupTypeMap = sysDictDataService.getDictDataMapByCode("actuator_setup_type");//setupType
                BizActuator queryBizActuator = new BizActuator();
                queryBizActuator.setString2(type);
                List<BizActuator> actuators = bizActuatorService.selectBizActuatorList(queryBizActuator);
                Map<String,BizActuator> actuatorMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(actuators)) {
                    for (BizActuator bizActuator : actuators) {
                        String code = bizActuator.getString1();
                        actuatorMap.put(code,bizActuator);
                    }
                }
                list = excelUtil.importExcel("",realPath);
                processTotal = new Long(list.size());
                for (int i = 0; i < list.size(); i++) {
                    BizActuatorImport actuatorImport = list.get(i);
                    logger.info("actuator import num=" + i);
                    processNum = new Long(i + 2);

                    String setupTypeName = StringUtils.trim(actuatorImport.getSetupType());
                    String setupType = sysDictDataService.saveDictData("actuator_setup_type",setupTypeName,setupTypeMap,ShiroUtils.getLoginName());

                    String string1 = StringUtils.trim(actuatorImport.getString1());
                    if ("2".equals(type)) {
                        string1 = StringUtils.trim(actuatorImport.getAreaString1());
                    }
                    BizActuator newBizActuator = new BizActuator();
                    if (actuatorMap.containsKey(string1)) {
                        newBizActuator = actuatorMap.get(string1);
                    }
                    newBizActuator.setString1(string1);
                    //areaString1


                    newBizActuator.setSetupType(setupType);
                    newBizActuator.setString2(type);
                    newBizActuator.setName(StringUtils.trim(actuatorImport.getName()));
                    newBizActuator.setQualityLevel(StringUtils.trim(actuatorImport.getQualityLevel()));
                    newBizActuator.setBrand(StringUtils.trim(actuatorImport.getBrand()));
                    newBizActuator.setOutputTorque(StringUtils.trim(actuatorImport.getOutputTorque()));
                    newBizActuator.setActionType(StringUtils.trim(actuatorImport.getActionType()));
                    newBizActuator.setControlCircuit(StringUtils.trim(actuatorImport.getControlCircuit()));
                    newBizActuator.setAdaptableVoltage(StringUtils.trim(actuatorImport.getAdaptableVoltage()));
                    newBizActuator.setProtectionLevel(StringUtils.trim(actuatorImport.getProtectionLevel()));
                    newBizActuator.setExplosionLevel(StringUtils.trim(actuatorImport.getExplosionLevel()));
                    newBizActuator.setManufacturer(StringUtils.trim(actuatorImport.getManufacturer()));
                    newBizActuator.setPrice(StringUtils.toDouble(actuatorImport.getPrice()));
                    newBizActuator.setRemark(StringUtils.trim(actuatorImport.getRemark()));

                    newBizActuator.setString3(StringUtils.trim(actuatorImport.getString3()));
                    newBizActuator.setString4(StringUtils.trim(actuatorImport.getString4()));
                    newBizActuator.setString5(StringUtils.trim(actuatorImport.getString5()));
                    newBizActuator.setString6(StringUtils.trim(actuatorImport.getString6()));
                    newBizActuator.setString9(StringUtils.trim(actuatorImport.getString9()));
                    newBizActuator.setPress(StringUtils.trim(actuatorImport.getPress()));
                    newBizActuator.setColor(StringUtils.trim(actuatorImport.getColor()));
                    newBizActuator.setFacePrice(StringUtils.toDouble(actuatorImport.getFacePrice()));


                    if ("2".equals(type)) {
                        newBizActuator.setQualityLevel(StringUtils.trim(actuatorImport.getAreaQualityLevel()));
                        newBizActuator.setString3(StringUtils.trim(actuatorImport.getAreaString3()));
                        newBizActuator.setString4(StringUtils.trim(actuatorImport.getString4()));
                        newBizActuator.setString5(StringUtils.trim(actuatorImport.getString5()));
                        newBizActuator.setPrice(StringUtils.toDouble(actuatorImport.getAreaPrice()));

                        //newBizActuator.setString6(StringUtils.trim(actuatorImport.getString6()));
                        newBizActuator.setString8(StringUtils.trim(actuatorImport.getString8()));
                        //特殊
                        newBizActuator.setString7(StringUtils.trim(actuatorImport.getString3()));

                    }



                    if (actuatorMap.containsKey(string1)) {
                        bizActuatorService.updateBizActuator(newBizActuator);
                    } else {
                        newBizActuator.setCreateBy(ShiroUtils.getUserId().toString());
                        newBizActuator.setCreateTime(now);
                        bizActuatorService.insertBizActuator(newBizActuator);
                    }
                }
                JSONObject json = new JSONObject();
                json.put("msg","成功导入" + list.size() + "条数据！");
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
}
