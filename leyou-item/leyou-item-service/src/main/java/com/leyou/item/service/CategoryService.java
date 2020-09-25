package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 根据parentId查询子类目
     * @param pid
     * @return
     */
    public List<Category> queryCategoriesByPid(Long pid) {
        Category t = new Category();
        t.setParentId(pid);
        return categoryMapper.select(t);
    }

    public String queryNameByIds(List<Long> ids) {
        int count = 1;
        String names = "";
        for (Long id : ids) {
            //根据cid1查询一级分类名称
            if(count==1){
                String name1 = categoryMapper.selectByPrimaryKey(id).getName();
                names+=name1;
            }else if(count==2){

                String name2 = categoryMapper.selectByPrimaryKey(id).getName();
                names+=" "+name2;
            }else{
                String name3 = categoryMapper.selectByPrimaryKey(id).getName();
                names+=" "+name3;
            }
            count++;
        }

        return names;
    }
}
