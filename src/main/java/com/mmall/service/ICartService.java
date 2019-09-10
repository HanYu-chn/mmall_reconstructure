/**
 * projectName: mmall
 * fileName: ICartService.java
 * packageName: com.mmall.service
 * date: 2019-09-09 21:24
 * copyright(c) HanYu
 */
package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @version: V1.0
 * @author: HanYu
 * @interfaceName: ICartService
 * @packageName: com.mmall.service
 * @description:
 * @data: 2019-09-09 21:24
 **/
public interface ICartService {
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> delete(Integer userId, String productIds);

    ServerResponse<CartVo> getList(Integer userId);

    ServerResponse<CartVo> selectOrUnselect(Integer userId, Integer checked,Integer productId);

    ServerResponse<Integer> getCartProductNum(Integer userId);
}