import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * @author liangzhirong
 * @date 2021/6/21
 */
public class UrlEncodeUtil {

    public static void main(String[] args) {
        String encode = UrlEncodeUtil.base64Encode("hehlgjslgsldfjl");
        System.out.println(UrlEncodeUtil.base64Decode(encode));

    }

    /**
     * URL 编码
     */
    public static String encode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException("URL编码工具编码异常");
        }
    }

    /**
     * URL 解码
     */
    public static String decode(String encodeUrl) {
        try {
            return URLDecoder.decode(encodeUrl, "UTF-8");
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException("URL编码工具解码异常");
        }
    }

    /**
     * Base64 编码
     */
    public static String base64Encode(String plainText) {
        return Base64.getEncoder().encodeToString(plainText.getBytes());
    }

    /**
     * Base64 解码
     */
    public static String base64Decode(String encodeText) {
        return new String(Base64.getDecoder().decode(encodeText));
    }


}
