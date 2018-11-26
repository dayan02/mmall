package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 大燕 on 2018/11/20.
 * 后台管理员
 */
@RequestMapping("/manager/user")
@Controller
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/user_login" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> user_login(String username, String password, HttpSession session){
    ServerResponse<User> response = iUserService.login(username,password);
        if (response.isSuccess()){
User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
//说明是管理员身份登录
                session.setAttribute(Const.CURRENT_USER,user);
            }else{
                return ServerResponse.createByErrorMessage("不是管理员，无法登录");
            }

        }
return response;
    }

}
