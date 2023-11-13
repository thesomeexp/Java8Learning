import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

/**
 * @author liangzhirong
 * @date 2021/7/6
 */
public class LocalDateTimeUtils {

    /**
     * 获取系统时区
     */
    public static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();

    /**
     * 解析时间格式
     */
    public static final String DEFAULT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认格式器
     */
    public static final DateTimeFormatter DEFAULT_FORMATTER;

    static {
        DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);
    }

    public static LocalDateTime parse(Date date) {
        try {
            return date.toInstant().atOffset(ZONE_OFFSET).toLocalDateTime();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 获取UNIX时间(精确到纳秒)
     *
     * @param localDateTime 非空
     * @return
     */
    public static Long toEpochMilli(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        return localDateTime.toInstant(ZONE_OFFSET).toEpochMilli();
    }

    /**
     * 获取当前小时开始时间(精确到纳秒)
     *
     * @param localDateTime
     * @return
     */
    public static Long toHourStart(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        localDateTime = localDateTime.truncatedTo(ChronoUnit.HOURS);
        return toEpochMilli(localDateTime);
    }

}
