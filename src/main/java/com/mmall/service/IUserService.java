package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by 大燕 on 2018/11/14.
 */
public interface IUserService {
    //登录
    ServerResponse<User> login(String name, String password);
    //注册
    ServerResponse<String> register(User user);
    //验证
    ServerResponse<String> checkValid(String str,String type);
    //找回密码的问题
    ServerResponse selectQuestion(String username);
   ServerResponse<String> checkAnswer(String username,String question,String answer);
   ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);
}
