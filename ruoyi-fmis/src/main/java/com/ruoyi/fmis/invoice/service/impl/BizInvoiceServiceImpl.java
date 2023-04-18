package com.ruoyi.fmis.invoice.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.fmis.data.domain.BizProcessData;
import com.ruoyi.fmis.data.service.IBizProcessDataService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.invoice.mapper.BizInvoiceMapper;
import com.ruoyi.fmis.invoice.bean.BizInvoice;
import com.ruoyi.fmis.invoice.service.IBizInvoiceService;
import com.ruoyi.common.core.text.Convert;

import javax.annotation.Resource;

/**
 * 发票信息（进项发票）Service业务层处理
 *
 * @author hedong
 * @date 2021-05-22
 */
@Service
@Slf4j
public class BizInvoiceServiceImpl implements IBizInvoiceService {
    @Autowired
    private BizInvoiceMapper bizInvoiceMapper;
    @Resource
    private IBizProcessDataService bizProcessDataService;


    /**
     * 查询发票信息（进项发票）
     *
     * @param id 发票信息（进项发票）ID
     * @return 发票信息（进项发票）
     */
    @Override
    public BizInvoice selectBizInvoiceById(Long id) {
        return bizInvoiceMapper.selectBizInvoiceById(id);
    }

    /**
     * 查询发票信息（进项发票）列表
     *
     * @param bizInvoice 发票信息（进项发票）
     * @return 发票信息（进项发票）
     */
    @Override
    public List<BizInvoice> selectBizInvoiceList(BizInvoice bizInvoice) {
        return bizInvoiceMapper.selectBizInvoiceList(bizInvoice);
    }

    /**
     * 新增发票信息（进项发票）
     *
     * @param bizInvoice 发票信息（进项发票）
     * @return 结果
     */
    @Override
    public AjaxResult insertBizInvoice(BizInvoice bizInvoice) {
        bizInvoice.setCreateTime(DateUtils.getNowDate());

        BizProcessData bizProcessData = new BizProcessData();
        bizProcessData.setBizId("procurement");
        bizProcessData.setString12(bizInvoice.getContractNo());
        List<BizProcessData> list = bizProcessDataService.selectBizProcessDataListRefProcurement(bizProcessData);
        Double contractAmount = list.get(0).getPrice1();

        BizInvoice biz =  new BizInvoice();
        // 做合同金额个发票金额是否相等  如果相等则不需添加，不相等的话 可以添加 直至相等修改合同状态
        biz.setContractNo(bizInvoice.getContractNo());
        List<BizInvoice> bizInvoices = bizInvoiceMapper.selectBizInvoiceList(biz);
        if (CollectionUtils.isEmpty(bizInvoices)) {
            if (bizInvoice.getAmount() > contractAmount) {
                return AjaxResult.error("发票金额大于合同金额请重新修改");
            }
            if (bizInvoice.getAmount() == contractAmount) {
                bizProcessData.setStatus("5");
                bizProcessDataService.updateBizProcessData(bizProcessData);
            }
            bizInvoiceMapper.insertBizInvoice(bizInvoice);
        } else {
            // 发票已有总金额
            Double amount = bizInvoices.stream().reduce(0.0, (re, invoice) -> re + invoice.getAmount(), Double::sum);
            if (amount == contractAmount) {
                return AjaxResult.error("发票金额已全部回票不需要再新增发票");
            }
            // 发票已有金额 + 新录入金额
            double v = amount + bizInvoice.getAmount();
            if (v > contractAmount) {
                return AjaxResult.error("发票总金额已超过合同金额，请重新修改");
            }
            if (v == contractAmount) {
                bizInvoiceMapper.insertBizInvoice(bizInvoice);
                bizProcessData.setStatus("5");
                bizProcessDataService.updateBizProcessDataByBizIdAndString12(bizProcessData);
            } else {
                bizInvoiceMapper.insertBizInvoice(bizInvoice);
            }
        }
        return AjaxResult.success();
    }

    /**
     * 修改发票信息（进项发票）
     *
     * @param bizInvoice 发票信息（进项发票）
     * @return 结果
     */
    @Override
    public int updateBizInvoice(BizInvoice bizInvoice) {
        bizInvoice.setUpdateTime(DateUtils.getNowDate());
        return bizInvoiceMapper.updateBizInvoice(bizInvoice);
    }

    /**
     * 删除发票信息（进项发票）对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizInvoiceByIds(String ids) {
        return bizInvoiceMapper.deleteBizInvoiceByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除发票信息（进项发票）信息
     *
     * @param id 发票信息（进项发票）ID
     * @return 结果
     */
    @Override
    public int deleteBizInvoiceById(Long id) {
        return bizInvoiceMapper.deleteBizInvoiceById(id);
    }

    @Override
    public String importList(List<BizInvoice> list, Boolean isUpdateSupport) {
        if (StringUtils.isNull(list) || list.size() == 0) {
            throw new BusinessException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (BizInvoice bizInvoice : list) {
            bizInvoice.setCreateBy(String.valueOf(ShiroUtils.getSysUser().getUserId()));
            bizInvoice.setCreateTime(DateUtils.getNowDate());
            bizInvoiceMapper.insertBizInvoice(bizInvoice);
        }
        return "导入成功";


//        try {
//            // 验证是否存在这个用户
//            SysUser u = bizInvoiceMapper.selectBizInvoiceList(user.getLoginName());
//            if (StringUtils.isNull(u)) {
//                user.setPassword(Md5Utils.hash(user.getLoginName() + password));
//                user.setCreateBy(operName);
//                this.insertUser(user);
//                successNum++;
//                successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 导入成功");
//            } else if (isUpdateSupport) {
//                user.setUpdateBy(operName);
//                this.updateUser(user);
//                successNum++;
//                successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 更新成功");
//            } else {
//                failureNum++;
//                failureMsg.append("<br/>" + failureNum + "、账号 " + user.getLoginName() + " 已存在");
//            }
//        } catch (Exception e) {
//            failureNum++;
//            String msg = "<br/>" + failureNum + "、账号 " + user.getLoginName() + " 导入失败：";
//            failureMsg.append(msg + e.getMessage());
//            log.error(msg, e);
//        }
//        if (failureNum > 0) {
//            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
//            throw new BusinessException(failureMsg.toString());
//        } else {
//            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
//        }
//        return successMsg.toString();
    }
}
