package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;


    /**
     * 根据父id查询子节点
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam("pid") Long pid) {
        List<Category> categories = categoryService.queryCategoriesByPid(pid);
        return ResponseEntity.ok(categories);
    }

      //修改
    @GetMapping("bid/{id}")
    public ResponseEntity<List<Category>> findByIdCateGory(@PathVariable("id") Long id){
        List<Category> blist = brandService.findByIdCateGory(id);
        return ResponseEntity.ok(blist);
    }

    //根据品牌id查询品牌名称
    @GetMapping("names")
  public  String queryNameByIds(@RequestParam("ids") List<Long> ids){
     String names =  categoryService.queryNameByIds(ids);
        return names;
    };
}
