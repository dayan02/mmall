package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 大燕 on 2018/12/17.
 */

@Controller
@RequestMapping("/car/")
public class CarController {

    @Autowired
    private ICarService iCarService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return  iCarService.list(user.getId());
    }


    @RequestMapping("add.do")
    @ResponseBody
    //购物车添加
    public ServerResponse add(HttpSession session,Integer count ,Integer productId){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return  iCarService.add(user.getId(),productId,count);
    }


    @RequestMapping("update.do")
    @ResponseBody
    //购物车更新
    public ServerResponse update(HttpSession session,Integer count ,Integer productId){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return  iCarService.update(user.getId(),productId,count);
    }




    @RequestMapping("delete_product.do")
    @ResponseBody
//购物车删除产品
    public ServerResponse deleteProduct(HttpSession session,String productIds){
        //可能删除多个产品，跟前端默认的是传过来String，使用“，”隔开，后台接收之后再进行类型转换数据输出
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return  iCarService.deleteProduct(user.getId(),productIds);
    }

//单选多选的不同点是传不传productId，传了就是单选，不传就是多选，所以service和mapper都包含productId，在controller层传入的时候进行限制
    //全选
    //全反选

    @RequestMapping("select_all.do")
    @ResponseBody
//购物车产品全选
    public ServerResponse selectAll(HttpSession session){
        //可能删除多个产品，跟前端默认的是传过来String，使用“，”隔开，后台接收之后再进行类型转换数据输出
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return  iCarService.selectOrUnSelect(user.getId(),null,Const.cart.CHECKED);
    }



    @RequestMapping("un_select_all.do")
    @ResponseBody
//购物车产品全选
    public ServerResponse unSelectAll(HttpSession session){
        //可能删除多个产品，跟前端默认的是传过来String，使用“，”隔开，后台接收之后再进行类型转换数据输出
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return  iCarService.selectOrUnSelect(user.getId(),null,Const.cart.UNCHECKED);
    }

    //单选
    //单选反选

    @RequestMapping("select_all.do")
    @ResponseBody
//购物车产品单选
    public ServerResponse select(HttpSession session,Integer productId){
        //可能删除多个产品，跟前端默认的是传过来String，使用“，”隔开，后台接收之后再进行类型转换数据输出
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return  iCarService.selectOrUnSelect(user.getId(),null,Const.cart.CHECKED);
    }

    @RequestMapping("un_select.do")
    @ResponseBody
//购物车产品反单选
    public ServerResponse unSelect(HttpSession session,Integer productId){
        //可能删除多个产品，跟前端默认的是传过来String，使用“，”隔开，后台接收之后再进行类型转换数据输出
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return  iCarService.selectOrUnSelect(user.getId(),productId,Const.cart.UNCHECKED);
    }



    @RequestMapping("get_cart-product_count.do")
    @ResponseBody
    //查询当前购物车产品数量，并放在界面右上角进行展示，如果一个产品有十个，数量就是十个，不按照产品种类计算总数，按照产品总量计算购物车的总数

    public ServerResponse getCartProductCount(HttpSession session){
        //若用户未登录，购物车给出数值0
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null ){
            return ServerResponse.createBySuccess(0);

        }
        return  iCarService.getCarProductCount(user.getId());
    }





}
