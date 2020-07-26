package com.ruoyi.fmis.steststay.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.steststay.mapper.BizDataSteststayMapper;
import com.ruoyi.fmis.steststay.domain.BizDataSteststay;
import com.ruoyi.fmis.steststay.service.IBizDataSteststayService;
import com.ruoyi.common.core.text.Convert;

/**
 * 待质检Service业务层处理
 *
 * @author frank
 * @date 2020-07-26
 */
@Service
public class BizDataSteststayServiceImpl implements IBizDataSteststayService {
    @Autowired
    private BizDataSteststayMapper bizDataSteststayMapper;

    /**
     * 查询待质检
     *
     * @param stayId 待质检ID
     * @return 待质检
     */
    @Override
    public BizDataSteststay selectBizDataSteststayById(Long stayId) {
        return bizDataSteststayMapper.selectBizDataSteststayById(stayId);
    }

    /**
     * 查询待质检列表
     *
     * @param bizDataSteststay 待质检
     * @return 待质检
     */
    @Override
    public List<BizDataSteststay> selectBizDataSteststayList(BizDataSteststay bizDataSteststay) {
        return bizDataSteststayMapper.selectBizDataSteststayList(bizDataSteststay);
    }

    /**
     * 新增待质检
     *
     * @param bizDataSteststay 待质检
     * @return 结果
     */
    @Override
    public int insertBizDataSteststay(BizDataSteststay bizDataSteststay) {
        bizDataSteststay.setCreateTime(DateUtils.getNowDate());
        return bizDataSteststayMapper.insertBizDataSteststay(bizDataSteststay);
    }

    /**
     * 修改待质检
     *
     * @param bizDataSteststay 待质检
     * @return 结果
     */
    @Override
    public int updateBizDataSteststay(BizDataSteststay bizDataSteststay) {
        bizDataSteststay.setUpdateTime(DateUtils.getNowDate());
        return bizDataSteststayMapper.updateBizDataSteststay(bizDataSteststay);
    }

    /**
     * 删除待质检对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizDataSteststayByIds(String ids) {
        return bizDataSteststayMapper.deleteBizDataSteststayByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除待质检信息
     *
     * @param stayId 待质检ID
     * @return 结果
     */
    @Override
    public int deleteBizDataSteststayById(Long stayId) {
        return bizDataSteststayMapper.deleteBizDataSteststayById(stayId);
    }
}
