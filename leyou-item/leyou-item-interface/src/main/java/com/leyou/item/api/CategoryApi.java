package com.leyou.item.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {

    //根据品牌id查询品牌名称
    @GetMapping("names")
    String queryNameByIds(@RequestParam("ids") List<Long> ids);
}
