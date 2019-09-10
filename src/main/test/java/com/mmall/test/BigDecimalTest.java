/**
 * projectName: mmall
 * fileName: BigDecimalTest.java
 * packageName: com.mmall.test
 * date: 2019-09-10 13:17
 * copyright(c) HanYu
 */
package com.mmall.test;

import java.math.BigDecimal;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: BigDecimalTest
 * @packageName: com.mmall.test
 * @description:
 * @data: 2019-09-10 13:17
 **/
public class BigDecimalTest {
    public static void main(String[] args) {
        //System.out.println(0.05 + 0.01);
        BigDecimal bigDecimal = new BigDecimal("0.05");
        BigDecimal bigDecimal1 = new BigDecimal("0.01");
        System.out.println(bigDecimal.add(bigDecimal1));
    }
}