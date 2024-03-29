package com.ruoyi.fmis.steststay.service;

import com.ruoyi.fmis.steststay.domain.BizDataSteststay;
import java.util.List;

/**
 * 待质检Service接口
 *
 * @author frank
 * @date 2020-07-26
 */
public interface IBizDataSteststayService {
    /**
     * 查询待质检
     *
     * @param stayId 待质检ID
     * @return 待质检
     */
    public BizDataSteststay selectBizDataSteststayById(Long stayId);

    public Long selectMaxNo();

    /**
     * 查询待质检列表
     *
     * @param bizDataSteststay 待质检
     * @return 待质检集合
     */
    public List<BizDataSteststay> selectBizDataSteststayList(BizDataSteststay bizDataSteststay);

    /**
     * 新增待质检
     *
     * @param bizDataSteststay 待质检
     * @return 结果
     */
    public int insertBizDataSteststay(BizDataSteststay bizDataSteststay);

    /**
     * 修改待质检
     *
     * @param bizDataSteststay 待质检
     * @return 结果
     */
    public int updateBizDataSteststay(BizDataSteststay bizDataSteststay);

    /**
     * 批量删除待质检
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizDataSteststayByIds(String ids);

    /**
     * 删除待质检信息
     *
     * @param stayId 待质检ID
     * @return 结果
     */
    public int deleteBizDataSteststayById(Long stayId);
}
