package top.summus.sword.util;

import androidx.room.TypeConverter;

import java.util.Date;

public class RoomConventer {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
