package com.ruoyi.fmis.file.controller;

import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fmis.common.BizConstants;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.fmis.file.domain.BizAccessory;
import com.ruoyi.fmis.file.domain.BizManageFile;
import com.ruoyi.fmis.file.service.IBizAccessoryService;
import com.ruoyi.fmis.file.service.IBizManageFileService;
import com.ruoyi.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/***
 * 文件管理
 */
@Controller
@RequestMapping("/fmis/fileManage")
public class FileManageController extends BaseController {
    private String prefix = "fmis/fileManage";

    @Autowired
    private IBizManageFileService bizManageFileService;

    @Autowired
    private IBizDictService bizDictService;

    @RequiresPermissions("fmis:fileManage:view")
    @GetMapping("/list")
    public String listView(ModelMap modelMap) {
        // 文件类目 表明附件是属于哪个列表的。1 供应商 2 销售合同列表 3 采购合同列表 4 质检检验
        String fileType = getRequest().getParameter("fileType");
        String bizId = getRequest().getParameter("bizId");
        modelMap.put("fileType", fileType);
        modelMap.put("bizId", bizId);
//        modelMap.put("fileClassify",bizDictService.findBizDictByCode("fileClassify"));
        return prefix + "/fileList";
    }


    @GetMapping("/add")
    public String add(ModelMap mmap) {
        return prefix + "/add";
    }
    @RequestMapping(value = "/upload",produces="application/json;charset=utf-8")
    @ResponseBody
    public Map<String,Object> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        Map<String, Object> uploadData = new HashMap<String, Object>();
        String uploadDocsPath = Global.getManagerFilePath();
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

    @GetMapping("preview")
    @ResponseBody
    public void previewFile() throws IOException {
        String fileName = getRequest().getParameter("fileName");
//        String filePath = "/home/img/images/"+fileName;
        String filePath = Global.getManagerFilePath();
        HttpServletResponse response = getResponse();
        if(fileName.endsWith("pdf") || fileName.endsWith("PDF")) {
            response.setContentType("application/pdf");
        } else {
            response.setContentType("image/jpeg");
        }
        FileInputStream in = new FileInputStream(new File(filePath));
        OutputStream out = response.getOutputStream();
        byte[] b = new byte[1024];
        while ((in.read(b))!=-1) {
            out.write(b);
        }
        out.flush();
        in.close();
        out.close();
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
     * @param bizManageFile
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public AjaxResult addAccessory(BizManageFile bizManageFile) {
        String fileName = bizManageFile.getFileName();
        int suffixIdx = fileName.lastIndexOf(".");
        String type = fileName.substring(suffixIdx);
        bizManageFile.setFileType(type);
        bizManageFile.setDelFlag(0);
        bizManageFile.setCreateBy(ShiroUtils.getLoginName());
        bizManageFile.setCreateTime(new Date());
        bizManageFile.setUpdateBy(ShiroUtils.getLoginName());
        bizManageFile.setUpdateTime(new Date());
        int i = bizManageFileService.insertManageFile(bizManageFile);
        return AjaxResult.success(i);
    }

    /**
     * 查询附件列表
     * @param bizManageFile
     * @return
     */
    @PostMapping("list")
    @ResponseBody
    public TableDataInfo listAccessory(BizManageFile bizManageFile) {
        startPage();
        return getDataTable(bizManageFileService.selectManageFileByBizId(bizManageFile));
    }


    /**
     * 删除附件
     * @param id
     * @return
     */
    @PostMapping("delete/{id}")
    @ResponseBody
    public AjaxResult deleteAccessory(@PathVariable("id") Integer id) {
        return AjaxResult.success(bizManageFileService.deleteManageFileById(id));
    }

}
