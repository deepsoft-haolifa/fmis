package com.ruoyi.fmis.file.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fmis.flow.domain.BizFlow;
import com.ruoyi.fmis.flow.service.IBizFlowService;
import com.ruoyi.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import java.util.HashMap;
import java.util.List;
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
}
