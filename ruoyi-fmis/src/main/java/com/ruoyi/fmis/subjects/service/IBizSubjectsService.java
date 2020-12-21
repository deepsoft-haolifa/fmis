package com.ruoyi.fmis.subjects.service;

import com.ruoyi.fmis.subjects.domain.BizSubjects;

import java.util.List;

/**
 * 费用科目Service接口
 *
 * @author hedong
 * @date 2020-12-20
 */
public interface IBizSubjectsService {
    /**
     * 查询费用科目
     *
     * @param subjectsId 费用科目ID
     * @return 费用科目
     */
    public BizSubjects selectBizSubjectsById(Long subjectsId);

    /**
     * 查询费用科目列表
     *
     * @param bizSubjects 费用科目
     * @return 费用科目集合
     */
    public List<BizSubjects> selectBizSubjectsList(BizSubjects bizSubjects);

    /**
     * 新增费用科目
     *
     * @param bizSubjects 费用科目
     * @return 结果
     */
    public int insertBizSubjects(BizSubjects bizSubjects);

    /**
     * 修改费用科目
     *
     * @param bizSubjects 费用科目
     * @return 结果
     */
    public int updateBizSubjects(BizSubjects bizSubjects);

    /**
     * 批量删除费用科目
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizSubjectsByIds(String ids);

    /**
     * 删除费用科目信息
     *
     * @param subjectsId 费用科目ID
     * @return 结果
     */
    public int deleteBizSubjectsById(Long subjectsId);
}
