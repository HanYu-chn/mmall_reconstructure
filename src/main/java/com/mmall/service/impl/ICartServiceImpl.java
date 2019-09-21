/**
 * projectName: mmall
 * fileName: ICartServiceImpl.java
 * packageName: com.mmall.service.impl
 * date: 2019-09-09 21:24
 * copyright(c) HanYu
 */
package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.service.IOrderService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: ICartServiceImpl
 * @packageName: com.mmall.service.impl
 * @description:
 * @data: 2019-09-09 21:24
 **/
@Service
public class ICartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    /*---------------------------------------分割线-----------------------------------------**/
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
    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            //说明这个产品不在该用户的购物车内,需要在购物车内新增记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        } else {
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.getList(userId);
    }
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
    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.getList(userId);
    }
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
    @Override
    public ServerResponse<CartVo> delete(Integer userId, String productIds) {
        List<String> idList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(idList)) {
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteCartByUserIdProductIds(userId, idList);
        return this.getList(userId);
    }
    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param userId
     * @title: getList
     * @description: 获取购物车内的记录集合, 包装成CartVo
     * @author: HanYu
     * @return: com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     * @throws:
     */
    @Override
    public ServerResponse<CartVo> getList(Integer userId) {
        CartVo cartVo = this.assembleCartVo(userId);
        return ServerResponse.createBySuccess(cartVo);
    }
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
    @Override
    public ServerResponse<CartVo> selectOrUnselect(Integer userId, Integer checked, Integer productId) {
        cartMapper.updateCheckedOrUnChecked(userId, checked, productId);
        return this.getList(userId);
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getCartProductNum
     * @description: 获取购物车中商品总数
     * @author: HanYu
     * @param userId
     * @return: com.mmall.common.ServerResponse<java.lang.Integer>
     * @throws:
     */
    @Override
    public ServerResponse<Integer> getCartProductNum(Integer userId) {
        if (userId == null) return ServerResponse.createBySuccess(0);
        return ServerResponse.createBySuccess(cartMapper.selectCartProductNum(userId));
    }

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param userId
     * @title: assembleCartVo
     * @description: 组装CartVo
     * @author: HanYu
     * @return: com.mmall.vo.CartVo
     * @throws:
     */
    private CartVo assembleCartVo(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo = assembleCartProduct(cartItem, userId);
                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.isAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return cartVo;
    }

    /*---------------------------------------分割线-----------------------------------------**/

    /**
     * @param userId
     * @title: isAllCheckedStatus
     * @description: 检查购物车是否为全选状态
     * @author: HanYu
     * @return: boolean  true为全选,false为没有全选
     * @throws:
     */
    private boolean isAllCheckedStatus(Integer userId) {
        if (userId == null) return false;
        return cartMapper.selectCartProductCheckedByUserId(userId) == 0;
    }
    /*---------------------------------------分割线-----------------------------------------**/
    /**
     * @title: getCartProductList
     * @description: 获取购物车内选中的商品详情
     * @author: HanYu
     * @param userId
     * @return: com.mmall.common.ServerResponse<java.lang.Integer>
     * @throws:
     */
    @Override
    public ServerResponse getCartCheckedProductList(Integer userId) {
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        if(CollectionUtils.isEmpty(cartList)) return ServerResponse.createByError("购物车没有选中商品");
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        for (Cart cart : cartList) {
            cartProductVoList.add(assembleCartProduct(cart,userId));
        }
        return ServerResponse.createBySuccess(cartProductVoList);
    }
    /*---------------------------------------分割线-----------------------------------------**/
    private CartProductVo assembleCartProduct(Cart cartItem,Integer userId) {
        CartProductVo cartProductVo = new CartProductVo();
        cartProductVo.setId(cartItem.getId());
        cartProductVo.setUserId(userId);
        cartProductVo.setProductId(cartItem.getProductId());

        Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
        if (product != null) {
            cartProductVo.setProductMainImage(product.getMainImage());
            cartProductVo.setProductName(product.getName());
            cartProductVo.setProductSubtitle(product.getSubtitle());
            cartProductVo.setProductStatus(product.getStatus());
            cartProductVo.setProductPrice(product.getPrice());
            cartProductVo.setProductStock(product.getStock());
            //判断库存
            int buyLimitCount = 0;
            if (product.getStock() >= cartItem.getQuantity()) {
                //库存充足的时候
                buyLimitCount = cartItem.getQuantity();
                cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
            } else {
                buyLimitCount = product.getStock();
                cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                //购物车中更新有效库存
                Cart cartForQuantity = new Cart();
                cartForQuantity.setId(cartItem.getId());
                cartForQuantity.setQuantity(buyLimitCount);
                cartMapper.updateByPrimaryKeySelective(cartForQuantity);
            }
            cartProductVo.setQuantity(buyLimitCount);
            //计算总价
            cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
            cartProductVo.setProductChecked(cartItem.getChecked());
        }
        return cartProductVo;
    }
}