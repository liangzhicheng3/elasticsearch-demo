package com.liangzhicheng.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liangzhicheng.modules.document.ElasticProduct;
import com.liangzhicheng.modules.entity.TestProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ITestProductMapper extends BaseMapper<TestProduct> {

    List<ElasticProduct> listProduct(@Param("id") Long id);

}
