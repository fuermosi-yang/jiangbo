package com.leyou.item.service;

import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpuDetailService {

    //注入
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    //根据id查询detail
    public SpuDetail querySpuDetail(Long id) {
        return spuDetailMapper.selectByPrimaryKey(id);
    }
}
