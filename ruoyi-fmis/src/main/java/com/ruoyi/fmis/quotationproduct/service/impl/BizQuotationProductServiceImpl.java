package com.ruoyi.fmis.quotationproduct.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fmis.actuator.domain.BizActuator;
import com.ruoyi.fmis.actuator.mapper.BizActuatorMapper;
import com.ruoyi.fmis.product.domain.BizProduct;
import com.ruoyi.fmis.product.mapper.BizProductMapper;
import com.ruoyi.fmis.productref.domain.BizProductRef;
import com.ruoyi.fmis.productref.mapper.BizProductRefMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fmis.quotationproduct.mapper.BizQuotationProductMapper;
import com.ruoyi.fmis.quotationproduct.domain.BizQuotationProduct;
import com.ruoyi.fmis.quotationproduct.service.IBizQuotationProductService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.util.CollectionUtils;

/**
 * 报价单产品Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2020-03-21
 */
@Service
public class BizQuotationProductServiceImpl implements IBizQuotationProductService {
    @Autowired
    private BizQuotationProductMapper bizQuotationProductMapper;

    @Autowired
    private BizProductMapper bizProductMapper;

    @Autowired
    private BizProductRefMapper bizProductRefMapper;

    @Autowired
    private BizActuatorMapper bizActuatorMapper;
    /**
     * 查询报价单产品
     *
     * @param qpId 报价单产品ID
     * @return 报价单产品
     */
    @Override
    public BizQuotationProduct selectBizQuotationProductById(Long qpId) {
        return bizQuotationProductMapper.selectBizQuotationProductById(qpId);
    }

    /**
     * 查询报价单产品列表
     *
     * @param bizQuotationProduct 报价单产品
     * @return 报价单产品
     */
    @Override
    public List<BizQuotationProduct> selectBizQuotationProductList(BizQuotationProduct bizQuotationProduct) {
        return bizQuotationProductMapper.selectBizQuotationProductList(bizQuotationProduct);
    }

    @Override
    public List<BizQuotationProduct> selectBizQuotationProductDictList(BizQuotationProduct bizQuotationProduct) {
        List<BizQuotationProduct> bizQuotationProducts = bizQuotationProductMapper.selectBizQuotationProductDictList(bizQuotationProduct);

        for (BizQuotationProduct bizQuotationProduct1 : bizQuotationProducts) {
            BizProduct bizProduct = new BizProduct();
            bizProduct.setProductId(bizQuotationProduct1.getProductId());
            List<BizProduct> bizProducts = bizProductMapper.selectBizProductList(bizProduct);
            if (!CollectionUtils.isEmpty(bizProducts)) {
                bizQuotationProduct1.setBizProduct(bizProducts.get(0));
            } else {
                bizQuotationProduct1.setBizProduct(new BizProduct());
            }

            Long ref1Id = bizQuotationProduct1.getProductRef1Id();
            if (ref1Id != null && ref1Id > 0) {
                BizProductRef bizProductRef1 = new BizProductRef();
                bizProductRef1.setProductRefId(ref1Id);
                List<BizProductRef> bizProductRefs = bizProductRefMapper.selectBizProductRefList(bizProductRef1);
                if (!CollectionUtils.isEmpty(bizProductRefs)) {
                    bizQuotationProduct1.setBizProductRef1(bizProductRefs.get(0));
                } else {
                    bizQuotationProduct1.setBizProductRef1(bizProductRef1);
                }
            }

            Long ref2Id = bizQuotationProduct1.getProductRef2Id();
            if (ref2Id != null && ref2Id > 0) {
                BizProductRef bizProductRef2 = new BizProductRef();
                bizProductRef2.setProductRefId(ref2Id);
                List<BizProductRef> bizProductRefs = bizProductRefMapper.selectBizProductRefList(bizProductRef2);
                if (!CollectionUtils.isEmpty(bizProductRefs)) {
                    bizQuotationProduct1.setBizProductRef2(bizProductRefs.get(0));
                } else {
                    bizQuotationProduct1.setBizProductRef2(bizProductRef2);
                }
            }

            Long actuatorId = bizQuotationProduct1.getActuatorId();
            if (actuatorId != null && actuatorId > 0) {
                BizActuator bizActuator = new BizActuator();
                bizActuator.setActuatorId(actuatorId);
                List<BizActuator> bizActuators = bizActuatorMapper.selectBizActuatorForRefList(bizActuator);
                if (!CollectionUtils.isEmpty(bizActuators)) {
                    bizQuotationProduct1.setBizActuator(bizActuators.get(0));
                } else {
                    bizQuotationProduct1.setBizActuator(bizActuator);
                }
            }

        }

        return bizQuotationProducts;
    }

    /**
     * 新增报价单产品
     *
     * @param bizQuotationProduct 报价单产品
     * @return 结果
     */
    @Override
    public int insertBizQuotationProduct(BizQuotationProduct bizQuotationProduct) {
        bizQuotationProduct.setCreateTime(DateUtils.getNowDate());
        return bizQuotationProductMapper.insertBizQuotationProduct(bizQuotationProduct);
    }

    /**
     * 修改报价单产品
     *
     * @param bizQuotationProduct 报价单产品
     * @return 结果
     */
    @Override
    public int updateBizQuotationProduct(BizQuotationProduct bizQuotationProduct) {
        bizQuotationProduct.setUpdateTime(DateUtils.getNowDate());
        return bizQuotationProductMapper.updateBizQuotationProduct(bizQuotationProduct);
    }

    /**
     * 删除报价单产品对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizQuotationProductByIds(String ids) {
        return bizQuotationProductMapper.deleteBizQuotationProductByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除报价单产品信息
     *
     * @param qpId 报价单产品ID
     * @return 结果
     */
    @Override
    public int deleteBizQuotationProductById(Long qpId) {
        return bizQuotationProductMapper.deleteBizQuotationProductById(qpId);
    }
}
