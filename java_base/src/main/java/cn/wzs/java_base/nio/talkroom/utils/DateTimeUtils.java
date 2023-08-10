package cn.wzs.java_base.nio.talkroom.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyyMMdd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static String nowTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    public static void main(String[] args) {
        System.out.println(nowTime());
    }
}
