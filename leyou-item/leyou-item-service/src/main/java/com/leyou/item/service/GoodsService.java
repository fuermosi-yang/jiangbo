package com.leyou.item.service;

import ch.qos.logback.core.util.TimeUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<SpuBo> findAllGoods(String key, Boolean saleable, Integer page, Integer rows) {

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

//分页
        PageHelper.startPage(page, rows);

        //模糊查
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }

        //判断是否为null
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        //查询spu商品
        List<Spu> spus = spuMapper.selectByExample(example);
        //放入pageinfo里
        PageInfo<Spu> plist = new PageInfo<Spu>(spus);

        List<SpuBo> spuBoArrayList = new ArrayList<SpuBo>();
        //遍历
        spus.forEach(spu -> {
            //赋值到新Bo实体类
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);

            //根据cid1查询一级分类名称
            String name1 = categoryMapper.selectByPrimaryKey(spuBo.getCid1()).getName();
            String name2 = categoryMapper.selectByPrimaryKey(spuBo.getCid2()).getName();
            String name3 = categoryMapper.selectByPrimaryKey(spuBo.getCid3()).getName();

            spuBo.setCname(name1 + "-" + name2 + "-" + name3);

            //根据brand_id查询品牌名
            String bname = brandMapper.selectByPrimaryKey(spuBo.getBrandId()).getName();
            spuBo.setBname(bname);

            //存入新建的集合中
            spuBoArrayList.add(spuBo);
        });

        return new PageResult<SpuBo>(plist.getTotal(), spuBoArrayList);
    }

    //根据spuid查询spu对象
    public Spu querySpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }

    @Autowired
    private SkuMapper skuMapper;

    //根据skuid查询sku对象
    public Sku queryBySkuId(Long id) {
        return skuMapper.selectByPrimaryKey(id);
    }
}
