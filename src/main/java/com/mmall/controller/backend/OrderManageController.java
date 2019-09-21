/**
 * projectName: mmall
 * fileName: OrderManageController.java
 * packageName: com.mmall.controller.backend
 * date: 2019-09-18 20:06
 * copyright(c) HanYu
 */
package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: OrderManageController
 * @packageName: com.mmall.controller.backend
 * @description:
 * @data: 2019-09-18 20:06
 **/
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iOrderService;
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: list
     * @description: 管理员获取全部订单详情
     * @author: HanYu
     * @param session
     * @param pageSize
     * @param pageNum
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> list(HttpSession session,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.getListManage(pageSize, pageNum);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: detail
     * @description: 获取订单详情
     * @author: HanYu
     * @param session
     * @param orderNo
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.OrderVo>
     * @throws:
     */
    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<OrderVo> detail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.getDetailManage(orderNo);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: search
     * @description: TODO 搜寻订单(扩展功能待写)
     * @author: HanYu
     * @param session
     * @param orderNo
     * @param pageSize
     * @param pageNum
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> search(HttpSession session, Long orderNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.searchManage(orderNo,pageSize,pageNum);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: sendGoods
     * @description: 订单发货
     * @author: HanYu
     * @param session
     * @param orderNo
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    @RequestMapping(value = "sendGoods.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> sendGoods(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iOrderService.sendGoodsManage(orderNo);
        } else {
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
}