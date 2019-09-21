/**
 * projectName: mmall
 * fileName: UserController.java
 * packageName: com.mmall.controller.portal
 * date: 2019-08-18 15:06
 * copyright(c) HanYu
 */
package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.nio.cs.US_ASCII;

import javax.servlet.http.HttpSession;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: UserController
 * @packageName: com.mmall.controller.portal
 * @description: UserController
 * @data: 2019-08-18 15:06
 **/
@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: login
     * @description: 用户登录
     * @author: HanYu
     * @param username
     * @param password
     * @param session
     * @return: com.mmall.common.ServerResponse<com.mmall.pojo.User>
     *          若成功登录,则返回带有该用户详情的serverResponse,
     *          若失败,则返回状态码为ERROR的serverResponse.
     * @throws:
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> serverResponse = iUserService.login(username, password);
        if (serverResponse.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: logout
     * @description: 用户退出登录
     * @author: HanYu
     * @param session
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess("退出成功");
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
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: checkValid
     * @description: 验证value是否有效
     * @author: HanYu
     * @param str  用户名或邮箱
     * @param type   "userName" 或 "email"
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    @RequestMapping(value = "checkValid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getUserInfo
     * @description: 获取用户信息
     * @author: HanYu
     * @param session
     * @return: com.mmall.common.ServerResponse<com.mmall.pojo.User>
     * @throws:
     */
    @RequestMapping(value = "getUserInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登录,无法获取用户信息");
        }
        return ServerResponse.createBySuccess(user);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: forgetGetQuestion
     * @description: 根据用户名获取密码提示问题
     * @author: HanYu
     * @param username
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    @RequestMapping(value = "forgetGetQuestion.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.getQuestion(username);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: forgetCheckAnswer
     * @description: 检查忘记密码时回答问题的答案
     * @author: HanYu
     * @param userName
     * @param question
     * @param answer
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    @RequestMapping(value = "forgetCheckAnswer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String userName, String question, String answer) {
        return iUserService.checkAnswer(userName, question, answer);
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
    @RequestMapping(value = "forgetResetPassword.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String userName, String passwordNew, String token) {
        return iUserService.forgetResetPassword(userName, passwordNew, token);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: resetPassword
     * @description: 登录状态下重置密码
     * @author: HanYu
     * @param session
     * @param passwordNew
     * @param passwordOld
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    @RequestMapping(value = "resetPassword.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordNew, String passwordOld) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError("用户未登录");
        }
        return iUserService.resetPassword(passwordNew, passwordOld, user);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: updateUserInfo
     * @description: 登录状态下更新个人信息
     * @author: HanYu
     * @param session
     * @param user
     * @return: com.mmall.common.ServerResponse<com.mmall.pojo.User>
     * @throws:
     */
    @RequestMapping(value = "updateUserInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByError("用户未登录");
        }
        //防止横向越权
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateUserInfo(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getInformation
     * @description: 获取当前登录用户的详细信息，未登录则强制登录
     * @author: HanYu
     * @param session
     * @return: com.mmall.common.ServerResponse<com.mmall.pojo.User>
     * @throws:
     */
    @RequestMapping(value = "getInformation.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,需要强制登录,code = 10");
        }
        return iUserService.getUserInfo(currentUser.getId());
    }
}
