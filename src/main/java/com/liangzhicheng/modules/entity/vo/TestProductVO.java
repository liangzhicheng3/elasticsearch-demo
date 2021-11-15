package com.liangzhicheng.modules.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TestProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> brandNames;

    private List<String> productCategoryNames;

    private List<ProductAttr> productAttrs;

    @Data
    public static class ProductAttr{

        private Long attrId;

        private String attrName;

        private List<String> attrValues;

    }

}
