package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * Created by 大燕 on 2018/11/26.
 */
public interface ICategoryService {
    //添加品类名称
    ServerResponse addCategory(String categoryName, Integer parentId);
    //更改品类名称
    ServerResponse updateCategoryName(Integer categoryId,String categoryName);
}
