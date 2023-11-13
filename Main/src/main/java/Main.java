import java.util.Date;

/**
 * @author liangzhirong
 * @date 2021/8/26
 */
public class Main {
    public static void main(String[] args) {
        getDateFixDateTest();
    }

    public static void getDateFixDateTest() {
        Date now = new Date();
        Date resultDate = DateUtil.getDateFixDate(now, 9, 30, 0, 0);
        System.out.println(resultDate);
        resultDate = DateUtil.getDateFixDate(resultDate, 0, 0, 0, 0);
        resultDate = DateUtil.getDateFixDate(resultDate, 21, 59, 22, 0);
        resultDate = DateUtil.getDateFixDate(resultDate, 23, 0, 0, 0);

    }
}
