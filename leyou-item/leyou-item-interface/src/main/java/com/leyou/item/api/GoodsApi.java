package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface GoodsApi {

    /**
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return 查询商品分页查询
     */
    @GetMapping("spu/page")
     PageResult<SpuBo> findAllGoods(
            @RequestParam(value="key",required = false) String key,
            @RequestParam(value="saleable",required = true) Boolean saleable,
            @RequestParam(value="page",defaultValue = "1") Integer page,
            @RequestParam(value="rows",defaultValue = "5") Integer rows
    );

    //根据spu商品id查询详情
    @GetMapping("spu/detail/{id}")
     SpuDetail querySpuDetail(@PathVariable("id") Long id);

    /**
     * 根据spuid查询sku
     */

    @GetMapping("sku/list")
   List<Sku> querySkuList(@RequestParam("id") Long id);

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
     Spu querySpuById(@PathVariable("id") Long id);

    //根据skuid查询sku对象
    @GetMapping("sku/{id}")
     Sku queryBySkuId(@PathVariable("id") Long id);
}
