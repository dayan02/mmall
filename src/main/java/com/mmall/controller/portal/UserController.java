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
@RequestMapping(value = "checValid.do" ,method=RequestMethod.GET)
@ResponseBody
    public ServerResponse<String> checValid(String str,String type){
return iUserService.checkValid(str,type);
    }
@RequestMapping(value = "get_user_info.do" ,method = RequestMethod.GET)
    @ResponseBody
    public  ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
            return  ServerResponse.createBySuccess(user);
        }
        return  ServerResponse.createByErrorMessage("用户未登录，获取不到信息");
    }
//密码提示问题的获取
@RequestMapping(value = "forget_get_question.do" ,method = RequestMethod.GET)
@ResponseBody
    public  ServerResponse<String> forgetGetQuestion(String username){

    return  iUserService.selectQuestion(username);
    }

//使用本地缓存校验问题答案接口
@RequestMapping(value = "forget_check_answer.do" ,method = RequestMethod.GET)
@ResponseBody
public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){

    return this.iUserService.checkAnswer(username,question,answer);
}


    //token给前端之后，在忘掉密码的重置密码中，需要与后台的token进行对比
    //忘记密码中的重置密码
    @RequestMapping(value = "forget_reset_password.do" ,method = RequestMethod.GET)
    @ResponseBody
    public  ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
return  this.iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }


    //登录状态的重置密码
    @RequestMapping(value = "reset_password.do" ,method =RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return this.iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    @RequestMapping(value = "update_information.do" ,method =RequestMethod.GET)
    @ResponseBody
    //更新用户信息
    public  ServerResponse<User> update_information(HttpSession session,User user){
        User currentUser =(User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        //只有在登录状态才能更改用户信息，所以需要进行登录判断
        //为了防止越权问题，就把id获取通过session获取当前用户id，用户是当前用户名，即获取当前登录的用户id和name
        user.setId(currentUser.getId());
        user.setUsername((currentUser.getUsername()));
        ServerResponse<User> response = this.iUserService.updateInformation(user);
        if (response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return  response;
    }
}
