package com.mmall.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by 大燕 on 2018/11/26.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private CategoryMapper categoryMapper;
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);


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

    /**
     *  子节点及其平级category信息查询
     * @param categoryId
     * @return
     */

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
List<Category> list = categoryMapper.selectChildrenById(categoryId);
        //判断是不是空集合以及集合中的元素是否为空
        if (CollectionUtils.isEmpty(list)){
    logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(list);
    }


    /**
     * 递归查询
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>>  selectCategoryAndChildrenCategory(Integer categoryId){
               Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

        List<Integer> list = Lists.newArrayList();
        if (categoryId!=null){
            for (Category categoryItem:categorySet){
                list.add(categoryItem.getId());
            }

        }
return ServerResponse.createBySuccess(list);
    }
    //递归查询方法实现
    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categpryId){
        Category category = categoryMapper.selectByPrimaryKey(categpryId);
        if (categpryId!=null){
            categorySet.add(category);
        }
        //查找子节点，以及递归退出
        List<Category> list = categoryMapper.selectChildrenById(categpryId);
            for(Category categoryItems:list){

    findChildCategory(categorySet,categoryItems.getId());
        }
        return categorySet;
    }
}
