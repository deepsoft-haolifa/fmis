package com.ruoyi.fmis.file.mapper;

import com.ruoyi.fmis.file.domain.BizAccessory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 附件表
 * @author guojixiang
 * @date 2021/5/31
 */

public interface BizAccessoryMapper {

    int insertBizAccessory(BizAccessory bizAccessory);

    int deleteAccessoryById(@Param("id") int id);

    List<BizAccessory> selectAccessoryByBizId(@Param("bizId") Integer bizId, @Param("fileType") Integer fileType);
}
