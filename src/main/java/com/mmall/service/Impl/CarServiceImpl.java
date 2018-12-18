package com.mmall.service.Impl;

import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICarService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 大燕 on 2018/12/17.
 */
@Service("iCarService")
public class CarServiceImpl implements ICarService {

    @Autowired
    private  CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    //添加购物车
    public ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count){
        if (productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectProductByUserIdProductId(userId,productId);
        if (cart == null){
            //产品不在购物车里面，需要新增一个这样的产品记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);//设置新增产品数量
            cartItem.setChecked(Const.cart.CHECKED);//新放入购物车的产品设置为选中状态
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            //把新增的产品记录添加到购物车里面
            cartMapper.insert(cartItem);
        } else{
       //产品已经在购物车存在，现在只需要更改商品数量
            count = cart.getQuantity()+count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
//不过在购物车里面需要有产品数量校验，即数量最少以及和库存相比最大
        }
        CartVo cartVo = this.getProductVoLimit(userId);

          return ServerResponse.createBySuccess(cartVo);
    }


    //更新购物车
    public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectProductByUserIdProductId(userId,productId);
        if (cart != null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        CartVo cartVo = this.getProductVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }


    private CartVo getProductVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        //通过用户id查询出来所有的产品订单
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        //初始化产品总值，使用BigDecimal的String构造器进行初始化，这样保证数据的准确性
        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (CollectionUtils.isNotEmpty(cartList)){
            for (Cart cartItem:cartList) {

                //每一个产品的属性值
             CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());
            //查询购物车产品
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null){
                    //查询不为空，继续组装cartProductVo
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubTitle(product.getSubtitle());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount = 0;
                    //如果产品库存>=用户选中的产品数
                    if (product.getStock() >= cartItem.getQuantity() ){
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.cart.LIMIT_NUM_SUCCESS);
                    }else{
                        //当勾选的产品数量大于产品总库存，即存放产品最大数值
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.cart.LIMIT_NUM_FAIL);
                        //在购物车中更新有效数量
                        Cart cartForQuantity  = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);

                    //计算单个产品总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    //是否勾选
                    cartProductVo.setProductStatus(cartItem.getChecked());
                }

            if (cartItem.getChecked() == Const.cart.CHECKED){
                //如果已经勾选，就把勾选的产品价值添加到产品总价中
                cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
            }
                //把用户以及产品的的信息完成添加到新的包装中
                cartProductVoList.add(cartProductVo);
            }
        }
//使用cartVo进行返回
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckStatus(userId));
        cartVo.setImageHost("ftp.server.http.prefix");
return cartVo;
    }

private boolean getAllCheckStatus(Integer userId){
    if (userId == null){
        return false;
    }else {
        return cartMapper.selectCartProcuctCheckedStatusByUserId(userId) == 0;
        //设定的1为选中，0为未选中，当查找出来未选择的值为0时候，说明全部选中
        // 此时前端的全选就会显示，这部分在后端完成，不用前端进行每个产品查找状态进行判断判断判断
    }
}

}
