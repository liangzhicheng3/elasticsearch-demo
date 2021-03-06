package com.liangzhicheng.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liangzhicheng.common.query.PageResult;
import com.liangzhicheng.common.utils.SysBeanUtil;
import com.liangzhicheng.modules.document.ElasticProduct;
import com.liangzhicheng.modules.entity.TestProduct;
import com.liangzhicheng.modules.entity.vo.TestProductVO;
import com.liangzhicheng.modules.mapper.ITestProductMapper;
import com.liangzhicheng.modules.repository.IElasticProductRepository;
import com.liangzhicheng.modules.service.IElasticProductService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ElasticsearchTemplate:??????????????????????????????Elasticsearch????????????
 * ElasticsearchRepository:?????????????????????????????????????????????????????????
 *
 * NativeSearchQueryBuilder:????????????????????????????????????,?????????????????????????????????
 * QueryBuilder:????????????????????????,?????????????????????QueryBuilders?????????????????????????????????????????????
 *    boolQuery():??????bool??????,????????? "bool": { }
 *    matchQuery():??????match??????,????????? "match": { }
 *    rangeQuery()?????????range??????,????????? "range": { }
 * AbstractAggregationBuilder:????????????????????????????????????,???????????????AggregationBuilders???????????????
 * Pageable:??????????????????,????????????PageRequest.of(????????????, ????????????)??????
 * SortBuilder:???????????????,????????????SortBuilders.fieldSort(??????).order(??????)??????
 */
@Slf4j
@Service
public class ElasticProductServiceImpl extends ServiceImpl<ITestProductMapper, TestProduct> implements IElasticProductService {

    @Resource
    private IElasticProductRepository elasticProductRepository;
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public Integer saveBatch() {
        List<ElasticProduct> productList = baseMapper.listProduct(null);
        Iterable<ElasticProduct> elasticProduct =
                elasticProductRepository.saveAll(SysBeanUtil.copyList(productList, ElasticProduct.class));
        Iterator<ElasticProduct> iterator = elasticProduct.iterator();
        Integer num = 0;
        while(iterator.hasNext()){
            num++;
            iterator.next();
        }
        return num;
    }

    @Override
    public Boolean saveOrUpdate() {
        //TODO ?????????????????????????????????
        //??????Elasticsearch?????????????????????????????????????????????????????????????????????id??????????????????????????????
        ElasticProduct elasticProduct = new ElasticProduct();
        elasticProduct.setId(1L);
        elasticProduct.setName("????????????");
        elasticProductRepository.save(elasticProduct);
        return null;
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        List<ElasticProduct> productList = new ArrayList<>(ids.size());
        for(Iterator<Long> it = ids.iterator(); it.hasNext();){
            ElasticProduct elasticProduct = new ElasticProduct();
            elasticProduct.setId(it.next());
            productList.add(elasticProduct);
        }
        elasticProductRepository.deleteAll(productList);
    }

    @Override
    public void delete(Long id) {
        elasticProductRepository.deleteById(String.valueOf(id));
    }

    @Override
    public List<TestProduct> list() {
        return baseMapper.selectList(new LambdaQueryWrapper<TestProduct>());
    }

    @Override
    public ElasticProduct get(Long id) {
        Optional<ElasticProduct> elasticProduct = elasticProductRepository.findById(String.valueOf(id));
        return elasticProduct.get();
    }

    @Override
    public PageResult search1(String keyword, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        /**
         * findByNameOrSubTitleOrKeywords???ElasticsearchRepository?????????????????????findBy...??????????????????
         */
        Page<ElasticProduct> page = elasticProductRepository.findByNameOrSubTitleOrKeywords(keyword, keyword, keyword, pageable);
        return new PageResult<>(page.getNumber(), page.getSize(), page.getContent(), (int) page.getTotalElements());
    }

    @Override
    public PageResult search2(String keyword, Long brandId, Long productCategoryId, Integer pageNo, Integer pageSize, Integer sort) {
        //??????????????????
        NativeSearchQueryBuilder resultQuery = new NativeSearchQueryBuilder();
        //????????????
        if (StringUtils.isEmpty(keyword)) {
            resultQuery.withQuery(QueryBuilders.matchAllQuery());
        } else {
            List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterQuery = new ArrayList<>();
            filterQuery.add(
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            QueryBuilders.matchQuery("name", keyword),
                            ScoreFunctionBuilders.weightFactorFunction(10)
                    )
            );
            filterQuery.add(
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            QueryBuilders.matchQuery("subTitle", keyword),
                            ScoreFunctionBuilders.weightFactorFunction(5)
                    )
            );
            filterQuery.add(
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            QueryBuilders.matchQuery("keywords", keyword),
                            ScoreFunctionBuilders.weightFactorFunction(2)
                    )
            );
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] builder =
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterQuery.size()];
            filterQuery.toArray(builder);
            FunctionScoreQueryBuilder resultBuilder =
                    QueryBuilders.functionScoreQuery(builder)
                            .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                            .setMinScore(2);
            resultQuery.withQuery(resultBuilder);
        }
        //??????????????????
        if(brandId != null || productCategoryId != null){
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (brandId != null) {
                boolQuery.must(QueryBuilders.termQuery("brandId", brandId));
            }
            if (productCategoryId != null) {
                boolQuery.must(QueryBuilders.termQuery("productCategoryId", productCategoryId));
            }
            resultQuery.withFilter(boolQuery);
        }
        //????????????
        resultQuery.withPageable(PageRequest.of(pageNo, pageSize));
        //????????????
        switch(sort){
            case 1:
                resultQuery.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC)); //?????????????????????
                break;
            case 2:
                resultQuery.withSort(SortBuilders.fieldSort("sale").order(SortOrder.DESC)); //?????????????????????
                break;
            case 3:
                resultQuery.withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC)); //?????????????????????
                break;
            case 4:
                resultQuery.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC)); //?????????????????????
                break;
            default:
                resultQuery.withSort(SortBuilders.scoreSort().order(SortOrder.DESC)); //????????????
                break;
        }
        NativeSearchQuery searchQuery = resultQuery.build();
        log.info("search2 result???{}", searchQuery.getQuery());
        SearchHits<ElasticProduct> searchHits = elasticsearchRestTemplate.search(searchQuery, ElasticProduct.class);
        if(searchHits != null && (int) searchHits.getTotalHits() > 0){
            return new PageResult<>(pageNo, pageSize,
                    searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList()),
                    (int) searchHits.getTotalHits());
        }
        return new PageResult<>(pageNo, pageSize, new ArrayList<>(), 0);
    }

    @Override
    public PageResult search3(Long productId, Integer pageNo, Integer pageSize) {
        TestProduct testProduct = baseMapper.selectById(productId);
        if(testProduct != null){
            Long brandId = testProduct.getBrandId();
            Long productCategoryId = testProduct.getProductCategoryId();
            String keyword = testProduct.getName();
            List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterQuery = new ArrayList<>();
            filterQuery.add(
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            QueryBuilders.matchQuery("name", keyword),
                            ScoreFunctionBuilders.weightFactorFunction(8)
                    )
            );
            filterQuery.add(
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            QueryBuilders.matchQuery("subTitle", keyword),
                            ScoreFunctionBuilders.weightFactorFunction(2)
                    )
            );
            filterQuery.add(
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            QueryBuilders.matchQuery("keywords", keyword),
                            ScoreFunctionBuilders.weightFactorFunction(2)
                    )
            );
            filterQuery.add(
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            QueryBuilders.matchQuery("brandId", brandId),
                            ScoreFunctionBuilders.weightFactorFunction(5)
                    )
            );
            filterQuery.add(
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            QueryBuilders.matchQuery("productCategoryId", productCategoryId),
                            ScoreFunctionBuilders.weightFactorFunction(3)
                    )
            );
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] builder =
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterQuery.size()];
            filterQuery.toArray(builder);
            FunctionScoreQueryBuilder resultBuilder = QueryBuilders.functionScoreQuery(builder)
                    .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                    .setMinScore(2);
            //??????????????????
            BoolQueryBuilder boolQuery = new BoolQueryBuilder();
            boolQuery.mustNot(QueryBuilders.termQuery("id", productId));
            //??????????????????
            NativeSearchQueryBuilder resultQuery = new NativeSearchQueryBuilder();
            resultQuery.withQuery(resultBuilder);
            resultQuery.withFilter(boolQuery);
            resultQuery.withPageable(PageRequest.of(pageNo, pageSize));
            NativeSearchQuery searchQuery = resultQuery.build();
            log.info("search2 result???{}", searchQuery.getQuery());
            SearchHits<ElasticProduct> searchHits = elasticsearchRestTemplate.search(searchQuery, ElasticProduct.class);
            if(searchHits != null && (int) searchHits.getTotalHits() > 0){
                return new PageResult<>(pageNo, pageSize,
                        searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList()),
                        (int) searchHits.getTotalHits());
            }
        }
        return new PageResult<>(pageNo, pageSize, new ArrayList<>(), 0);
    }

    @Override
    public TestProductVO search4(String keyword) {
        //??????????????????
        NativeSearchQueryBuilder resultQuery = new NativeSearchQueryBuilder();
        if(StringUtils.isEmpty(keyword)){
            resultQuery.withQuery(QueryBuilders.matchAllQuery());
        }else{
            resultQuery.withQuery(QueryBuilders.multiMatchQuery(keyword, "name", "subTitle", "keywords"));
        }
        //????????????????????????
        resultQuery.addAggregation(AggregationBuilders.terms("brandNames").field("brandName"));
        //????????????????????????
        resultQuery.addAggregation(AggregationBuilders.terms("productCategoryNames").field("productCategoryName"));
        //?????????????????????????????????type = 1????????????type???????????????0->?????????1->?????????
        AbstractAggregationBuilder aggregationBuilder = AggregationBuilders.nested("allAttrValues", "attrValueList")
                .subAggregation(AggregationBuilders.filter("productAttrs", QueryBuilders.termQuery("attrValueList.type", 1))
                        .subAggregation(AggregationBuilders.terms("attrIds").field("attrValueList.productAttributeId")
                                        .subAggregation(AggregationBuilders.terms("attrValues").field("attrValueList.value"))
                                        .subAggregation(AggregationBuilders.terms("attrNames").field("attrValueList.name"))
                        )
                );
        resultQuery.addAggregation(aggregationBuilder);
        NativeSearchQuery searchQuery = resultQuery.build();
        SearchHits<ElasticProduct> searchHits = elasticsearchRestTemplate.search(searchQuery, ElasticProduct.class);
        return this.toObject(searchHits);
    }

    /**
     * ??????????????????????????????
     * @param searchHits
     * @return TestProductVO
     */
    private TestProductVO toObject(SearchHits<ElasticProduct> searchHits) {
        if(searchHits != null && searchHits.getTotalHits() > 0){
            TestProductVO productVO = new TestProductVO();
            Map<String, Aggregation> aggregationMap = searchHits.getAggregations().getAsMap();
            //????????????
            Aggregation brandNames = aggregationMap.get("brandNames");
            List<? extends Terms.Bucket> bucketsBrand = ((Terms) brandNames).getBuckets();
            List<String> brandNameList = new ArrayList<>(bucketsBrand.size());
            for(int i = 0; i < bucketsBrand.size(); i++){
                brandNameList.add(bucketsBrand.get(i).getKeyAsString());
            }
            productVO.setBrandNames(brandNameList);
            //????????????
            Aggregation productCategoryNames = aggregationMap.get("productCategoryNames");
            List<? extends Terms.Bucket> bucketsCategory = ((Terms) productCategoryNames).getBuckets();
            List<String> productCategoryNameList = new ArrayList<>(bucketsCategory.size());
            for(int i = 0; i < bucketsCategory.size(); i++){
                productCategoryNameList.add(bucketsCategory.get(i).getKeyAsString());
            }
            productVO.setProductCategoryNames(productCategoryNameList);
            //????????????
            Aggregation productAttrs = aggregationMap.get("allAttrValues");
            List<? extends Terms.Bucket> attrIds = (
                    (ParsedLongTerms) (
                            (ParsedFilter) (
                                    (ParsedNested) productAttrs
                            ).getAggregations().get("productAttrs")
                    ).getAggregations().get("attrIds")
            ).getBuckets();
            List<TestProductVO.ProductAttr> attrList = new ArrayList<>(attrIds.size());
            for (Terms.Bucket attrId : attrIds) {
                TestProductVO.ProductAttr attr = new TestProductVO.ProductAttr();
                attr.setAttrId((Long) attrId.getKey());
                List<? extends Terms.Bucket> attrValues = ((ParsedStringTerms) attrId.getAggregations().get("attrValues")).getBuckets();
                List<? extends Terms.Bucket> attrNames = ((ParsedStringTerms) attrId.getAggregations().get("attrNames")).getBuckets();
                List<String> attrValueList = new ArrayList<>(attrValues.size());
                for (Terms.Bucket attrValue : attrValues) {
                    attrValueList.add(attrValue.getKeyAsString());
                }
                attr.setAttrValues(attrValueList);
                if(!CollectionUtils.isEmpty(attrNames)){
                    String attrName = attrNames.get(0).getKeyAsString();
                    attr.setAttrName(attrName);
                }
                attrList.add(attr);
            }
            productVO.setProductAttrs(attrList);
            return productVO;
        }
        return null;
    }

}
