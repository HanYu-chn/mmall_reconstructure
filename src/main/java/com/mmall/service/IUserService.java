/**
 * projectName: mmall
 * fileName: IUserService.java
 * packageName: com.mmall.service
 * date: 2019-08-18 15:45
 * copyright(c) HanYu
 */
package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @version: V1.0
 * @author: HanYu
 * @interfaceName: IUserService
 * @packageName: com.mmall.service
 * @description: 用户服务接口
 * @data: 2019-08-18 15:45
 **/
public interface IUserService {
    /**
     * @title: login
     * @description: 用户登录
     * @author: HanYu
     * @param username
     * @param passowrd
     * @return: com.mmall.common.ServerResponse<com.mmall.pojo.User>
     *          若成功登录,则返回带有该用户详情的serverResponse,
     *          若失败,则返回状态码为ERROR的serverResponse.
     * @throws:
     */
    ServerResponse<User> login(String username, String passowrd);
/*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: register
     * @description: 用户注册
     * @author: HanYu
     * @param user
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    ServerResponse<String> register(User user);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: checkValid
     * @description: 验证value是否有效
     * @author: HanYu
     * @param value  用户名或邮箱
     * @param type   "userName" 或 "email"
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    ServerResponse<String> checkValid(String value, String type);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getQuestion
     * @description: 根据用户名获取密码提示问题
     * @author: HanYu
     * @param username
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    ServerResponse<String> getQuestion(String username);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: checkAnswer
     * @description: 检查忘记密码时回答问题的答案
     * @author: HanYu
     * @param userName
     * @param question
     * @param answer
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    ServerResponse<String> checkAnswer(String userName, String question, String answer);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: forgetResetPassword
     * @description: 通过问题提示方式重置密码
     * @author: HanYu
     * @param userName
     * @param passwordNew
     * @param token
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    ServerResponse<String> forgetResetPassword(String userName, String passwordNew, String token);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: resetPassword
     * @description: 登录状态下重置密码
     * @author: HanYu
     * @param passwordNew
     * @param passwordOld
     * @param user
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    ServerResponse<String> resetPassword(String passwordNew, String passwordOld, User user);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: updateUserInfo
     * @description: 更新用户信息
     * @author: HanYu
     * @date: 2019-09-19 12:45
     * @param user
     * @return: com.mmall.common.ServerResponse<com.mmall.pojo.User>
     * @throws:
     */
    ServerResponse<User> updateUserInfo(User user);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getUserInfo
     * @description: 获取用户信息
     * @author: HanYu
     * @date: 2019-09-19 12:49
     * @param userId
     * @return: com.mmall.common.ServerResponse<com.mmall.pojo.User>
     * @throws:
     */
    ServerResponse<User> getUserInfo(int userId);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: checkAdminRole
     * @description: 校验是不是管理员
     * @author: HanYu
     * @param user
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse checkAdminRole(User user);
}

