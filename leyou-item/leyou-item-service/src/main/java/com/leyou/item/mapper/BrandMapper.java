package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper  extends Mapper<Brand> {
    @Insert("insert into `tb_category_brand`(category_id,brand_id) values(#{cid},#{id})")
    void addMiddenType(@Param("cid") Long cid, @Param("id") Long id);

    @Select("SELECT * FROM tb_category WHERE id IN \n" +
            "(SELECT category_id FROM tb_category_brand WHERE brand_id = #{bid})")
    List<Category> findByIdCateGory(Long bid);

//    @Update("update  tb_category_brand set category_id=#{cid},brand_id=#{id})")
//    void updateAndCategory(Long cid, Long id);

    @Delete("DELETE FROM tb_category_brand where brand_id=#{id}")
    void deleteById(Long id);


    @Select("SELECT b.* FROM tb_brand b, tb_category c,tb_category_brand cb\n" +
            "WHERE c.`id`=cb.`category_id` AND b.`id` = cb.`brand_id`  AND cb.`category_id`=76")
    List<Brand> queryByCidToAll(Long cid);

    @Select("select * from tb_brand where id = #{id}")
    Brand queryToBrand(Long id);
}
