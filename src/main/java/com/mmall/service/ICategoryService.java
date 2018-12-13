package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * Created by 大燕 on 2018/11/26.
 */
public interface ICategoryService {
    //添加品类名称
    ServerResponse addCategory(String categoryName, Integer parentId);
    //更改品类名称
    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    //子类及其平级查询
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
//递归查询
    ServerResponse<List<Integer>>  selectCategoryAndChildrenCategory(Integer categoryId);
}
