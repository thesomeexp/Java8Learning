import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liangzhirong
 * @date 2021/7/9
 */
public class JwtUtilTest {

    @Test
    public void verifyJwt() {
        String key = "9f8088b1-66b6-416d-b46c-49cca62972e5";
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhcGkiLCJDcmVhdGVkVGltZSI6MTYyNTgyMjg1NTUwMSwiZXhwIjoxNjI1ODMwMDU1LCJpYXQiOjE2MjU4MjI4NTUsImF1dGhJZCI6MX0.E4GaJTw1OCBOUu7gGr9OLy749pJ8oCXebjTRkZHgl5jLHZ4PHy6buwPqgUOxX-hPcYIfZur4OYCvEBaa-REzJw";
        String jwt = "Bearer " + token;
        Claims c = JwtUtil.parseToken(jwt, key);

        System.out.println(c.getIssuedAt());
        System.out.println(c.get("authId", Long.class));
    }

    @Test
    public void generateJwt() {
        String key = "9f8088b1-66b6-416d-b46c-49cca62972e5";
        Date date = DateUtil.parse("2021-07-09 12:00:00");

        Map<String, Object> claims = new HashMap<>(1);
        claims.put("authId", 1L);
        
        String token = JwtUtil.generateToken(claims, date, 7200, key);
        System.out.println(token);
    }

}
