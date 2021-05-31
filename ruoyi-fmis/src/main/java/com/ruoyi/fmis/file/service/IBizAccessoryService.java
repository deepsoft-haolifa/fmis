package com.ruoyi.fmis.file.service;

import java.util.List;

public interface IBizAccessoryService {

    int insertBizAccessory(com.ruoyi.fmis.file.domain.BizAccessory bizAccessory);

    List<com.ruoyi.fmis.file.domain.BizAccessory> selectBizAccessoryByBizId(com.ruoyi.fmis.file.domain.BizAccessory bizAccessory);

    int deleteBizAccessoryById(Integer id);
}
