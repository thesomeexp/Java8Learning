import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author liangzhirong
 * @date 2021/7/2
 */
public class DateUtilTest {

    @Test
    public void DateTest() {
        Date now = new Date();
        Date yesterday = DateUtil.getYesterday(now);
        System.out.println(yesterday);
    }

    @Test
    public void parseDateTest() {
        Date date = DateUtil.parse("2021-07-01");
        Date yesterday = DateUtil.getYesterday(date);
        System.out.println(yesterday);

        date = DateUtil.parse("2021-01-01");
        yesterday = DateUtil.getYesterday(date);
        System.out.println(yesterday);
    }

    @Test
    public void dateAddMinutesTest() {
        Date now = new Date();
        Date startDate = DateUtil.getStartDate(now);
        Date triggerDate = DateUtil.addMinutes(startDate, 570);
        if (now.getTime() > triggerDate.getTime()) {
            System.out.println("大于");
        }
        if (startDate.getTime() > triggerDate.getTime()) {
            System.out.println("大于");
        }
        Date startDate2 = DateUtil.addMinutes(startDate, 400);
        if (startDate2.getTime() > triggerDate.getTime()) {
            System.out.println("大于");
        }
        System.out.println(triggerDate);
    }

}
