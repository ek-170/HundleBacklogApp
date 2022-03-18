package com.muramoto.extract.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class DateUtil {
    private static final String DATE            = "yyyyMMdd";
    private static final String DATE_WITH_SLASH = "yyyy/MM/dd";

    public static String getTodayStr(boolean separateSlash){
        Date             date       = new Date();
        TimeZone         timeZoneJP = TimeZone.getTimeZone("Asia/Tokyo");
        SimpleDateFormat result     = separateSlash ? new SimpleDateFormat(DATE_WITH_SLASH) : new SimpleDateFormat(DATE);
        result.setTimeZone(timeZoneJP);
        return result.format(date);
    }

    public static String dateFormat(Date date, boolean separateSlash){
        SimpleDateFormat result     = separateSlash ? new SimpleDateFormat(DATE_WITH_SLASH) : new SimpleDateFormat(DATE);
        return result.format(date);
    }
}