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

    //判断email是否存在
    int checkEmail(String email);

    User selectLogin(@Param("username") String username,@Param("password") String password);
}