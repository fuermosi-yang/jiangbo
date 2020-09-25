package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBySearchAll(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        //初始化example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        //根据名字模糊查,不为空为true,进行拼接
        if (StringUtils.isNotBlank(key)) {//和之前的<if text=name!=null and name!=''>一样
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }

        //设置分页条件,利用插件
        PageHelper.startPage(page, rows);

        //添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }

        //将example放入查询
        List<Brand> brandList = brandMapper.selectByExample(example);

        //将brandList封装到pageInfo中
        PageInfo<Brand> brandPageInfo = new PageInfo<>(brandList);


        return new PageResult<>(brandPageInfo.getTotal(), brandPageInfo.getList());
    }

    public void saveBrand(Brand brand, List<Long> cids) {
        //先添加brand
        brandMapper.insertSelective(brand);
        //在添加中间的那张表
        cids.forEach(cid ->
                brandMapper.addMiddenType(cid, brand.getId())

        );

    }


    public List<Category> findByIdCateGory(Long bid) {
        return brandMapper.findByIdCateGory(bid);
    }

    public void updateBrand(Brand brand, List<Long> cids) {
        //修改分类表
        brandMapper.updateByPrimaryKey(brand);
        brandMapper.deleteById(brand.getId());
//        //修改中间表
//        cids.forEach( cid -> brandMapper.updateAndCategory(cid,brand.getId()));
        //在添加中间的那张表
        cids.forEach(cid ->
                brandMapper.addMiddenType(cid, brand.getId())

        );
    }

    public void deleteById(Long id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    public List<Brand> queryByCidToAll(Long cid) {

        return brandMapper.queryByCidToAll(cid);
    }

    public Brand queryBrandById(Long id) {
        return brandMapper.queryToBrand(id);
    }
}
