/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 5:42 PM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间格式化工具类
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 5:42 PM
 */
public class DateUtils {

    public static final Calendar CALENDAR = Calendar.getInstance();

    public static final String C_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String C_DATE_TIME_YMD="yyyyMMdd";

    public static final String C_DATE_TIME_YYMMDD="yyyy-MM-dd";

    public static final String C_DATE_TIME_HMS="HHmmss";

    public static final String C_DATE_TIME_YMDHMS="yyyyMMddHHmmss";

    public static final String C_DATE_TIME_YMDH = "yyyyMMddHH";

    /**
     * 获取当前日期时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String pareDate(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 格式化 ： yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String C_DATE_TIME_FORMAT() {
        return DateFormatUtils.format(new Date(), C_DATE_TIME_FORMAT);
    }

    /**
     * 格式化 ：yyyyMMdd
     *
     * @return
     */
    public static String C_DATE_TIME_YMD() {
        return DateFormatUtils.format(new Date(), C_DATE_TIME_YMD);
    }

    /**
     * 格式化 ：yyyy-MM-dd
     *
     * @return
     */
    public static String C_DATE_TIME_YYMMDD() {
        return DateFormatUtils.format(new Date(), C_DATE_TIME_YYMMDD);
    }

    /**
     * 格式化 ：HHmmss
     *
     * @return
     */
    public static String C_DATE_TIME_HMS() {
        return DateFormatUtils.format(new Date(), C_DATE_TIME_HMS);
    }

    /**
     * 格式化 ：yyyyMMddHHmmss
     *
     * @return
     */
    public static String C_DATE_TIME_YMDHMS() {
        return DateFormatUtils.format(new Date(), C_DATE_TIME_YMDHMS);
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取据当前时间差几天的日期
     * @return
     */
    public static Date getDays(int day){
        CALENDAR.setTime(new Date());
        CALENDAR.add(Calendar.DATE, day);
        Date d = CALENDAR.getTime();

        return d;
    }

    /**
     * 获取当前时间之前的30天日期
     * @return
     */
    public static Date get30Days(){
        CALENDAR.setTime(new Date());
        CALENDAR.add(Calendar.DATE, - 30);
        Date d = CALENDAR.getTime();

        return d;
    }

    /**
     * 获取当前时间之前的一个月日期
     * @return
     */
    public static Date getMonthDays(){
        CALENDAR.setTime(new Date());
        CALENDAR.add(Calendar.MONTH, -1);
        Date m = CALENDAR.getTime();
        return m;
    }

    /**
     * 获取当前时间之前的一年日期
     * @return
     */
    public static Date getYearDays(){
        CALENDAR.setTime(new Date());
        CALENDAR.add(Calendar.YEAR, -1);
        Date y = CALENDAR.getTime();
        return y;
    }

    /**
     * 获取当前hour,24小时制
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前时间前一小时
     *
     * @return
     */
    public static String getOneHoursAgoTime() {
        String oneHoursAgoTime = "";
        Calendar cal = Calendar.getInstance();
        //把时间设置为当前时间-1小时，同理，也可以设置其他时间
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
        //获取到完整的时间
        oneHoursAgoTime = new SimpleDateFormat(C_DATE_TIME_YMDH).format(cal.getTime());
        return oneHoursAgoTime;
    }

    /**
     * @title: dateCompare
     * @description: 比较日期大小
     * @param date1 日期1
     * @param date2 日期2
     * @return
     */
    public static int dateCompare(Date date1, Date date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateFirst = dateFormat.format(date1);
        String dateLast = dateFormat.format(date2);
        int dateFirstIntVal = Integer.parseInt(dateFirst);
        int dateLastIntVal = Integer.parseInt(dateLast);
        if (dateFirstIntVal > dateLastIntVal) {
            return 1;
        } else if (dateFirstIntVal < dateLastIntVal) {
            return -1;
        }
        return 0;
    }

    /**
     * 获取当前日期的次日
     * @return
     */
    public static String getTomorrow(){
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        //把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, 1);
        //这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date);
    }
}
