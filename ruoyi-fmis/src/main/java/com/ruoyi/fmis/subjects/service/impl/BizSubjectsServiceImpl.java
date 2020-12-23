package com.ruoyi.fmis.subjects.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.subjects.domain.BizSubjects;
import com.ruoyi.fmis.subjects.mapper.BizSubjectsMapper;
import com.ruoyi.fmis.subjects.service.IBizSubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 费用科目Service业务层处理
 *
 * @author hedong
 * @date 2020-12-20
 */
@Service
public class BizSubjectsServiceImpl implements IBizSubjectsService {
    @Autowired
    private BizSubjectsMapper bizSubjectsMapper;

    /**
     * 查询费用科目
     *
     * @param subjectsId 费用科目ID
     * @return 费用科目
     */
    @Override
    public BizSubjects selectBizSubjectsById(Long subjectsId) {
        return bizSubjectsMapper.selectBizSubjectsById(subjectsId);
    }

    /**
     * 查询费用科目列表
     *
     * @param bizSubjects 费用科目
     * @return 费用科目
     */
    @Override
    public List<BizSubjects> selectBizSubjectsList(BizSubjects bizSubjects) {
        List<BizSubjects> parentList = this.selectBizSubjectsListContainWu();
        Map<Long, String> parentMap = parentList.stream().collect(Collectors.toMap(BizSubjects::getSubjectsId, BizSubjects::getName));
        List<BizSubjects> bizSubjectsList = bizSubjectsMapper.selectBizSubjectsList(bizSubjects);
        bizSubjectsList.stream().forEach(e -> {
            e.setParentName(parentMap.get(e.getParentId()));
        });
        return bizSubjectsList;
    }

    @Override
    public List<BizSubjects> selectBizSubjectsListContainWu() {
        List<BizSubjects> bizSubjects = bizSubjectsMapper.selectBizSubjectsList(new BizSubjects() {{
            setParentId(0L);
        }});
        bizSubjects.add(0, new BizSubjects() {{
            setSubjectsId(0L);
            setName("无");
        }});
        return bizSubjects;
    }

    /**
     * 新增费用科目
     *
     * @param bizSubjects 费用科目
     * @return 结果
     */
    @Override
    public int insertBizSubjects(BizSubjects bizSubjects) {
        bizSubjects.setCreateTime(DateUtils.getNowDate());
        return bizSubjectsMapper.insertBizSubjects(bizSubjects);
    }

    /**
     * 修改费用科目
     *
     * @param bizSubjects 费用科目
     * @return 结果
     */
    @Override
    public int updateBizSubjects(BizSubjects bizSubjects) {
        bizSubjects.setUpdateTime(DateUtils.getNowDate());
        return bizSubjectsMapper.updateBizSubjects(bizSubjects);
    }

    /**
     * 删除费用科目对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizSubjectsByIds(String ids) {
        return bizSubjectsMapper.deleteBizSubjectsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除费用科目信息
     *
     * @param subjectsId 费用科目ID
     * @return 结果
     */
    @Override
    public int deleteBizSubjectsById(Long subjectsId) {
        return bizSubjectsMapper.deleteBizSubjectsById(subjectsId);
    }
}
