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

    int checkUserName(String userName);

    int checkEmail(String email);

    User selectLogin(@Param("userName") String userName, @Param("password") String password);

    String selectQuestion(String userName);

    int checkAnswer(@Param("userName") String userName, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUserName(@Param("userName") String userName, @Param("password") String password);

    int checkPassword(@Param("userId") int userId, @Param("password") String password);

    int checkEmailOccupiedByUserId(@Param("userId") int userId, @Param("email") String email);
}