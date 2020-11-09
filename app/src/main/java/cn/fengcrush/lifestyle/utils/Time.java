package cn.fengcrush.lifestyle.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    public static final String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
