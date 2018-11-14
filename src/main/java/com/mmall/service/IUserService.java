package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by 大燕 on 2018/11/14.
 */
public interface IUserService {

    ServerResponse<User> login(String name, String password);
    ServerResponse<String> register(User user);

}
