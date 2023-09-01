package com.ruoyi.fmis.suppliers.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelProduct;
import com.ruoyi.fmis.common.BizProductImport;
import com.ruoyi.fmis.common.BizSuppliersImport;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.service.IBizProductService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
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
import com.ruoyi.fmis.suppliers.domain.BizSuppliers;
import com.ruoyi.fmis.suppliers.service.IBizSuppliersService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 供应商Controller
 *
 * @author Xianlu Tech
 * @date 2020-03-06
 */
@Controller
@RequestMapping("/fmis/suppliers")
public class BizSuppliersController extends BaseController {
    private String prefix = "fmis/suppliers";

    @Autowired
    private IBizSuppliersService bizSuppliersService;

    @Autowired
    private ISysUserService userService;
    @Autowired
    private IBizProductService bizProductService;

    @RequiresPermissions("fmis:suppliers:view")
    @GetMapping()
    public String suppliers() {
        return prefix + "/suppliers";
    }

    /**
     * 查询供应商列表
     */
    @RequiresPermissions("fmis:suppliers:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizSuppliers bizSuppliers) {
        startPage();
        List<BizSuppliers> list = bizSuppliersService.selectBizSuppliersList(bizSuppliers);
            return getDataTable(list);
    }

    @GetMapping("/product")
    public String productList(ModelMap modelMap) {
        String supplierId = getRequest().getParameter("supplierId");
        modelMap.put("supplierId", supplierId);
        return prefix +"/product";
    }

    /**
     * 导出供应商列表
     */
    @RequiresPermissions("fmis:suppliers:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizSuppliers bizSuppliers) {
        List<BizSuppliers> list = bizSuppliersService.selectBizSuppliersList(bizSuppliers);
        ExcelUtil<BizSuppliers> util = new ExcelUtil<BizSuppliers>(BizSuppliers.class);
        return util.exportExcel(list, "suppliers");
    }

    /**
     * 新增供应商
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("users", userService.selectUserList(new SysUser()));
        return prefix + "/add";
    }

    /**
     * 新增保存供应商
     */
    @RequiresPermissions("fmis:suppliers:add")
    @Log(title = "供应商", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizSuppliers bizSuppliers) {
        return toAjax(bizSuppliersService.insertBizSuppliers(bizSuppliers));
    }

    /**
     * 修改供应商
     */
    @GetMapping("/edit/{suppliersId}")
    public String edit(@PathVariable("suppliersId") Long suppliersId, ModelMap mmap) {
        BizSuppliers bizSuppliers = bizSuppliersService.selectBizSuppliersById(suppliersId);
        mmap.put("usersel", userService.selectUserById(Long.parseLong(bizSuppliers.getOwnerId())));
        mmap.put("users", userService.selectUserList(new SysUser()));
        mmap.put("bizSuppliers", bizSuppliers);
        return prefix + "/edit";
    }

    /**
     * 修改保存供应商
     */
    @RequiresPermissions("fmis:suppliers:edit")
    @Log(title = "供应商", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizSuppliers bizSuppliers) {
        return toAjax(bizSuppliersService.updateBizSuppliers(bizSuppliers));
    }

    /**
     * 删除供应商
     */
    @RequiresPermissions("fmis:suppliers:remove")
    @Log(title = "供应商", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizSuppliersService.deleteBizSuppliersByIds(ids));
    }


    @GetMapping("/upload")
    public String upload(ModelMap mmap) {
        return prefix + "/upload";
    }
    @PostMapping("/importExcel")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject importExcel(){
        com.alibaba.fastjson.JSONObject retJson = new com.alibaba.fastjson.JSONObject();
        JSONArray dataArray = new JSONArray();
        JSONArray errorArray = new JSONArray();
        String url = getRequest().getParameter("url");
        String realPath = Global.getFilePath() + url;
        List<BizSuppliersImport> list = new ArrayList<>();
        try {
            ExcelUtil<BizSuppliersImport> excelUtil = new ExcelUtil(BizSuppliersImport.class);
            int rows = excelUtil.getRowLength("",realPath);
            if (rows > 50000) {
                JSONObject json = new JSONObject();
                json.put("msg","一次不能超过50000条数据");
                errorArray.add(json);
                retJson.put("error",errorArray);
            } else {
                list = excelUtil.importExcel("",realPath);
                logger.info("list size=" + list.size());
                int insertCount = 0;
                int updateCount = 0;
                List<BizSuppliers> bizSuppliersList = bizSuppliersService.selectAllList();
                Map<String,BizSuppliers> bizSuppliersMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(bizSuppliersList)) {
                    for (BizSuppliers bizSuppliers : bizSuppliersList) {
                        bizSuppliersMap.put(bizSuppliers.getNickName(),bizSuppliers);
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    BizSuppliersImport suppliersImport = list.get(i);
                    String nickName = suppliersImport.getCode();
                    String name = suppliersImport.getName();
                    if (StringUtils.isEmpty(nickName) || StringUtils.isEmpty(name)) {
                        JSONObject json = new JSONObject();
                        json.put("msg","第" + (i + 2) + "行  名称或者代码不能为空！");
                        errorArray.add(json);
                        retJson.put("error",errorArray);
                        break;
                    }
                    if (bizSuppliersMap.containsKey(nickName)) {
                        bizSuppliersService.deleteBizSuppliersById(bizSuppliersMap.get(nickName).getSuppliersId());
                        updateCount++;
                    } else {
                        insertCount++;
                    }
                    BizSuppliers bizSuppliers = new BizSuppliers();
                    bizSuppliers.setName(name);
                    bizSuppliers.setNickName(nickName);
                    bizSuppliers.setCreateBy(ShiroUtils.getUserId().toString());
                    bizSuppliers.setStatus("0");
                    bizSuppliers.setDelFlag("0");
                    bizSuppliers.setOwnerId(ShiroUtils.getUserId().toString());
                    bizSuppliersService.insertBizSuppliers(bizSuppliers);
                }
                JSONObject json = new JSONObject();
                json.put("msg","成功导入 添加 " + insertCount + "条数据，覆盖 " + updateCount + "条数据！");
                dataArray.add(json);
                retJson.put("data",dataArray);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException("导出失败，请联系网站管理员！");
        }
        return retJson;
    }
    // 临时注掉权限
//    @RequiresPermissions("fmis:supplier:exportProduct")
    @PostMapping("exportProduct")
    @ResponseBody
    public AjaxResult exportProduct(BizProduct bizProduct) {
        List<BizProduct> list = bizProductService.selectBizProductListNoAuth(bizProduct);
        ArrayList<BizProductImport> bizProductImports = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)) {
            int i = 1;
            for (BizProduct bizProduct1: list) {
                BizProductImport bizProductImport = new BizProductImport();
                bizProductImport.setSeqNum(i + "");
                bizProductImport.setName(bizProduct1.getName());
                bizProductImport.setString1(bizProduct1.getString1());
                bizProductImport.setSeries(bizProduct1.getSeries());
                bizProductImport.setString2(bizProduct1.getString2());
                bizProductImport.setModel(bizProduct1.getModel());
                bizProductImport.setSpecifications(bizProduct1.getSpecifications());
                bizProductImport.setNominalPressure(bizProduct1.getNominalPressure());
                bizProductImport.setConnectionType(bizProduct1.getConnectionType());
                bizProductImport.setStructuralStyle(bizProduct1.getStructuralStyle());
                bizProductImport.setValvebodyMaterial(bizProduct1.getValvebodyMaterial());
                bizProductImport.setValveElement(bizProduct1.getValveElement());
                bizProductImport.setSealingMaterial(bizProduct1.getSealingMaterial());
                bizProductImport.setDriveForm(bizProduct1.getDriveForm());
                bizProductImport.setPrice(String.valueOf(bizProduct1.getPrice()));
                bizProductImport.setSupplierCode(bizProduct1.getSupplier());
                bizProductImport.setSupplier(bizProduct1.getSupplierName());
                bizProductImport.setColor(bizProduct1.getColor());
                bizProductImport.setString4(bizProduct1.getString4());
                bizProductImport.setProcurementPrice(bizProduct1.getProcurementPrice() + "");
                bizProductImport.setRemark(bizProduct1.getRemark());
                bizProductImport.setValveShaft(bizProduct1.getString3());
                bizProductImport.setString5(bizProduct1.getString5());
                bizProductImport.setCostPrice(bizProduct1.getCostPrice() + "");
                bizProductImport.setPrice(bizProduct1.getPrice() + "");
                bizProductImport.setFacePrice(bizProduct1.getFacePrice() + "");
                bizProductImports.add(bizProductImport);
                i++;
            }
        }
        ExcelUtil<BizProductImport> util = new ExcelUtil<BizProductImport>(BizProductImport.class);
        return util.exportExcel(bizProductImports, "供应商产品");
    }
}
