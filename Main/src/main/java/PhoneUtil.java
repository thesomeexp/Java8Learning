/**
 * @author liangzhirong
 * @date 2021/6/24
 */
public class PhoneUtil {

    private static final int PHONE_LENGTH = 11;

    private static final char MASK_CHAR = '*';

    /**
     * 手机号脱敏
     *
     * @param phoneNumber
     * @return
     */
    public static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != PHONE_LENGTH) {
            throw new RuntimeException("手机号不能为空或者长度有误");
        }
        char[] chars = phoneNumber.toCharArray();
        chars[3] = MASK_CHAR;
        chars[4] = MASK_CHAR;
        chars[5] = MASK_CHAR;
        chars[6] = MASK_CHAR;
        return String.valueOf(chars);
    }

    public static void main(String[] args) {
        String phone = "13025562312";
        System.out.println(phone.hashCode());
        String afterMask = maskPhoneNumber(phone);
        System.out.println(afterMask);
        System.out.println(afterMask.hashCode());
    }

}
