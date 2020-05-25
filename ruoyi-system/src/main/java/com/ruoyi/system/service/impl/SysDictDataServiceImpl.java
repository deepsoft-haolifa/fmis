package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.service.ISysDictDataService;
import org.springframework.util.CollectionUtils;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService
{
    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典数据
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData)
    {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 根据字典类型查询字典数据
     * 
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType)
    {
        return dictDataMapper.selectDictDataByType(dictType);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue)
    {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode)
    {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 通过字典ID删除字典数据信息
     * 
     * @param dictCode 字典数据ID
     * @return 结果
     */
    @Override
    public int deleteDictDataById(Long dictCode)
    {
        return dictDataMapper.deleteDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据
     * 
     * @param ids 需要删除的数据
     * @return 结果
     */
    @Override
    public int deleteDictDataByIds(String ids)
    {
        return dictDataMapper.deleteDictDataByIds(Convert.toStrArray(ids));
    }

    /**
     * 新增保存字典数据信息
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData dictData)
    {
        return dictDataMapper.insertDictData(dictData);
    }

    /**
     * 修改保存字典数据信息
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysDictData dictData)
    {
        return dictDataMapper.updateDictData(dictData);
    }

    /**
     * 检测数据字典，如果不存在新建一个
     * @param type
     * @param label
     * @param dictDataMap
     * @param createByName
     * @return
     */
    @Override
    public String saveDictData (String type, String label, Map<String,SysDictData> dictDataMap,String createByName) {
        SysDictData dictData = null;
        if (StringUtils.isNotEmpty(label)) {
            if (dictDataMap.containsKey(label)) {
                dictData = dictDataMap.get(label);
            } else {
                dictData = new SysDictData();
                dictData.setDictSort(new Long(dictDataMap.size() + 1));
                dictData.setDictCode(new Long(dictDataMap.size() + 1));
                dictData.setDictValue(String.valueOf((dictDataMap.size() + 1)));
                dictData.setDictLabel(label);
                dictData.setDictType(type);
                dictData.setStatus("0");
                dictData.setCreateBy(createByName);
                dictData.setCreateTime(new Date());
                this.insertDictData(dictData);
                //dictDataMap = getDictDataMapByCode(type);
                dictDataMap.put(label,dictData);
            }
        } else {
            return "";
        }
        return dictData.getDictValue();
    }

    @Override
    public Map<String,SysDictData> getDictDataMapByCode (String code) {
        Map<String,SysDictData> map = new HashMap<>();
        List<SysDictData> dictDataList = this.selectDictDataByType(code);
        if (!CollectionUtils.isEmpty(dictDataList)) {
            for (SysDictData sysDictData : dictDataList) {
                map.put(sysDictData.getDictLabel(),sysDictData);
            }
        }
        return map;
    }
}
