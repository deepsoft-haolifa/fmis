package com.ruoyi.fmis.subjects.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.subjects.mapper.BizSubjectsMapper;
import com.ruoyi.fmis.subjects.domain.BizSubjects;
import com.ruoyi.fmis.subjects.service.IBizSubjectsService;
import com.ruoyi.common.core.text.Convert;

/**
 * 科目Service业务层处理
 *
 * @author frank
 * @date 2020-06-28
 */
@Service
public class BizSubjectsServiceImpl implements IBizSubjectsService {
    @Autowired
    private BizSubjectsMapper bizSubjectsMapper;

    /**
     * 查询科目
     *
     * @param subjectsId 科目ID
     * @return 科目
     */
    @Override
    public BizSubjects selectBizSubjectsById(Long subjectsId) {
        return bizSubjectsMapper.selectBizSubjectsById(subjectsId);
    }

    /**
     * 查询科目列表
     *
     * @param bizSubjects 科目
     * @return 科目
     */
    @Override
    public List<BizSubjects> selectBizSubjectsList(BizSubjects bizSubjects) {
        return bizSubjectsMapper.selectBizSubjectsList(bizSubjects);
    }

    @Override
    public List<BizSubjects> selectBizSubjectsAll() {
        return bizSubjectsMapper.selectBizSubjectsList(new BizSubjects());
    }

    /**
     * 新增科目
     *
     * @param bizSubjects 科目
     * @return 结果
     */
    @Override
    public int insertBizSubjects(BizSubjects bizSubjects) {
        bizSubjects.setCreateTime(DateUtils.getNowDate());
        return bizSubjectsMapper.insertBizSubjects(bizSubjects);
    }

    /**
     * 修改科目
     *
     * @param bizSubjects 科目
     * @return 结果
     */
    @Override
    public int updateBizSubjects(BizSubjects bizSubjects) {
        bizSubjects.setUpdateTime(DateUtils.getNowDate());
        return bizSubjectsMapper.updateBizSubjects(bizSubjects);
    }

    /**
     * 删除科目对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizSubjectsByIds(String ids) {
        return bizSubjectsMapper.deleteBizSubjectsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除科目信息
     *
     * @param subjectsId 科目ID
     * @return 结果
     */
    @Override
    public int deleteBizSubjectsById(Long subjectsId) {
        return bizSubjectsMapper.deleteBizSubjectsById(subjectsId);
    }
}
