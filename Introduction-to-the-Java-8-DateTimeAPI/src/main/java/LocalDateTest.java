import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * @author liangzhirong
 * @date 2021/9/1
 */
public class LocalDateTest {

    public static void main(String[] args) {
        Test_LocalDate_Example();
        Test_LocalTime_Example();
        Test_LocalDateTime_Example();
        Test_ZonedDateTime_Example();
        Test_Period_Example();
        Test_Duration_Example();
        Test_DateCalendar_Example();
        Test_Formatting_Example();
    }

    // 例子来自: https://www.baeldung.com/java-8-date-time-intro
    public static void Test_LocalDate_Example() {
        // 根据系统时钟创建一个当前实例
        LocalDate localDate = LocalDate.now();

        // 解析特定时间
        LocalDate parseDate = LocalDate.of(2015, 02, 20);
        parseDate = LocalDate.parse("2015-02-20");

        // 获取当前时间再加一天
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // 获取当前时间再减一个月
        LocalDate previousMonthSameDay = LocalDate.now().minus(1, ChronoUnit.MONTHS);

        // 解析 date, 获取 the day of the week (返回 DayOfWeek)和 the day of the month (返回 int)
        DayOfWeek sunday = LocalDate.parse("2016-06-12").getDayOfWeek();
        int twelve = LocalDate.parse("2016-06-12").getDayOfMonth();

        // 是否是 闰年
        boolean leapYear = LocalDate.now().isLeapYear();

        // 日期间的关系, 是否在之前/之后
        boolean notBefore = LocalDate.parse("2016-06-12")
                .isBefore(LocalDate.parse("2016-06-11"));
        boolean isAfter = LocalDate.parse("2016-06-12")
                .isAfter(LocalDate.parse("2016-06-11"));

        // 获取边界
        // 获取一天的开始时间 和 一个月的开始时间
        LocalDateTime beginningOfDay = LocalDate.parse("2016-06-12").atStartOfDay();
        LocalDate firstDayOfMonth = LocalDate.parse("2016-06-12")
                .with(TemporalAdjusters.firstDayOfMonth());
    }

    public static void Test_LocalTime_Example() {
        // 当前
        LocalTime now = LocalTime.now();

        // 解析
        LocalTime sixThirty = LocalTime.parse("06:30");

        // 使用工厂方法
        sixThirty = LocalTime.of(6, 30);

        // 加时
        LocalTime sevenThirty = LocalTime.parse("06:30").plus(1, ChronoUnit.HOURS);

        // 多种获取特定时间单位方法
        int six = LocalTime.parse("06:30").getHour();

        // 比较
        boolean isbefore = LocalTime.parse("06:30").isBefore(LocalTime.parse("07:30"));

        // 边界
        LocalTime maxTime = LocalTime.MAX;
    }

    public static void Test_LocalDateTime_Example() {
        // 当前
        LocalDateTime now = LocalDateTime.now();

        // 解析
        LocalDateTime parseDate = LocalDateTime.of(2015, Month.FEBRUARY, 20, 06, 30);
        parseDate = LocalDateTime.parse("2015-02-20T06:30:00");

        // 增加/减少
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(2);

        // 获取时间单元
        Month month = localDateTime.getMonth();
    }

    public static void Test_ZonedDateTime_Example() {
        // 巴黎时区
        ZoneId zoneId = ZoneId.of("Europe/Paris");

        // 获取所有时区 id
        Set<String> allZoneIds = ZoneId.getAvailableZoneIds();

        // LocalDateTime 可以转换成特定时区
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), zoneId);

        // 解析带时区的时间
        ZonedDateTime parseTime = ZonedDateTime.parse("2015-05-03T10:15:30+01:00[Europe/Paris]");

        // -------- 时区的其它方式

        // OffsetDateTime
        LocalDateTime localDateTime = LocalDateTime.of(2015, Month.FEBRUARY, 20, 06, 30);
        // 创建一个 ZoneOffset 来增加 2 小时
        ZoneOffset offset = ZoneOffset.of("+02:00");
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, offset);
    }

    public static void Test_Period_Example() {
        // Period 表示以 年, 月, 日 为单位的时间量
        // Period 广泛用于修改给定日期的值, 或者获取两个日期之间的差异
        LocalDate initialDate = LocalDate.parse("2007-05-10");
        // 使用 Period 操作日期
        LocalDate finalDate = initialDate.plus(Period.ofDays(5));

        // Period 有很多获取时间单元的方法
        int five = Period.between(initialDate, finalDate).getDays();
        // 我们也可以使用 ChronoUnit.between 来获取给定的单元时间间隔
        long five_long = ChronoUnit.DAYS.between(initialDate, finalDate);
    }

    public static void Test_Duration_Example() {
        // Duration 表示以 秒, 纳秒 为单位的时间量
        // 间隔修改
        LocalTime initialTime = LocalTime.of(6, 30, 0);
        LocalTime finalTime = initialTime.plus(Duration.ofSeconds(30));

        // 两种方式获取间隔
        long thirty = Duration.between(initialTime, finalTime).getSeconds();
        long thirty_2 = ChronoUnit.SECONDS.between(initialTime, finalTime);
    }

    public static void Test_DateCalendar_Example() {
        // 对 Date 和 Calendar 的兼容
        // 转换到新的 API
        LocalDateTime fromDate = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        LocalDateTime fromCalendar = LocalDateTime.ofInstant(Calendar.getInstance().toInstant(), ZoneId.systemDefault());
        // 对 epoch seconds 的兼容
        LocalDateTime fromEpoch = LocalDateTime.ofEpochSecond(1465817690, 0, ZoneOffset.UTC);
    }

    public static void Test_Formatting_Example() {
        // ISO 格式
        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        // 自定义格式
        String customDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // 可的格式样式
        String optionalDateString = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.UK));
    }
}
