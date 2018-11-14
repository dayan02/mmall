package com.mmall.controller.portal;

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
 * Created by 大燕 on 2018/11/14.
 */

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    @ResponseBody
public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response  = iUserService.login(username,password);
        if(response.isSuccess()){
session.setAttribute("Const.CURRENT_USER",response.getData());
        }

return  response;
}

    /**
     * 用户退出登录
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do" ,method=RequestMethod.GET)
    @ResponseBody
    public  ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return  ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do" ,method=RequestMethod.GET)
    @ResponseBody
public  ServerResponse<String> register(User user){
return  iUserService.register(user);
}

//校验email和用户名是否存在，每次输入信息实时校验

    public ServerResponse<String> checValid(String str,String type){

    }


}
