package com.leyou.item.mapper;

import com.leyou.item.pojo.Spu;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface SpuMapper extends Mapper<Spu> {
    @Update("update tb_spu set valid=false where id=#{id}")
    void updateById(Spu spu);
    @Update("update tb_spu set saleable=#{saleable} where id=#{id}")
    void updateByStatus(Spu spu);
}