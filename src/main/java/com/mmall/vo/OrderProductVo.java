package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;


/**
 * @version: V1.0
 * @author: HanYu
 * @className: OrderProductVo
 * @packageName: com.mmall.vo
 * @description: 订单产品包装类
 * @data: 2019-09-18 19:07
 **/
public class OrderProductVo {
    private List<OrderItemVo> orderItemVoList;
    private BigDecimal productTotalPrice;
    private String imageHost;

    public List<OrderItemVo> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
