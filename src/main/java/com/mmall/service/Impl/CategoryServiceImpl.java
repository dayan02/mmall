package com.mmall.service.Impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 大燕 on 2018/11/26.
 */
@Service("iCategoryService")
public class CategoryServiceImpl {

@Autowired
private CategoryMapper categoryMapper;

    /**添加商品品类
     * @param categoryName
     * @param parentId
     * @return
     */
    public ServerResponse addCategory(String categoryName,Integer parentId){
        if (parentId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount  = categoryMapper.insertSelective(category);
        if (rowCount>0){
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }


    /**
     * 更新品类名称
     * @param categoryId
     * @param categoryName
     * @return
     */
    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
if (categoryId==null||StringUtils.isBlank("categoryName")){
    return ServerResponse.createByErrorMessage("品类参数错误");
}
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount>0){
            return ServerResponse.createBySuccessMessage("更新品类名称成功");
        }else{}
        return ServerResponse.createByErrorMessage("更新品类名称失败");

    }
}
