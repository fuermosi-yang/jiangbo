package com.leyou.item.service;

import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SkuListService {

    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;

    public List<Sku> querySkuList(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        //根据spuid查询sku集合
        List<Sku> skuList = skuMapper.select(sku);
        //顺便把库存也填上
        skuList.forEach(s -> {
            Stock stock = new Stock();
            stock.setSkuId(s.getId());
            //查询stock
            Stock stock1 = stockMapper.selectOne(stock);
            //将库存放入sku中
            s.setStock(stock1.getStock());
        });


        return skuList;
    }
}
