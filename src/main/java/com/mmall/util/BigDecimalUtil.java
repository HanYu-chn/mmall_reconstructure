/**
 * projectName: mmall
 * fileName: BigDecimalUtil.java
 * packageName: com.mmall.util
 * date: 2019-09-10 13:23
 * copyright(c) HanYu
 */
package com.mmall.util;

import java.math.BigDecimal;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: BigDecimalUtil
 * @packageName: com.mmall.util
 * @description:
 * @data: 2019-09-10 13:23
 **/
public class BigDecimalUtil {
    private BigDecimalUtil(){}

    public static BigDecimal add(double v1, double v2) {
        BigDecimal value1 = new BigDecimal(Double.toString(v1));
        BigDecimal value2 = new BigDecimal(Double.toString(v2));
        return value1.add(value2);
    }

    public static BigDecimal sub(double v1, double v2) {
        BigDecimal value1 = new BigDecimal(Double.toString(v1));
        BigDecimal value2 = new BigDecimal(Double.toString(v2));
        return value1.subtract(value2);
    }

    public static BigDecimal mul(double v1, double v2) {
        BigDecimal value1 = new BigDecimal(Double.toString(v1));
        BigDecimal value2 = new BigDecimal(Double.toString(v2));
        return value1.multiply(value2);
    }

    public static BigDecimal div(double v1, double v2) {
        BigDecimal value1 = new BigDecimal(Double.toString(v1));
        BigDecimal value2 = new BigDecimal(Double.toString(v2));
        return value1.divide(value2,2,BigDecimal.ROUND_HALF_UP);
    }
}