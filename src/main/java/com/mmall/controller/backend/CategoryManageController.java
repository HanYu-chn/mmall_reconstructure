/**
 * projectName: mmall
 * fileName: CategoryManageController.java
 * packageName: com.mmall.controller.backend
 * date: 2019-09-03 15:50
 * copyright(c) HanYu
 */
package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: CategoryManageController
 * @packageName: com.mmall.controller.backend
 * @description: CategoryManageController
 * @data: 2019-09-03 15:50
 **/
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: addCategory
     * @description: 添加分类
     * @author: HanYu
     * @param session
     * @param categoryName
     * @param parentId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "addCategory.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: updateCategoryName
     * @description: 更新分类名称
     * @author: HanYu
     * @param session
     * @param categoryName
     * @param categoryId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "updateCategoryName.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateCategoryName(HttpSession session, String categoryName, int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.updateCategoryName(categoryName, categoryId);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getChildrenParallelCategory
     * @description: 获取平级子分类
     * @author: HanYu
     * @param session
     * @param categoryId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "getChildrenParallelCategory.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId" ,defaultValue = "0") int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getChildrenParallelAndDeepCategory
     * @description: 获取其所有子分类的ID
     * @author: HanYu
     * @param session
     * @param categoryId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "getChildrenParallelAndDeepCategory.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenParallelAndDeepCategory(HttpSession session, @RequestParam(value = "categoryId" ,defaultValue = "0") int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getChildrenParallelAndDeepCategory(categoryId);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
}