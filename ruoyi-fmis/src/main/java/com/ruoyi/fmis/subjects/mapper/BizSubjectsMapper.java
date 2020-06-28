package com.ruoyi.fmis.subjects.mapper;

import com.ruoyi.fmis.subjects.domain.BizSubjects;
import java.util.List;

/**
 * 科目Mapper接口
 *
 * @author frank
 * @date 2020-06-28
 */
public interface BizSubjectsMapper {
    /**
     * 查询科目
     *
     * @param subjectsId 科目ID
     * @return 科目
     */
    public BizSubjects selectBizSubjectsById(Long subjectsId);

    /**
     * 查询科目列表
     *
     * @param bizSubjects 科目
     * @return 科目集合
     */
    public List<BizSubjects> selectBizSubjectsList(BizSubjects bizSubjects);

    /**
     * 新增科目
     *
     * @param bizSubjects 科目
     * @return 结果
     */
    public int insertBizSubjects(BizSubjects bizSubjects);

    /**
     * 修改科目
     *
     * @param bizSubjects 科目
     * @return 结果
     */
    public int updateBizSubjects(BizSubjects bizSubjects);

    /**
     * 删除科目
     *
     * @param subjectsId 科目ID
     * @return 结果
     */
    public int deleteBizSubjectsById(Long subjectsId);

    /**
     * 批量删除科目
     *
     * @param subjectsIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizSubjectsByIds(String[] subjectsIds);
}
