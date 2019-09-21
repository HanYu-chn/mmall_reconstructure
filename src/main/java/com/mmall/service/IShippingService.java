/**
 * projectName: mmall
 * fileName: IShippingService.java
 * packageName: com.mmall.service
 * date: 2019-09-11 14:34
 * copyright(c) HanYu
 */
package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * @version: V1.0
 * @author: HanYu
 * @interfaceName: IShippingService
 * @packageName: com.mmall.service
 * @description:
 * @data: 2019-09-11 14:34
 **/
public interface IShippingService {

    /**
     * @title: add
     * @description: 添加收货地址
     * @author: HanYu
     * @param userId
     * @param shipping
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse add(Integer userId, Shipping shipping);

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: update
     * @description: 更新收货地址
     * @author: HanYu
     * @param userId
     * @param shipping
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse update(Integer userId, Shipping shipping);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: delete
     * @description: 删除收货地址
     * @author: HanYu
     * @param userId
     * @param shippingId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse delete(Integer userId, Integer shippingId);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: select
     * @description: 选择收货地址
     * @author: HanYu
     * @param userId
     * @param shippingId
     * @return: com.mmall.common.ServerResponse
     * @throws:
     */
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getList
     * @description: 获取收货地址集合
     * @author: HanYu
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     * @throws:
     */
    ServerResponse<PageInfo> getList(Integer userId, Integer pageNum, Integer pageSize);
}