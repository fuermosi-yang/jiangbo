package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {


    @Autowired
    private BrandService brandService;
    //根据条件查询
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBySearchAll(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc

    ) {
      PageResult<Brand>  brandList = brandService.queryBySearchAll(key,page,rows,sortBy,desc);
        return ResponseEntity.ok(brandList);
    }

    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id") Long id){
      Brand brand =   brandService.queryBrandById(id);
        return brand;
    };
}
