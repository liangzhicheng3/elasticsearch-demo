package com.liangzhicheng.modules.repository;

import com.liangzhicheng.modules.document.ElasticProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 泛型1:操作的对象类型
 * 泛型2:主键id的类型
 */
@Repository
public interface IElasticProductRepository extends ElasticsearchRepository<ElasticProduct, String> {
    //可按照jpa方法规范制定高级查询方法

    /**
     * 搜索查询
     * @param name 商品名称
     * @param subTitle 商品标题
     * @param keywords 商品关键字
     * @param page 分页信息
     * @return
     */
    Page<ElasticProduct> findByNameOrSubTitleOrKeywords(String name, String subTitle, String keywords, Pageable page);

}
