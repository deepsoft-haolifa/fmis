package com.ruoyi.fmis.dict.service.impl;

import java.util.List;

import com.ruoyi.common.json.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.common.BizConstants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.dict.mapper.BizDictMapper;
import com.ruoyi.fmis.dict.domain.BizDict;
import com.ruoyi.fmis.dict.service.IBizDictService;
import com.ruoyi.common.core.text.Convert;

/**
 * 业务数据字典Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2020-03-11
 */
@Service
public class BizDictServiceImpl implements IBizDictService {
    @Autowired
    private BizDictMapper bizDictMapper;

    /**
     * 查询业务数据字典
     *
     * @param dictId 业务数据字典ID
     * @return 业务数据字典
     */
    @Override
    public BizDict selectBizDictById(Long dictId) {
        return bizDictMapper.selectBizDictById(dictId);
    }

    /**
     * 查询业务数据字典列表
     *
     * @param bizDict 业务数据字典
     * @return 业务数据字典
     */
    @Override
    public List<BizDict> selectBizDictList(BizDict bizDict) {
        return bizDictMapper.selectBizDictList(bizDict);
    }

    /**
     * 新增业务数据字典
     *
     * @param bizDict 业务数据字典
     * @return 结果
     */
    @Override
    public int insertBizDict(BizDict bizDict) {
        bizDict.setCreateTime(DateUtils.getNowDate());
        return bizDictMapper.insertBizDict(bizDict);
    }

    /**
     * 修改业务数据字典
     *
     * @param bizDict 业务数据字典
     * @return 结果
     */
    @Override
    public int updateBizDict(BizDict bizDict) {
        bizDict.setUpdateTime(DateUtils.getNowDate());
        return bizDictMapper.updateBizDict(bizDict);
    }

    /**
     * 删除业务数据字典对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizDictByIds(String ids) {
        return bizDictMapper.deleteBizDictByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除业务数据字典信息
     *
     * @param dictId 业务数据字典ID
     * @return 结果
     */
    @Override
    public int deleteBizDictById(Long dictId) {
        return bizDictMapper.deleteBizDictById(dictId);
    }

    /**
     * 根据code获取对象
     * @param code
     * @return
     */
    @Override
    public BizDict findBizDictByCode (String code) {
        BizDict queryBizDict = new BizDict();
        queryBizDict.setCode(code);
        List<BizDict> list = bizDictMapper.selectBizDictList(queryBizDict);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<BizDict> selectBizDictByProductType(String code) {
        BizDict bizDict = findBizDictByCode(code);
        BizDict queryProductType = new BizDict();
        queryProductType.setParentId(bizDict.getDictId());
        return bizDictMapper.selectBizDictList(queryProductType);
    }

    @Override
    public JSONObject selectProductTypeDict (Long dictId) {
        JSONObject json = new JSONObject();
        //规格
        BizDict queryObject = new BizDict();
        queryObject.setParentId(dictId);
        queryObject.setParentType(BizConstants.specificationCode);
        json.put("specification",bizDictMapper.selectBizDictList(queryObject));

        //公称压力 nominalPressure
        queryObject = new BizDict();
        queryObject.setParentId(dictId);
        queryObject.setParentType(BizConstants.nominalPressure);
        json.put("nominalPressure",bizDictMapper.selectBizDictList(queryObject));
        //连接方式 connectionType
        queryObject = new BizDict();
        queryObject.setParentId(dictId);
        queryObject.setParentType(BizConstants.linkType);
        json.put("linkType",bizDictMapper.selectBizDictList(queryObject));
        //密封材质 sealingMaterial
        queryObject = new BizDict();
        queryObject.setParentId(dictId);
        queryObject.setParentType(BizConstants.sealingMaterial);
        json.put("sealingMaterial",bizDictMapper.selectBizDictList(queryObject));
        //阀芯材质  valveElement
        queryObject = new BizDict();
        queryObject.setParentId(dictId);
        queryObject.setParentType(BizConstants.spoolMaterial);
        json.put("spoolMaterial",bizDictMapper.selectBizDictList(queryObject));
        //驱动形式  driveForm
        queryObject = new BizDict();
        queryObject.setParentId(dictId);
        queryObject.setParentType(BizConstants.driveMode);
        json.put("driveMode",bizDictMapper.selectBizDictList(queryObject));

        //阀体材质
        queryObject = new BizDict();
        queryObject.setParentId(dictId);
        queryObject.setParentType(BizConstants.bodyMaterial);
        json.put("bodyMaterial",bizDictMapper.selectBizDictList(queryObject));

        //结构形式
        queryObject = new BizDict();
        queryObject.setParentId(dictId);
        queryObject.setParentType(BizConstants.structuralType);
        json.put("structuralStyle",bizDictMapper.selectBizDictList(queryObject));


        return json;
    }

}
