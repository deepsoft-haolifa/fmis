package com.ruoyi.fmis.stestn.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.stestn.mapper.BizDataStestnMapper;
import com.ruoyi.fmis.stestn.domain.BizDataStestn;
import com.ruoyi.fmis.stestn.service.IBizDataStestnService;
import com.ruoyi.common.core.text.Convert;

/**
 * 采购检测数量Service业务层处理
 *
 * @author frank
 * @date 2020-06-24
 */
@Service
public class BizDataStestnServiceImpl implements IBizDataStestnService {
    @Autowired
    private BizDataStestnMapper bizDataStestnMapper;

    /**
     * 查询采购检测数量
     *
     * @param testId 采购检测数量ID
     * @return 采购检测数量
     */
    @Override
    public BizDataStestn selectBizDataStestnById(Long testId) {
        return bizDataStestnMapper.selectBizDataStestnById(testId);
    }

    /**
     * 查询采购检测数量列表
     *
     * @param bizDataStestn 采购检测数量
     * @return 采购检测数量
     */
    @Override
    public List<BizDataStestn> selectBizDataStestnList(BizDataStestn bizDataStestn) {
        return bizDataStestnMapper.selectBizDataStestnList(bizDataStestn);
    }

    /**
     * 新增采购检测数量
     *
     * @param bizDataStestn 采购检测数量
     * @return 结果
     */
    @Override
    public int insertBizDataStestn(BizDataStestn bizDataStestn) {
        bizDataStestn.setCreateTime(DateUtils.getNowDate());
        return bizDataStestnMapper.insertBizDataStestn(bizDataStestn);
    }

    /**
     * 修改采购检测数量
     *
     * @param bizDataStestn 采购检测数量
     * @return 结果
     */
    @Override
    public int updateBizDataStestn(BizDataStestn bizDataStestn) {
        bizDataStestn.setUpdateTime(DateUtils.getNowDate());
        return bizDataStestnMapper.updateBizDataStestn(bizDataStestn);
    }

    /**
     * 删除采购检测数量对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizDataStestnByIds(String ids) {
        return bizDataStestnMapper.deleteBizDataStestnByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除采购检测数量信息
     *
     * @param testId 采购检测数量ID
     * @return 结果
     */
    @Override
    public int deleteBizDataStestnById(Long testId) {
        return bizDataStestnMapper.deleteBizDataStestnById(testId);
    }
}
