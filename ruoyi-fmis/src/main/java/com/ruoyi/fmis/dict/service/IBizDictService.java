package com.ruoyi.fmis.dict.service;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.fmis.dict.domain.BizDict;
import java.util.List;

/**
 * 业务数据字典Service接口
 *
 * @author Xianlu Tech
 * @date 2020-03-11
 */
public interface IBizDictService {
    /**
     * 查询业务数据字典
     *
     * @param dictId 业务数据字典ID
     * @return 业务数据字典
     */
    public BizDict selectBizDictById(Long dictId);

    /**
     * 查询业务数据字典列表
     *
     * @param bizDict 业务数据字典
     * @return 业务数据字典集合
     */
    public List<BizDict> selectBizDictList(BizDict bizDict);

    /**
     * 新增业务数据字典
     *
     * @param bizDict 业务数据字典
     * @return 结果
     */
    public int insertBizDict(BizDict bizDict);

    /**
     * 修改业务数据字典
     *
     * @param bizDict 业务数据字典
     * @return 结果
     */
    public int updateBizDict(BizDict bizDict);

    /**
     * 批量删除业务数据字典
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizDictByIds(String ids);

    /**
     * 删除业务数据字典信息
     *
     * @param dictId 业务数据字典ID
     * @return 结果
     */
    public int deleteBizDictById(Long dictId);
    /**
     * 根据code获取对象
     * @param code
     * @return
     */
    public BizDict findBizDictByCode (String code);

    /**
     * 获取产品系列字典
     * @param code
     * @return
     */
    public List<BizDict> selectBizDictByProductType(String code);

    /**
     * 根据id获取产品系列下的所有字典 例如 规格 阀体材质等
     * @param dictId
     * @return
     */
    public JSONObject selectProductTypeDict (Long dictId);

    /**
     * 根据类型获取字典 去重
     * @param type
     * @return
     */
    public List<BizDict> selectProductDictForParentType (String type,Long dictId);
}
