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







}
