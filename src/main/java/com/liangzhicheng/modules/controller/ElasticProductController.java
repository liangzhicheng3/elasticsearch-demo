package com.liangzhicheng.modules.controller;

import com.liangzhicheng.common.basic.ResponseResult;
import com.liangzhicheng.modules.service.IElasticProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/elastic/product")
public class ElasticProductController {

    @Resource
    private IElasticProductService elasticProductService;

    @GetMapping(value = "/save/batch")
    public ResponseResult saveBatch(){
        return ResponseResult.success(elasticProductService.saveBatch());
    }

    @GetMapping(value = "/saveOrUpdate")
    public ResponseResult saveOrUpdate(){
        return ResponseResult.success(elasticProductService.saveOrUpdate());
    }

    @GetMapping(value = "/delete/batch")
    public ResponseResult deleteBatch(@RequestParam("ids") List<Long> ids){
        elasticProductService.deleteBatch(ids);
        return ResponseResult.success();
    }

    @GetMapping(value = "/delete/{id}")
    public ResponseResult delete(@PathVariable("id") Long id){
        elasticProductService.delete(id);
        return ResponseResult.success();
    }

    @GetMapping(value = "/list")
    public ResponseResult list(){
        return ResponseResult.success(elasticProductService.list());
    }

    @GetMapping(value = "/get/{id}")
    public ResponseResult get(@PathVariable("id") Long id){
        return ResponseResult.success(elasticProductService.get(id));
    }

    @GetMapping(value = "/search1")
    public ResponseResult search1(@RequestParam(required = false) String keyword,
                                  @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                  @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return ResponseResult.success(elasticProductService.search1(keyword, pageNo, pageSize));
    }

    @PostMapping(value = "/search2")
    public ResponseResult search2(@RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) Long brandId,
                                  @RequestParam(required = false) Long productCategoryId,
                                  @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                  @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                  @RequestParam(required = false, defaultValue = "0") Integer sort) {
        return ResponseResult.success(elasticProductService.search2(keyword, brandId, productCategoryId, pageNo, pageSize, sort));
    }

    @PostMapping(value = "/search3")
    public ResponseResult search3(@RequestParam(required = false) Long productId,
                                  @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                  @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return ResponseResult.success(elasticProductService.search3(productId, pageNo, pageSize));
    }

    @PostMapping(value = "/search4")
    public ResponseResult search4(@RequestParam(required = false) String keyword) {
        return ResponseResult.success(elasticProductService.search4(keyword));
    }

}
