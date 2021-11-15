package com.liangzhicheng.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liangzhicheng.common.query.PageResult;
import com.liangzhicheng.modules.document.ElasticProduct;
import com.liangzhicheng.modules.entity.TestProduct;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IElasticProductService extends IService<TestProduct> {

    Integer saveBatch();

    Boolean saveOrUpdate();

    void deleteBatch(List<Long> ids);

    void delete(Long id);

    List<TestProduct> list();

    ElasticProduct get(Long id);

    PageResult search1(String keyword, Integer pageNo, Integer pageSize);

    PageResult search2(String keyword, Long brandId, Long productCategoryId, Integer pageNo, Integer pageSize, Integer sort);

    /**
     * 根据商品id获取商品名称查询推荐商品
     * @param productId
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageResult search3(Long productId, Integer pageNo, Integer pageSize);

    Object search4(String keyword);

}
