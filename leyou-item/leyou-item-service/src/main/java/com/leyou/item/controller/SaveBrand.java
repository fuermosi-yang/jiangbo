package com.leyou.item.controller;

import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class SaveBrand {

    @Autowired
    private BrandService brandService;
    /**
     * 新增的品牌有：
     * @brand
     * @cids
     */
    //保存brand

//添加
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
//修改
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        brandService.updateBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
//删除
    @RequestMapping("bid")
    public ResponseEntity<Void> deleteById(Long id){
        brandService.deleteById(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //添加商品-根据cid查询数据
    @GetMapping("cid/{cid}")
    public  ResponseEntity<List<Brand>> queryByCidToAll(@PathVariable("cid") Long cid){
     List<Brand>  brandList =   brandService.queryByCidToAll(cid);
        return ResponseEntity.ok(brandList);
    }

}
