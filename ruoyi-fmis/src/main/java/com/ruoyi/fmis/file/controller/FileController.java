package com.ruoyi.fmis.file.controller;

import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.fmis.file.domain.BizAccessory;
import com.ruoyi.fmis.file.service.IBizAccessoryService;
import com.ruoyi.framework.util.ShiroUtils;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程记录Controller
 *
 * @author frank
 * @date 2020-03-18
 */
@Controller
@RequestMapping("/fmis/file")
public class FileController extends BaseController {
    private String prefix = "fmis/file";

    @Autowired
    private IBizAccessoryService bizAccessoryService;


    @RequestMapping(value = "/upload",produces="application/json;charset=utf-8")
    @ResponseBody
    public Map<String,Object> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        Map<String, Object> uploadData = new HashMap<String, Object>();
        String uploadDocsPath = Global.getFilePath();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dir = new File(uploadDocsPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        logger.info("uploadDocsPath=" + uploadDocsPath);
        try {
            Path path = Paths.get(uploadDocsPath + "//" + fileName);
            Files.write(path,file.getBytes());
            uploadData.put("code", 0);
            uploadData.put("msg", "上传成功");
            uploadData.put("data", fileName);
        } catch (IOException e) {
            uploadData.put("code", -1);
            uploadData.put("msg", "上传失败");
            uploadData.put("data", "");
            uploadData.put("error", "上传失败，请检查网络连接或联系管理员");
        }
        return uploadData;
    }


    @GetMapping("/upload/view")
    public String uploadView(ModelMap modelMap) {
        // 文件类目 表明附件是属于哪个列表的。1 供应商 2 销售合同列表 3 采购合同列表 4 质检检验
        String fileType = getRequest().getParameter("fileType");
        String bizId = getRequest().getParameter("bizId");
        modelMap.put("fileType", fileType);
        modelMap.put("bizId", bizId);
        return prefix + "/fileUpload";
    }

    /**
     * 添加附件
     * @param bizAccessory
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public AjaxResult addAccessory(BizAccessory bizAccessory) {
        String fileName = bizAccessory.getFileName();
        int suffixIdx = fileName.lastIndexOf(".");
        String fileFormat = fileName.substring(suffixIdx);
        bizAccessory.setFileFormat(fileFormat);
        bizAccessory.setDelFlag(0);
        bizAccessory.setCreateBy(ShiroUtils.getLoginName());
        bizAccessory.setCreateTime(new Date());
        bizAccessory.setUpdateBy(ShiroUtils.getLoginName());
        bizAccessory.setUpdateTime(new Date());
        int i = bizAccessoryService.insertBizAccessory(bizAccessory);
        return AjaxResult.success(i);
    }

    /**
     * 查询附件列表
     * @param bizAccessory
     * @return
     */
    @PostMapping("list")
    @ResponseBody
    public AjaxResult listAccessory(BizAccessory bizAccessory) {
        return AjaxResult.success(bizAccessoryService.selectBizAccessoryByBizId(bizAccessory));
    }


    /**
     * 删除附件
     * @param id
     * @return
     */
    @PostMapping("delete/{id}")
    @ResponseBody
    public AjaxResult deleteAccessory(@PathVariable("id") Integer id) {
        return AjaxResult.success(bizAccessoryService.deleteBizAccessoryById(id));
    }

}
