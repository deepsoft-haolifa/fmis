package com.ruoyi.fmis.file.service.impl;

import com.ruoyi.fmis.file.domain.BizManageFile;
import com.ruoyi.fmis.file.mapper.BizManageFileMapper;
import com.ruoyi.fmis.file.service.IBizManageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IBizManageFileServiceImpl implements IBizManageFileService {

    @Autowired
    private BizManageFileMapper bizManageFileMapper;

    /**
     * 添加附件
     * @param bizManageFile
     * @return
     */
    @Override
    public int insertManageFile(com.ruoyi.fmis.file.domain.BizManageFile bizManageFile) {
        return bizManageFileMapper.insertManageFile(bizManageFile);
    }

    /**
     * 查询附件列表
     * @param bizManageFile
     * @return
     */
    @Override
    public List<com.ruoyi.fmis.file.domain.BizManageFile> selectManageFileByBizId(com.ruoyi.fmis.file.domain.BizManageFile bizManageFile) {
        return bizManageFileMapper.selectManageFileByBizType(bizManageFile);
    }

    /**
     * 删除附件
     * @param id
     * @return
     */
    @Override
    public int deleteManageFileById(Integer id) {
        return bizManageFileMapper.deleteManageFileById(id);
    }
}
