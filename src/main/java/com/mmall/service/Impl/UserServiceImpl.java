package com.mmall.service.Impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by 大燕 on 2018/11/14.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = this.userMapper.checkUserName(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        // md5加密之后的密码验证一下
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */

    public ServerResponse<String> register(User user) {
        //校验用户名以及email是否存在
//       int resultCount  = this.userMapper.checkUserName(user.getUsername());
//        if(resultCount > 0){
//            return  ServerResponse.createByErrorMessage("用户名存在");
//        }
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

//resultCount  = this.userMapper.checkEmail(user.getEmail());
//        if (resultCount>0){
//            return  ServerResponse.createByErrorMessage("email存在");
//        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        //设置用户身份，普通还是管理者
        user.setRole(Const.Role.ROLE_CUSTOMER);

        //md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }


    /**
     * 参数校验
     *
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            //开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = this.userMapper.checkUserName(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = this.userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("email存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");

    }

    /** 找回密码问题
     * @param username
     * @return
     */

    public ServerResponse selectQuestion(String username) {
        ServerResponse validQuestion = this.checkValid(username, Const.USERNAME);
        if (validQuestion.isSuccess()) {
            //校验：当用户不存在的时候会出现校验成功，所以这是issuccess对应信息时用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccessMessage(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }



    /**
     *   核对问题的答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultConut = this.userMapper.checkAnswer(username, question, answer);
        if (resultConut > 0) {
            //说明问题及问题答案是此用户的，并且是正确的
            //给一个令牌token，进行验证
            String forgetToken = UUID.randomUUID().toString();
            //把forgetToken放到本地缓存中，设置有效期
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }



    /**
     *    忘记密码的重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse validQuestion = this.checkValid(username, Const.USERNAME);
        if (validQuestion.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }
        if (StringUtils.equals(forgetToken, token)) {
            String md5Passord = MD5Util.MD5EncodeUtf8(passwordNew);
int rowCount = this.userMapper.updatePassword(username,md5Passord);
            if (rowCount>0){
                return ServerResponse.createByErrorMessage("修改密码成功");
            }
        }else{
            //传过来的token跟存储的token不一样
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
return ServerResponse.createByErrorMessage("修改密码失败");
    }



    /**
     * 登录状态的重置密码
     * @param passwordOld
     * @param passordNew
     * @param user
     * @return
     */
    public  ServerResponse<String> resetPassword(String passwordOld,String passordNew,User user){
        //防止横向越权，要校验一下当前这个用户的旧密码
        int resultCount = this.userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
    if (resultCount == 0){
    return  ServerResponse.createByErrorMessage("旧密码错误");
}
        user.setPassword(MD5Util.MD5EncodeUtf8(passordNew));
int updateCount = this.userMapper.updateByPrimaryKey(user);
        if (updateCount>0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");

    }


    /**更新用户信息
     * @param user
     * @return
     */
    public  ServerResponse<User> updateInformation(User user){
        //username和id不能被更新
        //email也要校验，校验新的email是否已经被使用
        int resultCount = this.userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount>0){
            return ServerResponse.createByErrorMessage("email已经被使用，请更换新的email");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = this.userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount>0){
            return ServerResponse.createBySuccess("更新信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新信息失败");
        }


    //获取用户全部信息
    public  ServerResponse<User> getInformation(Integer userId){
User user = this.userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
        }
