/**
 * projectName: mmall
 * fileName: UserManageController.java
 * packageName: com.mmall.controller.backend
 * date: 2019-09-02 18:01
 * copyright(c) HanYu
 */
package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: UserManageController
 * @packageName: com.mmall.controller.backend
 * @description: 后台管理Controller
 * @data: 2019-09-02 18:01
 **/
@Controller
@RequestMapping("/manage/user/")
public class UserManageController {
    @Autowired
    private IUserService iUserService;

    /*---------------------------------------分割线-----------------------------------------**/
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String userName, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(userName, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            } else {
                return ServerResponse.createByError("登录用户不是管理员");
            }
        }
        return response;
    }
}