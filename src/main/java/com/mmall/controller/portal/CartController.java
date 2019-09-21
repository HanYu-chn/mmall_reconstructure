/**
 * projectName: mmall
 * fileName: CartController.java
 * packageName: com.mmall.controller.portal
 * date: 2019-09-09 21:20
 * copyright(c) HanYu
 */
package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: CartController
 * @packageName: com.mmall.controller.portal
 * @description:
 * @data: 2019-09-09 21:20
 **/
@RequestMapping("/cart/")
@Controller
public class CartController {
    @Autowired
    private ICartService iCartService;

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getList
     * @description: 获取购物车内的记录集合,包装成CartVo
     * @author: HanYu
     * @param session
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    @RequestMapping(value = "getList.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> getList(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iCartService.getList(user.getId());
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getCartProductList
     * @description: 获取购物车内选中的商品详情
     * @author: HanYu
     * @param session
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    @RequestMapping(value = "getCartCheckedProductList.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCartCheckedProductList(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iCartService.getCartCheckedProductList(user.getId());
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: add
     * @description: 添加商品至购物车,并返回购物车详情
     * @author: HanYu
     * @param session
     * @param productId
     * @param count
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iCartService.add(user.getId(), productId, count);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: add
     * @description: 更新购物车中商品数量,并返回购物车详情
     * @author: HanYu
     * @param session
     * @param productId
     * @param count
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iCartService.update(user.getId(), productId, count);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: delete
     * @description: 删除购物车中商品,并返回购物车详情
     * @author: HanYu
     * @param session
     * @param productIds 删除商品ID,与前端约定传值形式   "1,2,3"
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    @RequestMapping(value = "delete.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iCartService.delete(user.getId(), productIds);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: selectAll
     * @description: 购物车全选操作
     * @author: HanYu
     * @param session
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    @RequestMapping(value = "selectAll.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iCartService.selectOrUnselect(user.getId(), Const.Cart.CHECKED, null);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: selectAll
     * @description: 购物车全不选操作
     * @author: HanYu
     * @param session
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    @RequestMapping(value = "unSelectAll.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iCartService.selectOrUnselect(user.getId(), Const.Cart.UNCHECKED, null);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: selectOne
     * @description: 购物车对一个商品进行选择操作
     * @author: HanYu
     * @param session
     * @param productId
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    @RequestMapping(value = "selectOne.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> selectOne(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if(productId == null) return ServerResponse.createByError("参数错误");
        return iCartService.selectOrUnselect(user.getId(), Const.Cart.CHECKED, productId);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: selectOne
     * @description: 购物车对一个商品进行取消选择操作
     * @author: HanYu
     * @param session
     * @param productId
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    @RequestMapping(value = "unSelectOne.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> unSelectOne(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        if(productId == null) return ServerResponse.createByError("参数错误");
        return iCartService.selectOrUnselect(user.getId(), Const.Cart.UNCHECKED, productId);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getCartProductNum
     * @description: 获取购物车中商品总数
     * @author: HanYu
     * @param session
     * @return: com.mmall.common.ServerResponse<java.lang.Integer>
     * @throws:
     */
    @RequestMapping(value = "getCartProductNum.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Integer> getCartProductNum(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createBySuccess(0);
        return iCartService.getCartProductNum(user.getId());
    }
}