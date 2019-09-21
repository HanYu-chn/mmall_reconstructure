/**
 * projectName: mmall
 * fileName: Const.java
 * packageName: com.mmall.common
 * date: 2019-08-18 18:05
 * copyright(c) HanYu
 */
package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: Const
 * @packageName: com.mmall.common
 * @description: 常量
 * @data: 2019-08-18 18:05
 **/
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String USERNAME = "userName";
    public static final String EMAIL = "email";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface Cart {
        int CHECKED = 1;
        int UNCHECKED = 0;
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
    }

    public interface AlipayCallBack {
        String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_SUCCESS = "TRADE_SUCCESS";
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum ProductStatusEnum {
        ON_SALE("在售", 1),
        OFF_SALE("下线", 0);

        private String value;
        private int code;

        ProductStatusEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum OrderStatus {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已支付"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSED(60, "订单关闭");

        OrderStatus(int code, String status) {
            this.code = code;
            this.status = status;
        }

        public static OrderStatus codeOf(int code) {
            for (OrderStatus item : values()) {
                if (item.getCode() == code) {
                    return item;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }

        private int code;
        private String status;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝");
        private int code;
        private String platformName;

        PayPlatformEnum(int code, String platformName) {
            this.code = code;
            this.platformName = platformName;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }
    }

    public enum PaymentTypeEnum {
        PAYMENT_ONLINE(1, "在线支付");
        private int code;
        private String value;

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static PaymentTypeEnum codeOf(int code) {
            for (PaymentTypeEnum item : values()) {
                if (item.getCode() == code) {
                    return item;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
