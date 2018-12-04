package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetaiVo;

/**
 * Created by 大燕 on 2018/12/3.
 */
public interface IProductService {
    //保存或更新
    ServerResponse savaOrUpdateProduct(Product product);product
//更改商品销售状态
    ServerResponse<String> setSaleSatus(Integer productId,Integer status);
    //获取商品信息
    ServerResponse<ProductDetaiVo> manageProductDetail(Integer productId);
}
