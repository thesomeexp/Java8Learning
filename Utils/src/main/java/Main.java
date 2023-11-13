import utils.JwtUtil;

/**
 * @author liangzhirong
 * @date 2021/6/28
 */
public class Main {

    public static void main(String[] args) {
        Long expiration = 7200L;
        String secret = "";

        String jwt = JwtUtil.generateToken("1", expiration, secret);
        System.out.println(jwt);
//        BlockingQueue
    }

}
