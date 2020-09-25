package com.leyou.item.service;

import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpuBoService {

    //添加商品

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SpuMapper spuMapper;

    /**
     * 新增商品
     * @param spuBo
     */
    @Transactional
    public void addGoodsAll(SpuBo spuBo) {
        // 新增spu
        // 设置默认字段
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);

        // 新增spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);

        saveSkuAndStock(spuBo);
    }

    private void saveSkuAndStock(SpuBo spuBo) {
        spuBo.getSkus().forEach(sku -> {
            // 新增sku
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            // 新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }


    //修改
    public void updateGoodsAll(SpuBo spuBo) {


        //先删除spu，根据id删
        spuMapper.deleteByPrimaryKey(spuBo.getId());
        //在添加一遍
        //先添加spu
        Spu spu = new Spu();
        spu.setId(spuBo.getId());
        spu.setTitle(spuBo.getTitle());
        spu.setSubTitle(spuBo.getSubTitle());
        spu.setCid1(spuBo.getCid1());
        spu.setCid2(spuBo.getCid2());
        spu.setCid3(spuBo.getCid3());
        spu.setBrandId(spuBo.getBrandId());
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());
        spuMapper.insert(spu);

        //删除spudetail表这行数据
        spuDetailMapper.deleteByPrimaryKey(spuBo.getId());
        //在添加spudetail
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(spuBo.getSpuDetail().getSpuId());
        spuDetail.setDescription(spuBo.getSpuDetail().getDescription());
        spuDetail.setGenericSpec(spuBo.getSpuDetail().getGenericSpec());
        spuDetail.setSpecialSpec(spuBo.getSpuDetail().getSpecialSpec());
        spuDetail.setPackingList(spuBo.getSpuDetail().getPackingList());
        spuDetail.setAfterService(spuBo.getSpuDetail().getAfterService());
        spuDetailMapper.insert(spuDetail);

     //调用方法操作sku相关
        updateSkuAll(spuBo);
    }


    public void  updateSkuAll(SpuBo spuBo){


        //遍历删除
        spuBo.getSkus().forEach(sku -> {

            //先删除sku数据，但是查询一下比较好
            //新建一个sku，存放id方便查询
            Sku sku1 = new Sku();
            //赋值
            sku1.setSpuId(spuBo.getId());
            sku1.setStock(sku.getStock());
            //根据id去查
            Sku sku2 = skuMapper.selectOne(sku1);

            skuMapper.delete(sku2);
            //在添加
            Sku newSku = new Sku();
            newSku.setId(sku2.getId());
            newSku.setSpuId(sku2.getSpuId());
            newSku.setTitle(sku.getTitle());
            newSku.setImages(sku.getImages());
            newSku.setPrice(sku.getPrice());
            newSku.setIndexes(sku.getIndexes());
            newSku.setOwnSpec(sku.getOwnSpec());
            newSku.setEnable(sku.getEnable());
            newSku.setCreateTime(new Date());
            newSku.setLastUpdateTime(new Date());
            //添加,返回自增主键
            skuMapper.insert(newSku);


            //根据skuid删除在添加

            stockMapper.deleteById(sku2.getId());
            //添加stock
            Stock stock = new Stock();
            stock.setSkuId(sku2.getId());
            stock.setStock(sku.getStock());
            stockMapper.insertSelective(stock);


        });

    }

    public void deleteById(Long id) {
        //删除spu表,但是不是真的删，修改状态而已
        Spu spu = new Spu();
        spu.setValid(false);
        spu.setId(id);
        spuMapper.updateById(spu);


        //删除sku表，但是不是真的删，修改状态而已
        //先查
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skuList = skuMapper.select(sku);
        skuList.forEach(s->{
            s.setEnable(false);
            skuMapper.updateByPrimaryKey(s);
        });


    }

    public void updateById(Long id) {

        //修改spu表,修改状态而已
        Spu spu = new Spu();
        //查询
        Spu spu1 = spuMapper.selectByPrimaryKey(id);
        Boolean flag = spu1.getSaleable();
        if(flag){
            //为true，改变成false
            spu.setSaleable(false);
        }else{
            //为false，改变成true
            spu.setSaleable(true);
        }
        spu.setId(id);
        spuMapper.updateByStatus(spu);



    }
}
