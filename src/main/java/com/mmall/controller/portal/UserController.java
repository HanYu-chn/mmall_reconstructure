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
     * @param username 用户名
     * @param password 密码
     * @param session
     * @title: login
     * @description: 用户登录
     * @author: YH
     * @date: 2019-08-18 15:11
     * @return: java.lang.Object
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
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess("退出成功");
    }

    /*---------------------------------------分割线-----------------------------------------**/
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    @RequestMapping(value = "checkValid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /*---------------------------------------分割线-----------------------------------------**/
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
    @RequestMapping(value = "forgetGetQuestion.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.getQuestion(username);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    @RequestMapping(value = "forgetCheckAnswer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String userName, String question, String answer) {
        return iUserService.checkAnswer(userName, question, answer);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    @RequestMapping(value = "forgetResetPassword.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String userName, String passwordNew, String token) {
        return iUserService.forgetResetPassword(userName, passwordNew, token);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    //登录状态中重置密码
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
    //登录状态更新个人信息
    @RequestMapping(value = "updateUserInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByError("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateUserInfo(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    /*---------------------------------------分割线-----------------------------------------**/
    //获取当前登录用户的详细信息，并强制登录
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
