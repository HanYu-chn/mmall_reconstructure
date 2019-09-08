/**
 * projectName: mmall
 * fileName: DateTimeUtil.java
 * packageName: com.mmall.util
 * date: 2019-09-06 13:23
 * copyright(c) HanYu
 */
package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @version: V1.0
 * @author: HanYu
 * @className: DateTimeUtil
 * @packageName: com.mmall.util
 * @description:
 * @data: 2019-09-06 13:23
 **/
public class DateTimeUtil {

    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /*---------------------------------------分割线-----------------------------------------**/
    //String -> Date
    public static Date strToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /*---------------------------------------分割线-----------------------------------------**/
    //Date -> String
    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
    /*---------------------------------------分割线-----------------------------------------**/
    //String -> Date
    public static Date strToDate(String dateTimeStr, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }
    /*---------------------------------------分割线-----------------------------------------**/
    //Date -> String
    public static String dateToStr(Date date, String format) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(format);
    }
}