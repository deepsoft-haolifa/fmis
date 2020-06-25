package com.ruoyi.fmis.stestn.service;

import com.ruoyi.fmis.stestn.domain.BizDataStestn;
import java.util.List;

/**
 * 采购检测数量Service接口
 *
 * @author frank
 * @date 2020-06-24
 */
public interface IBizDataStestnService {
    /**
     * 查询采购检测数量
     *
     * @param testId 采购检测数量ID
     * @return 采购检测数量
     */
    public BizDataStestn selectBizDataStestnById(Long testId);

    /**
     * 查询采购检测数量列表
     *
     * @param bizDataStestn 采购检测数量
     * @return 采购检测数量集合
     */
    public List<BizDataStestn> selectBizDataStestnList(BizDataStestn bizDataStestn);

    /**
     * 新增采购检测数量
     *
     * @param bizDataStestn 采购检测数量
     * @return 结果
     */
    public int insertBizDataStestn(BizDataStestn bizDataStestn);

    /**
     * 修改采购检测数量
     *
     * @param bizDataStestn 采购检测数量
     * @return 结果
     */
    public int updateBizDataStestn(BizDataStestn bizDataStestn);

    /**
     * 批量删除采购检测数量
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizDataStestnByIds(String ids);

    /**
     * 删除采购检测数量信息
     *
     * @param testId 采购检测数量ID
     * @return 结果
     */
    public int deleteBizDataStestnById(Long testId);
}
