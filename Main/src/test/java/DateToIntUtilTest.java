import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liangzhirong
 * @date 2021/7/6
 */
public class DateToIntUtilTest {

    @Test
    public void Test01() throws ParseException {
        System.out.println(DateToIntUtil.dateToDays(new Date()));
        System.out.println(DateToIntUtil.daysToDate(0));
        System.out.println(DateToIntUtil.daysToDate(1));
        System.out.println(DateToIntUtil.daysToDate(18799));
        System.out.println(DateToIntUtil.daysToDate(Integer.MAX_VALUE));
    }

    @Test
    public void Test02() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = simpleDateFormat.parse("2021-07-05 00:00:00");
        Date date2 = simpleDateFormat.parse("2021-07-05 00:00:01");
        Date date3 = simpleDateFormat.parse("2021-07-05 01:00:00");
        Date date4 = simpleDateFormat.parse("2021-07-05 02:00:00");
        Date date5 = simpleDateFormat.parse("2021-07-05 12:00:00");
        Date date6 = simpleDateFormat.parse("2021-07-05 13:00:00");
        Date date7 = simpleDateFormat.parse("2021-07-05 23:59:59");
        System.out.println(DateToIntUtil.dateToDays(date1));
        System.out.println(DateToIntUtil.dateToDays(date2));
        System.out.println(DateToIntUtil.dateToDays(date3));
        System.out.println(DateToIntUtil.dateToDays(date4));
        System.out.println(DateToIntUtil.dateToDays(date5));
        System.out.println(DateToIntUtil.dateToDays(date6));
        System.out.println(DateToIntUtil.dateToDays(date7));
    }

}
