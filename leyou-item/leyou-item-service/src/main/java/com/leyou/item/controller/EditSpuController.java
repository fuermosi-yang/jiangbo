package com.leyou.item.controller;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.SkuListService;
import com.leyou.item.service.SpuDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EditSpuController {

    @Autowired
    private SpuDetailService spuDetailService;

    @Autowired
    private SkuListService skuListService;
    /*
      oldGoods.spuDetail = await this.$http.loadData("/item/spu/detail/" + oldGoods.id);
        oldGoods.skus = await this.$http.loadData("/item/sku/list?id=" + oldGoods.id);
     */
//根据id查询detail
    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetail(@PathVariable("id") Long id){
        return ResponseEntity.ok(spuDetailService.querySpuDetail(id));
    }


    //根据id查询list
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuList(@RequestParam("id") Long id){
        List<Sku> skus = skuListService.querySkuList(id);
        return ResponseEntity.ok(skus);

    }

}
