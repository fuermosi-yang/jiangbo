package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据groups/gid查询
     * 根据params？cid=？查询
     */


//查询一级
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> findByGidToGroup(@PathVariable("cid") Long gid) {
        //根据gid去查询，结果为集合
        List<SpecGroup> specGroupList = specificationService.findByGidToGroup(gid);
        return ResponseEntity.ok(specGroupList);
    }

    //查询二级
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> findByCidParams(
            @RequestParam(value = "gid", required = false)Long gid,
            @RequestParam(value = "cid", required = false)Long cid,
            @RequestParam(value = "generic", required = false)Boolean generic,
            @RequestParam(value = "searching", required = false)Boolean searching
    ) {
        //根据cid去查询
        List<SpecParam> SpecParamlist = specificationService.findByCidParams(gid, cid, generic, searching);
        return ResponseEntity.ok(SpecParamlist);
    }

    //添加
    @PostMapping("group")
    public void addGroup(@RequestBody SpecGroup group) {
        specificationService.addGroup(group);
    }

    //修改
    @PutMapping("group")
    public void updateGroup(@RequestBody SpecGroup group) {
        specificationService.updateGroup(group);
    }

    //删除
    @DeleteMapping("group/{id}")
    public void deleteById(@PathVariable("id") Long id){
        specificationService.deleteById(id);
    }


    //添加
    @PostMapping("param")
    public void addParam(@RequestBody SpecParam specParam){
        specificationService.addParam(specParam);
    }

    //修改
    @PutMapping("param")
    public  void updateParam(@RequestBody SpecParam specParam){
        specificationService.updateParam(specParam);
    }

    //删除
    @DeleteMapping("param/{id}")
    public void deleteByParamId(@PathVariable("id") Long id){

        specificationService.deleteByParamId(id);
    }

    //根据cid查询查询规格参数组，及组内参数
    @GetMapping("{cid}")
  public  ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> SpecGroupList  = specificationService.querySpecsByCid(cid);
        return ResponseEntity.ok(SpecGroupList);
    };

}
