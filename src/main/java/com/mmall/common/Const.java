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

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
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
}
