package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 大燕 on 2018/12/3.
 */

@Controller
@RequestMapping("/manage/product")
public class ManageProduct {

@Autowired
    private IUserService iUserService;
@Autowired
    private IProductService iProductService;
    //保存商品
    @RequestMapping("save_update.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断是否登录，并强制登录
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，需登录");
        }if (iUserService.checkUserRole(user).isSuccess()){
            //身份确认成功，进行业务处理
            return iProductService.savaOrUpdateProduct(product);
        }else{
            return  ServerResponse.createByErrorMessage("不是管理员，无权限");
        }

    }


    //产品上下架，更新产品销售状态
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session ,Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER) ;
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要登录");
        }
        if (iUserService.checkUserRole(user).isSuccess()){
            //业务处理
return iProductService.setSaleSatus(productId,status);
        }
        return ServerResponse.createByErrorMessage("不是管理员，无权限");
    }


    //获取产品详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session ,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER) ;
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要登录");
        }
        if (iUserService.checkUserRole(user).isSuccess()){
            //业务处理
            return iProductService.manageProductDetail(productId);

        }
        return ServerResponse.createByErrorMessage("不是管理员，无权限");
    }

    @RequestMapping("list.do")
    @ResponseBody
    //pageNum当前页，默认为第一页，pageSize每页多少，默认10
    public ServerResponse getList(HttpSession session , @RequestParam(value = "pageNum",defaultValue = "1" ) int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER) ;
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要登录");
        }
        if (iUserService.checkUserRole(user).isSuccess()){
            //业务处理，分页列表查询显示

            return iProductService.getProductList(pageNum,pageSize);

        }
        return ServerResponse.createByErrorMessage("不是管理员，无权限");
    }




}
