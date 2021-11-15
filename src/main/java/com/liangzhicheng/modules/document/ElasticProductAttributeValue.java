package com.liangzhicheng.modules.document;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
public class ElasticProductAttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long productAttributeId;

    //属性值
    @Field(type = FieldType.Keyword, fielddata = true)
    private String value;

    //属性参数：0->规格；1->参数
    private Integer type;

    //属性名称
    @Field(type = FieldType.Keyword, fielddata = true)
    private String name;

}
