import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author liangzhirong
 * @date 2021/7/6
 */
public class LocalDateTimeUtilsTest {

    @Test
    public void parseTest01() {
        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTimeUtils.parse(date);
        System.out.println(localDateTime);
    }

}
