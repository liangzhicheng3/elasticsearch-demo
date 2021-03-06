package com.liangzhicheng.modules.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 关系型数据库          Elasticsearch
 * Database(数据库)     Indices(索引)
 * Tables(表) 	       Types(类型)
 * Rows(行) 	       Documents(文档)
 * Columns(列) 	       Fields(字段)
 */
@Data
@Document(indexName = "test_product", type = "test_product", shards = 1, replicas = 0) //配置对应的索引和类型
public class ElasticProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id //文档的id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String productSn;

    private Long brandId;

    @Field(type = FieldType.Keyword, fielddata = true)
    private String brandName;

    private Long productCategoryId;

    @Field(type = FieldType.Keyword, fielddata = true)
    private String productCategoryName;

    private String pic;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String subTitle;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String keywords;

    private BigDecimal price;
    private Integer sale;
    private Integer newStatus;
    private Integer recommandStatus;
    private Integer stock;
    private Integer promotionType;
    private Integer sort;

    @Field(type = FieldType.Nested)
    private List<ElasticProductAttributeValue> attrValueList;

}
