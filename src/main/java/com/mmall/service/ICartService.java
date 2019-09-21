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

    /**
     * @title: add
     * @description: 添加商品至购物车,并返回购物车详情
     * @author: HanYu
     * @param userId
     * @param productId
     * @param count
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: add
     * @description: 更新购物车中商品数量,并返回购物车详情
     * @author: HanYu
     * @param userId
     * @param productId
     * @param count
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: delete
     * @description: 删除购物车中商品,并返回购物车详情
     * @author: HanYu
     * @param userId
     * @param productIds 删除商品ID,与前端约定传值形式   "1,2,3"
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    ServerResponse<CartVo> delete(Integer userId, String productIds);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getList
     * @description: 获取购物车内的记录集合,包装成CartVo
     * @author: HanYu
     * @param userId
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    ServerResponse<CartVo> getList(Integer userId);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: selectOrUnselect
     * @description: 对购物车商品进行选择操作,productId为null时,则为全选或全不选操作,
     *                                      productId不为null时,则对单一商品选择操作.
     * @author: HanYu
     * @param userId
     * @param checked
     * @param productId
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    ServerResponse<CartVo> selectOrUnselect(Integer userId, Integer checked, Integer productId);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getCartProductNum
     * @description: 获取购物车中商品总数
     * @author: HanYu
     * @param userId
     * @return: com.mmall.common.ServerResponse<java.lang.Integer>
     * @throws:
     */
    ServerResponse<Integer> getCartProductNum(Integer userId);
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getCartProductList
     * @description: 获取购物车内选中的商品详情
     * @author: HanYu
     * @param userId
     * @return: com.mmall.common.ServerResponse<java.lang.Integer>
     * @throws:
     */
    ServerResponse getCartCheckedProductList(Integer userId);
}