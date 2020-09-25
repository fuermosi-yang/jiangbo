package com.leyou.item.service;


import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;


    public List<SpecGroup> findByGidToGroup(Long gid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(gid);
        return specGroupMapper.select(specGroup);
    }

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecParam> findByCidParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        specParam.setCid(cid);
        return specParamMapper.select(specParam);
    }

    //添加
    public void addGroup(SpecGroup specGroup) {
        specGroupMapper.insert(specGroup);
    }

    //修改
    public void updateGroup(SpecGroup group) {
        specGroupMapper.updateByPrimaryKey(group);
    }

    //删除
    public void deleteById(Long id) {
        specGroupMapper.deleteByPrimaryKey(id);
    }


    public void addParam(SpecParam specParam) {
        specParamMapper.insert(specParam);
    }

    public void updateParam(SpecParam specParam) {
        specParamMapper.updateByPrimaryKey(specParam);
    }

    public void deleteByParamId(Long id) {
        specParamMapper.deleteByPrimaryKey(id);
    }

    public List<SpecGroup> querySpecsByCid(Long cid) {
        // 查询规格组
        List<SpecGroup> groups = this.findByGidToGroup(cid);
        groups.forEach(g -> {
            // 查询组内参数,根据gorup表id，查询字段param的gid
            g.setParams(this.findByCidParams(g.getId(), null, null, null));
        });
        return groups;
    }
}
