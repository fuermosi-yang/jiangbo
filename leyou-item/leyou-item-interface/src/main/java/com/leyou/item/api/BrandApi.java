package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("brand")
public interface BrandApi {

    /**
     *
     * @param id
     * @return 根据id查询品牌
     */
    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id") Long id);

}
