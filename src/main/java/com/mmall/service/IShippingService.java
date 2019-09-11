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
    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse delete(Integer userId, Integer shippingId);

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> getList(Integer userId,Integer pageNum,Integer pageSize);
}