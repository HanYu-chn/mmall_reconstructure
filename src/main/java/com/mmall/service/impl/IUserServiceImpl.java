/**
 * projectName: mmall
 * fileName: IUserServiceImpl.java
 * packageName: com.mmall.service.impl
 * date: 2019-08-18 15:49
 * copyright(c) HanYu
 */
package com.mmall.service.impl;

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
import sun.security.provider.MD5;

import java.util.UUID;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: IUserServiceImpl
 * @packageName: com.mmall.service.impl
 * @description: 用户服务实现类
 * @data: 2019-08-18 15:49
 **/
@Service
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /*---------------------------------------分割线-----------------------------------------**/
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
    @Override
    public ServerResponse<User> login(String username, String passowrd) {
        int resultCount = userMapper.checkUserName(username);
        if (resultCount <= 0) {
            return ServerResponse.createByError("用户名不存在");
        }
        //MD5
        String md5Password = MD5Util.MD5EncodeUtf8(passowrd);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByError("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: register
     * @description: 用户注册
     * @author: HanYu
     * @param user
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> checkResult = checkValid(user.getUsername(), Const.USERNAME);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        checkResult = checkValid(user.getEmail(), Const.EMAIL);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int result = userMapper.insert(user);
        if (result <= 0) {
            return ServerResponse.createByError("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

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
    @Override
    public ServerResponse<String> checkValid(String value, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUserName(value);
                if (resultCount > 0) {
                    return ServerResponse.createByError("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(value);
                if (resultCount > 0) {
                    return ServerResponse.createByError("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByError("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }
    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @title: getQuestion
     * @description: 根据用户名获取密码提示问题
     * @author: HanYu
     * @param username
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    @Override
    public ServerResponse<String> getQuestion(String username) {
        ServerResponse<String> result = checkValid(username, Const.USERNAME);
        if(result.isSuccess()) {
            return ServerResponse.createByError("用户名不存在");
        }
        String question = userMapper.selectQuestion(username);
        if(StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByError("提示问题为空");
    }
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
    @Override
    public ServerResponse<String> checkAnswer(String userName, String question, String answer) {
        int result = userMapper.checkAnswer(userName, question, answer);
        if (result > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + userName,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByError("问题答案错误");
    }
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
    @Override
    public ServerResponse<String> forgetResetPassword(String userName, String passwordNew, String token) {
        if(StringUtils.isBlank(token)) {
            return ServerResponse.createByError("参数错误,Token需要传递");
        }
        ServerResponse<String> result = checkValid(userName, Const.USERNAME);
        if(result.isSuccess()) {
            return ServerResponse.createByError("用户名不存在");
        }
        String userToken = TokenCache.getKey(TokenCache.TOKEN_PREFIX + userName);
        if (StringUtils.isBlank(userToken)) {
            return ServerResponse.createByError("token失效");
        }
        if (StringUtils.equals(userToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUserName(userName, md5Password);
            if(resultCount > 0) {
                return ServerResponse.createBySuccessMessage("用户密码更新成功");
            }
        } else {
            return ServerResponse.createByError("token错误");
        }
        return ServerResponse.createByError("用户密码更新失败");
    }
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
    @Override
    public ServerResponse<String> resetPassword(String passwordNew, String passwordOld, User user) {
        int resultCount = userMapper.checkPassword(user.getId(),MD5Util.MD5EncodeUtf8(passwordOld));
        if(resultCount <= 0) {
            return ServerResponse.createByError("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByError("密码更新失败");
    }
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
    @Override
    public ServerResponse<User> updateUserInfo(User user) {
        //修改的email不能被占用
        int resultCount = userMapper.checkEmailOccupiedByUserId(user.getId(), user.getEmail());
        if(resultCount > 0) {
            return ServerResponse.createByError("email已被占用,请更改后重新尝试");
        }
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功",user);
        }
        return ServerResponse.createByError("更新个人信息失败");
    }
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
    @Override
    public ServerResponse<User> getUserInfo(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByError("找不到该用户");
        }
        //返回结果前把密码置空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: checkAdminRole
     * @description: 校验是不是管理员
     * @author: HanYu
     * @param user
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
