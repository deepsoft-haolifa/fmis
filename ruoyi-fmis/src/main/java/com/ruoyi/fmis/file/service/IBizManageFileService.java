package com.ruoyi.fmis.file.service;

import java.util.List;

public interface IBizManageFileService {

    int insertManageFile(com.ruoyi.fmis.file.domain.BizManageFile bizManageFile);

    List<com.ruoyi.fmis.file.domain.BizManageFile> selectManageFileByBizId(com.ruoyi.fmis.file.domain.BizManageFile bizManageFile);

    int deleteManageFileById(Integer id);
}
