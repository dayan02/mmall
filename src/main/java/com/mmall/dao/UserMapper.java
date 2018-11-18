package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //查看是否有此用户
    int checkUserName(String username);

    //登录
    User selectLogin(@Param("username") String username,@Param("password") String password);
    //判断email是否存在
    int checkEmail(String email);
    //查找答案问题
    String selectQuestionByUsername (String username);
    //核对问题
    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);

    //修改密码
    int uodatePassword(@Param("username")String username,@Param("passwordNew")String passwordNew);
}