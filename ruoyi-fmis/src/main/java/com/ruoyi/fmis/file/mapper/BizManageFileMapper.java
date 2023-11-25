package com.ruoyi.fmis.file.mapper;

import com.ruoyi.fmis.file.domain.BizAccessory;
import com.ruoyi.fmis.file.domain.BizManageFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 附件表
 * @author guojixiang
 * @date 2021/5/31
 */

public interface BizManageFileMapper {

    int insertManageFile(BizManageFile bizManageFile);

    int deleteManageFileById(@Param("id") int id);

    List<BizManageFile> selectManageFileByBizType(BizManageFile bizManageFile);
}
