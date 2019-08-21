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
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public ServerResponse<User> login(String username, String passowrd) {
        int resultCount = userMapper.checkUserName(username);
        if(resultCount == 0) {
            return ServerResponse.createByError("用户名不存在");
        }
        //todo MD5
        String md5Password = MD5Util.MD5EncodeUtf8(passowrd);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByError("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> checkResult = checkValid(user.getUsername(), Const.USERNAME);
        if(!checkResult.isSuccess()){
            return checkResult;
        }
        checkResult = checkValid(user.getEmail(), Const.EMAIL);
        if(!checkResult.isSuccess()){
            return checkResult;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int result = userMapper.insert(user);
        if (result == 0) {
            return ServerResponse.createByError("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String value, String type) {
        if(StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUserName(value);
                if(resultCount > 0) {
                    return ServerResponse.createByError("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(value);
                if(resultCount > 0) {
                    return ServerResponse.createByError("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByError("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }
}
