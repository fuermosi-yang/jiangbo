package com.leyou.item.mapper;

import com.leyou.item.pojo.Stock;
import org.apache.ibatis.annotations.Delete;
import tk.mybatis.mapper.common.Mapper;

public interface StockMapper extends Mapper<Stock> {
    @Delete("delete from `tb_stock` where sku_id =#{id}")
    void deleteById(Long id);
}
