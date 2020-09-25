package com.leyou.item.controller;

import com.leyou.item.bo.SpuBo;
import com.leyou.item.service.GoodsService;
import com.leyou.item.service.SpuBoService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("goods")
public class saveGoods {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SpuBoService spuBoService;


    @PostMapping
    public ResponseEntity<Void> addGoodsAll(@RequestBody SpuBo spuBo){
        spuBoService.addGoodsAll(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public  ResponseEntity<Void> updateGoodsAll(@RequestBody SpuBo spuBo){
        spuBoService.updateGoodsAll(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //根据id删除
    @DeleteMapping("deleteById/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable("id") Long id){
        spuBoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //根据id修改状态
    @PutMapping("updateById/{id}")
    public  ResponseEntity<Void> updateById(@PathVariable("id") Long id){
        spuBoService.updateById(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
