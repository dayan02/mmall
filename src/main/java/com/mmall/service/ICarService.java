package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * Created by 大燕 on 2018/12/17.
 */
public interface ICarService {
    //添加到购物车
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
    //更新购物车
    ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);
    //删除购物车产品
    ServerResponse<CartVo> deleteProduct(Integer userId,String  productIds);
    //购物车列表
    ServerResponse<CartVo> list(Integer userId);

}
