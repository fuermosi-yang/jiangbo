package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("spu")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> findAllGoods(
                                @RequestParam(value="key",required = false) String key,
                                @RequestParam(value="saleable",required = true) Boolean saleable,
                                @RequestParam(value="page",defaultValue = "1") Integer page,
                                @RequestParam(value="rows",defaultValue = "5") Integer rows
                                ){
   PageResult<SpuBo> slist =   goodsService.findAllGoods(key,saleable,page,rows);
         return ResponseEntity.ok(slist);
    }

    /**
     * 根据spuid查询spu对象
     */

    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = this.goodsService.querySpuById(id);
        if(spu == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spu);
    }

    //根据skuid查询sku对象
    @GetMapping("sku/{id}")
    public ResponseEntity<Sku> queryBySkuId(@PathVariable("id") Long id){
      Sku sku =   goodsService.queryBySkuId(id);
        return ResponseEntity.ok(sku);
    }


}
