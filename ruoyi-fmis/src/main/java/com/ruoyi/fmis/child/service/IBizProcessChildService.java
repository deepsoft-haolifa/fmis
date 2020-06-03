package com.ruoyi.fmis.child.service;

import com.ruoyi.fmis.child.domain.BizProcessChild;
import java.util.List;

/**
 * 流程数据字Service接口
 *
 * @author Xianlu Tech
 * @date 2020-05-05
 */
public interface IBizProcessChildService {
    /**
     * 查询流程数据字
     *
     * @param childId 流程数据字ID
     * @return 流程数据字
     */
    public BizProcessChild selectBizProcessChildById(Long childId);

    /**
     * 查询流程数据字列表
     *
     * @param bizProcessChild 流程数据字
     * @return 流程数据字集合
     */
    public List<BizProcessChild> selectBizProcessChildList(BizProcessChild bizProcessChild);


    public List<BizProcessChild> selectBizQuotationProductList(BizProcessChild bizProcessChild);
    /**
     * 新增流程数据字
     *
     * @param bizProcessChild 流程数据字
     * @return 结果
     */
    public int insertBizProcessChild(BizProcessChild bizProcessChild);

    /**
     * 修改流程数据字
     *
     * @param bizProcessChild 流程数据字
     * @return 结果
     */
    public int updateBizProcessChild(BizProcessChild bizProcessChild);

    /**
     * 批量删除流程数据字
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizProcessChildByIds(String ids);

    /**
     * 删除流程数据字信息
     *
     * @param childId 流程数据字ID
     * @return 结果
     */
    public int deleteBizProcessChildById(Long childId);
}
