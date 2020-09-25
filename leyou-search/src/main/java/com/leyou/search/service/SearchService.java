package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.reponsitory.GoodsReponsitory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SearchService {

    //注入
    @Autowired
    private BrandClient brandClient;
    //注入
    @Autowired
    private CategoryClient categoryClient;
    //注入
    @Autowired
    private GoodsClient goodsClient;
    //注入
    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsReponsitory GoodsReponsitory;

    //注入通用接口ElasticsearchRepository
    @Autowired
    private ElasticsearchRepository elasticsearchRepository;


    private static final ObjectMapper MAPPER = new ObjectMapper();


    public Goods buildGoods(Spu spu) throws IOException {

/**
 *  @Id
 *     private Long id; // spuId
 *     private String all; // 所有需要被搜索的信息，包含标题，分类，甚至品牌
 *     private String subTitle;// 卖点
 *     private Long brandId;// 品牌id
 *     private Long cid1;// 1级分类 idprivate Long cid2;// 2级分类id private Long cid3;// 3级分类id
 *     private Date createTime;// 创建时间
 *     private List<Long> price;// 价格
 *     private String skus;// List<sku>信息的json结构
 *     private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值
 *                                  |
 *                                 |
 *      //首先获取数据              |
 *      获取数据需要的是这些：（对应上面字段行数）
 *     1 spu有 spuid
 *     2 all中拼接的一般是品牌的名字，spu这个系列的title商品标题，以及分类的名字（通过cid1，cid2，cid3去查，分类名对应三个，分别一级分类，二级分类，三级分类）
 *     3 spu有subtitle
 *     4 首先获取brand，根据spu中的spuid
 *     5 spu有cid1 cid2 cid3
 *     6 spu表有创建时间
 *     7 sku表中有价格，可以通过spuid查询到sku集合，获取每个sku的price（价格也是个集合）
 *     8 skus中会有每个商品的名称，以及对应的该商品的多个描述自己的信息，需要在List中存放该商品的价格，商品描述信息格式为key：value，最后将list转mapper
 *     9 最后一个文档中存放一个map集合，key为 param规格参数名，获取param的名字就可以，value为param 中id对应detail表中的通用规格参数和特殊规格参数
 *
 *      //其次封装数据
 */

      //创建一个Goods
        Goods goods = new Goods();

        //获取spuid
        Long id = spu.getId();

        //获取all :品牌，title标题，分类名
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        String title = spu.getTitle();
        List<Long> cids = new ArrayList<>();
        cids.add(spu.getCid1());
        cids.add(spu.getCid2());
        cids.add(spu.getCid3());
        String categoryNames = categoryClient.queryNameByIds(cids);
        //拼接
        String names = title+brand.getName()+categoryNames;

        //获取卖点：
        String subTitle = spu.getSubTitle();

        //获取brandid
        Long brandId = brand.getId();

        //cid1 cid2 cid3
        Long cid1 = spu.getCid1();
        Long cid2 = spu.getCid2();
        Long cid3 = spu.getCid3();

        //创建时间
        Date createTime = spu.getCreateTime();

        //查询skus并遍历
        List<Sku> skus = goodsClient.querySkuList(id);

        //首先new一个list存放价格
       List<Long> prices = new ArrayList<>();

       //在new一个list存放map，map中存放商品字段名称和商品值
       List<Map<String,Object>> skuMapList = new ArrayList<>();
        for (Sku sku : skus) {

            //获取price放入prices中
            Long price = sku.getPrice();
            prices.add(price);

            //new一个map用来存放遍历的信息
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id",sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            //图片有多个就传多个，中间用逗号隔开
            skuMap.put("image", StringUtils.isNotBlank(sku.getImages()) ? StringUtils.split(sku.getImages(), ",")[0] : "");

           //最后将map放入list中
            skuMapList.add(skuMap);
        }

        //转换为json格式
        String newSkus = MAPPER.writeValueAsString(skuMapList);

        //查询搜索param规格参数
        List<SpecParam>  params = specificationClient.findByCidParams(null, spu.getCid3(), null, true);


        // 查询spuDetail。获取规格参数值
        SpuDetail spuDetail = goodsClient.querySpuDetail(id);



        // 获取通用的规格参数
        Map<Long, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        // 获取特殊的规格参数
        Map<Long, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<Object>>>() {
        });
        // 定义map接收{规格参数名，规格参数值}
        Map<String, Object> paramMap = new HashMap<>();
        params.forEach(param -> {
            // 判断是否通用规格参数
            if (param.getGeneric()) {
                // 获取通用规格参数值
                String value = genericSpecMap.get(param.getId()).toString();
                // 判断是否是数值类型
                if (param.getNumeric()){
                    // 如果是数值的话，判断该数值落在那个区间
                    value = chooseSegment(value, param);
                }
                // 把参数名和值放入结果集中
                paramMap.put(param.getName(), value);
            } else {
                paramMap.put(param.getName(), specialSpecMap.get(param.getId()));
            }
        });

        //最后将参数设置进去
        // 设置参数
        goods.setId(id);
        goods.setCid1(cid1);
        goods.setCid2(cid2);
        goods.setCid3(cid3);
        goods.setBrandId(brandId);
        goods.setCreateTime(createTime);
        goods.setSubTitle(subTitle);
        goods.setAll(names);
        goods.setPrice(prices);
        goods.setSkus(newSkus);
        goods.setSpecs(paramMap);
        return goods;

    }



//查询区间方法
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }


    public PageResult<Goods> search(SearchRequest request) {
        String key = request.getKey();
        // 判断是否有搜索条件，如果没有，直接返回null。不允许搜索全部商品
        if (StringUtils.isBlank(key)) {
            return null;
        }

        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 1、对key进行全文检索查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", key).operator(Operator.AND));

        // 2、通过sourceFilter设置返回的结果字段,我们只需要id、skus、subTitle
        queryBuilder.withSourceFilter(new FetchSourceFilter(
                new String[]{"id","skus","subTitle"}, null));

        // 3、分页
        // 准备分页参数
        int page = request.getPage();
        int size = request.getSize();
        queryBuilder.withPageable(PageRequest.of(page - 1, size));

        // 4、查询，获取结果
        Page<Goods> pageInfo = this.GoodsReponsitory.search(queryBuilder.build());

        // 封装结果并返回
        return new PageResult<Goods>(pageInfo.getTotalElements(), (long)pageInfo.getTotalPages(), pageInfo.getContent());
    }
}
