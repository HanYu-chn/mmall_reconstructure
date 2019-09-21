/**
 * projectName: mmall
 * fileName: IOrderService.java
 * packageName: com.mmall.service
 * date: 2019-09-17 14:07
 * copyright(c) HanYu
 */
package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderVo;

import java.util.Map;

/**
 * @version: V1.0
 * @author: HanYu
 * @interfaceName: IOrderService
 * @packageName: com.mmall.service
 * @description:
 * @data: 2019-09-17 14:07
 **/
public interface IOrderService {

    /**
     * @param orderNo
     * @param userId
     * @param path
     * @title: pay
     * @description: alipay支付订单
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse pay(Long orderNo, Integer userId, String path);

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param params
     * @title: alipayCallBack
     * @description: alipay异步回调接口
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse alipayCallBack(Map<String, String> params);

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param orderNo
     * @param userId
     * @title: queryOrderPayStatus
     * @description: 查询订单支付状态
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse queryOrderPayStatus(Long orderNo, Integer userId);

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param userId
     * @param shippingId
     * @title: createOrder
     * @description: 创建订单
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse createOrder(Integer userId, Integer shippingId);

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param userId
     * @param orderNo
     * @title: cancelOrder
     * @description: 取消订单
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse cancelOrder(Integer userId, Long orderNo);

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param userId
     * @param orderNo
     * @title: getOrderProductList
     * @description: 获取订单商品详情
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse getOrderProductList(Integer userId, Long orderNo);

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param userId
     * @param orderNo
     * @title: getDetail
     * @description: 获取订单详情
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse getDetail(Integer userId, Long orderNo);

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param userId
     * @param pageSize
     * @param pageNum
     * @title: getList
     * @description: 获取订单详情集合
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    ServerResponse<PageInfo> getList(Integer userId, Integer pageSize, Integer pageNum);

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param pageSize
     * @param pageNum
     * @title: getListManage
     * @description: 管理员获取全部订单详情
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    ServerResponse<PageInfo> getListManage(Integer pageSize, Integer pageNum);

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param orderNo
     * @title: getDetailManage
     * @description: 获取订单详情
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse<OrderVo> getDetailManage(Long orderNo);

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param orderNo
     * @title: sendGoodsManage
     * @description: 订单发货
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     * @throws:
     */
    ServerResponse<String> sendGoodsManage(Long orderNo);

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param orderNo
     * @param pageSize
     * @param pageNum
     * @title: searchManage
     * @description: TODO 搜寻订单(扩展功能待写)
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    ServerResponse<PageInfo> searchManage(Long orderNo, Integer pageSize, Integer pageNum);
}