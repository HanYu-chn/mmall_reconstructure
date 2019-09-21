/**
 * projectName: mmall
 * fileName: OrderController.java
 * packageName: com.mmall.controller.portal
 * date: 2019-09-17 13:56
 * copyright(c) HanYu
 */
package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: OrderController
 * @packageName: com.mmall.controller.portal
 * @description:
 * @data: 2019-09-17 13:56
 **/
@RequestMapping("/order/")
@Controller
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: createOrder
     * @description: 创建订单
     * @author: HanYu
     * @param session
     * @param shippingId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "createOrder.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse createOrder(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iOrderService.createOrder(user.getId(), shippingId);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: cancelOrder
     * @description: 取消订单
     * @author: HanYu
     * @param session
     * @param orderNo
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "cancelOrder.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse cancelOrder(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iOrderService.cancelOrder(user.getId(), orderNo);
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getOrderProductList
     * @description: 获取订单商品详情
     * @author: HanYu
     * @param session
     * @param orderNo
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "getOrderProductList.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getOrderProductList(HttpSession session,Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iOrderService.getOrderProductList(user.getId(),orderNo);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: detail
     * @description: 获取订单详情
     * @author: HanYu
     * @param session
     * @param orderNo
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse detail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iOrderService.getDetail(user.getId(),orderNo);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: list
     * @description: 获取订单详情集合
     * @author: HanYu
     * @param session
     * @param pageSize
     * @param pageNum
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageSize" , defaultValue = "10") Integer pageSize,
                                                    @RequestParam(value = "pageNum" , defaultValue = "1") Integer pageNum) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        return iOrderService.getList(user.getId(), pageSize, pageNum);
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: pay
     * @description: alipay支付订单
     * @author: HanYu
     * @param session
     * @param orderNo
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    @RequestMapping(value = "pay.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        String path = session.getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, user.getId(), path);
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: alipayCallback
     * @description: alipay异步回调接口
     * @author: HanYu
     * @param request
     * @return: java.lang.Object
     * @throws:
     */
    @RequestMapping(value = "alipayCallback.do", method = RequestMethod.POST)
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Iterator iter = parameterMap.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) parameterMap.get(name);
            String strValue = "";
            for (int i = 0; i < values.length; i++) {
                strValue = (i == values.length - 1) ? strValue + values[i] : strValue + values[i] + ",";
            }
            params.put(name, strValue);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());
        //重要! 验证回调是不是支付宝发的,并避免重复通知
        params.remove("sign_type");
        try {
            boolean checkSignResult = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!checkSignResult) {
                return ServerResponse.createByError("非法请求,再重复请求报警处理");
            }
        } catch (AlipayApiException e) {
            logger.error("验证支付宝回调异常", e);
            return ServerResponse.createByError("验证支付宝回调异常");
        }
        //todo 验证各种数据
        ServerResponse serverResponse = iOrderService.alipayCallBack(params);
        if (serverResponse.isSuccess()) {
            return Const.AlipayCallBack.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallBack.RESPONSE_FAILED;
    }

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: queryOrderPayStatus
     * @description: 查询订单支付状态
     * @author: HanYu
     * @param session
     * @param orderNo
     * @return: com.mmall.common.ServerResponse<java.lang.Boolean>
     * @throws:
     */
    @RequestMapping(value = "queryOrderPayStatus.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(orderNo, user.getId());
        if (serverResponse.isSuccess()) return ServerResponse.createBySuccess(true);
        return ServerResponse.createBySuccess(false);
    }
}