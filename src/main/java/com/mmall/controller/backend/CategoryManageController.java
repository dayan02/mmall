package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 大燕 on 2018/11/26.
 */

@Controller
public class CategoryManageController {
    @Autowired
    private IUserService iuserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId" ,defaultValue = "0") Integer parentId){


    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user==null){
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
    }
        if (iuserService.checkUserRole(user).isSuccess()){
            //是管理员
            //增加处理分类的逻辑
return iCategoryService.addCategory(categoryName,parentId);
        }else{
            return  ServerResponse.createByErrorMessage("无权限处理，需管理员权限");
        }

}

    @RequestMapping("set_category_name.do")
    @ResponseBody
    //更新categoryName的接口
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
           return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (iuserService.checkUserRole(user).isSuccess()){
//更新操作
return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else{
            return ServerResponse.createByErrorMessage("无权限处理，需管理员权限");
        }
    }
}
