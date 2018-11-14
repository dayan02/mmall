package com.mmall.service.Impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 大燕 on 2018/11/14.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = this.userMapper.checkUserName(username);
        if(resultCount == 0){
            return  ServerResponse.createByErrorMessage("用户名不存在");
        }
        // md5加密之后的密码验证一下
String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
        if(user ==  null){
            return  ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    public  ServerResponse<String> register(User user){
        //校验用户名以及email是否存在
//       int resultCount  = this.userMapper.checkUserName(user.getUsername());
//        if(resultCount > 0){
//            return  ServerResponse.createByErrorMessage("用户名存在");
//        }
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if (!validResponse.isSuccess()){
            return  validResponse;
        }

//resultCount  = this.userMapper.checkEmail(user.getEmail());
//        if (resultCount>0){
//            return  ServerResponse.createByErrorMessage("email存在");
//        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if (!validResponse.isSuccess()){
            return  validResponse;
        }


        //设置用户身份，普通还是管理者
        user.setRole(Const.Role.ROLE_CUSTOMER);


        //md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
int resultCount  = userMapper.insert(user);
        if (resultCount == 0){
           return  ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }


    /**
     * 参数校验
     * @param str
     * @param type
     * @return
     */
    public  ServerResponse<String> checkValid(String str,String type){
if (StringUtils.isBlank(type)){
    //开始校验
    if(Const.USERNAME.equals(type)){
        int resultCount  = this.userMapper.checkUserName(str);
        if(resultCount > 0){
            return  ServerResponse.createByErrorMessage("用户名存在");
        }
    }
    if (Const.EMAIL.equals(type)){
        int resultCount  = this.userMapper.checkEmail(str);
        if (resultCount>0){
            return  ServerResponse.createByErrorMessage("email存在");
        }
    }
}else{
    return  ServerResponse.createByErrorMessage("参数错误");
}
        return  ServerResponse.createBySuccessMessage("校验成功");

    }


}
