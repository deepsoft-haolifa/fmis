package com.ruoyi.fmis.child.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.child.mapper.BizProcessChildMapper;
import com.ruoyi.fmis.child.domain.BizProcessChild;
import com.ruoyi.fmis.child.service.IBizProcessChildService;
import com.ruoyi.common.core.text.Convert;

/**
 * 流程数据字Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2020-05-05
 */
@Service
public class BizProcessChildServiceImpl implements IBizProcessChildService {
    @Autowired
    private BizProcessChildMapper bizProcessChildMapper;

    /**
     * 查询流程数据字
     *
     * @param childId 流程数据字ID
     * @return 流程数据字
     */
    @Override
    public BizProcessChild selectBizProcessChildById(Long childId) {
        return bizProcessChildMapper.selectBizProcessChildById(childId);
    }

    /**
     * 查询流程数据字列表
     *
     * @param bizProcessChild 流程数据字
     * @return 流程数据字
     */
    @Override
    public List<BizProcessChild> selectBizProcessChildList(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizProcessChildList(bizProcessChild);
    }
    /**
     * 查询流程数据字列表
     *
     * @param bizProcessChild 查询存库
     * @return 流程数据字
     */
    @Override
    public List<BizProcessChild> selectBizProcessChildListForKuCun(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizProcessChildListForKuCun(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizProcessChildInventoryList(BizProcessChild bizProcessChild){
        return bizProcessChildMapper.selectBizProcessChildInventoryList(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizChildProductList(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizChildProductList(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizTestChildList(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizTestChildList(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizTestChildHistoryList(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizTestChildHistoryList(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizTestStayChildList(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizTestStayChildList(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizTestProductList(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizTestProductList(bizProcessChild);
    }
    @Override
    public List<BizProcessChild> selectBizTestActuatorList(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizTestActuatorList(bizProcessChild);
    }
    @Override
    public List<BizProcessChild> selectBizTestRef1List(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizTestRef1List(bizProcessChild);
    }
    @Override
    public List<BizProcessChild> selectBizTestRef2List(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizTestRef2List(bizProcessChild);
    }
    @Override
    public List<BizProcessChild> selectBizTestPAList(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizTestPAList(bizProcessChild);
    }


    @Override
    public List<BizProcessChild> selectBizChildActuatorList(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizChildActuatorList(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizChildRef1List(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizChildRef1List(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizChildRef2List(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizChildRef2List(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizChildPAList(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizChildPAList(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizChildPA1List(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizChildPA1List(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizChildPA2List(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizChildPA2List(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizChildPA3List(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizChildPA3List(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizChildPA4List(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizChildPA4List(bizProcessChild);
    }

    @Override
    public List<BizProcessChild> selectBizQuotationProductList(BizProcessChild bizProcessChild) {
        return bizProcessChildMapper.selectBizQuotationProductList(bizProcessChild);
    }
    /**
     * 新增流程数据字
     *
     * @param bizProcessChild 流程数据字
     * @return 结果
     */
    @Override
    public int insertBizProcessChild(BizProcessChild bizProcessChild) {
        bizProcessChild.setCreateTime(DateUtils.getNowDate());
        return bizProcessChildMapper.insertBizProcessChild(bizProcessChild);
    }

    /**
     * 修改流程数据字
     *
     * @param bizProcessChild 流程数据字
     * @return 结果
     */
    @Override
    public int updateBizProcessChild(BizProcessChild bizProcessChild) {
        bizProcessChild.setUpdateTime(DateUtils.getNowDate());
        return bizProcessChildMapper.updateBizProcessChild(bizProcessChild);
    }
    /**
     * 修改流程发货状态
     *
     * @return 结果
     */
    @Override
    public int updateBizProcessChildFH(BizProcessChild bizProcessChild) {
        bizProcessChild.setUpdateTime(DateUtils.getNowDate());
        return bizProcessChildMapper.updateBizProcessChildFH(bizProcessChild);
    }
    /**
     * 删除流程数据字对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizProcessChildByIds(String ids) {
        return bizProcessChildMapper.deleteBizProcessChildByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除流程数据字信息
     *
     * @param childId 流程数据字ID
     * @return 结果
     */
    @Override
    public int deleteBizProcessChildById(Long childId) {
        return bizProcessChildMapper.deleteBizProcessChildById(childId);
    }

    @Override
    public int deleteBizProcessChildByDataIds(String dataIds) {
        return bizProcessChildMapper.deleteBizProcessChildByDataIds(Convert.toStrArray(dataIds));
    }

    @Override
    public double selectPaymentedPrice(String subjectId, String deptId, String startDate, String endDate) {
        return 0;
    }
}
