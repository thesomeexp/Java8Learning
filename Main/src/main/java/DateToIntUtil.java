import java.util.Date;

/**
 * 该工具类有时区问题
 *
 * @author liangzhirong
 * @date 2021/6/21
 */
public class DateToIntUtil {

    // millisec * sec * min * hours
    // 1000 * 60 * 60 * 24 = 86400000
    public static final long MAGIC = 86400000L;

    public static int dateToDays(Date date) {
        return (int) (date.getTime() / MAGIC);
    }

    public static Date daysToDate(int days) {
        return new Date((long) days * MAGIC);
    }

}
