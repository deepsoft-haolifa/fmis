package com.ruoyi.fmis.file.service.impl;

import com.ruoyi.fmis.file.mapper.BizAccessoryMapper;
import com.ruoyi.fmis.file.service.IBizAccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IBizAccessoryServiceImpl implements IBizAccessoryService {

    @Autowired
    private BizAccessoryMapper bizAccessoryMapper;

    /**
     * 添加附件
     * @param bizAccessory
     * @return
     */
    @Override
    public int insertBizAccessory(com.ruoyi.fmis.file.domain.BizAccessory bizAccessory) {
        return bizAccessoryMapper.insertBizAccessory(bizAccessory);
    }

    /**
     * 查询附件列表
     * @param bizAccessory
     * @return
     */
    @Override
    public List<com.ruoyi.fmis.file.domain.BizAccessory> selectBizAccessoryByBizId(com.ruoyi.fmis.file.domain.BizAccessory bizAccessory) {
        return bizAccessoryMapper.selectAccessoryByBizId(bizAccessory.getBizId(),bizAccessory.getFileType());
    }

    /**
     * 删除附件
     * @param id
     * @return
     */
    @Override
    public int deleteBizAccessoryById(Integer id) {
        return bizAccessoryMapper.deleteAccessoryById(id);
    }
}
