package top.summus.sword.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    public static String parseDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
