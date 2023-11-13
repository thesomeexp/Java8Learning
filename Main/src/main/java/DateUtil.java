import java.util.Calendar;
import java.util.Date;

/**
 * @author liangzhirong
 * @date 2021/7/2
 */
public class DateUtil {

    public static String formatDate(Date date) {
        return cn.hutool.core.date.DateUtil.formatDate(date);
    }

    public static Date parse(String dateStr) {
        return cn.hutool.core.date.DateUtil.parse(dateStr);
    }

    public static Date getYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 对给定时间 增加/减少 分钟数 (返回新的实例)
     */
    public static Date addMinutes(Date date, int minutes) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, minutes);
            return calendar.getTime();
        } catch (Exception e) {
            String msg = "时间获取异常";
            e.printStackTrace();
            throw e;
        }
    }

    public static Date getStartDate(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return c.getTime();
        } catch (Exception e) {
            String msg = "获取整点时间异常";
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 获取 Date 的指定时间 (返回新的实例)
     */
    public static Date getDateFixDate(Date date, int hourOfDay, int minute, int second, int millisecond) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, second);
            c.set(Calendar.MILLISECOND, millisecond);
            return c.getTime();
        } catch (Exception e) {
            String msg = "获取指定时间异常";
            e.printStackTrace();
            throw e;
        }
    }

}
