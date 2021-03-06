package com.leyou.search.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    //搜索商品
  @PostMapping("page")
    public ResponseEntity<PageResult<Goods>>  search(@RequestBody SearchRequest request){
      PageResult<Goods> pagelist =    searchService.search(request);
      return ResponseEntity.ok(pagelist);
  }
}
