package utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * @author liangzhirong
 * @date 2021/6/28
 */
public class JwtUtil {

    /**
     * 生成jwt token
     */
    public static String generateToken(String au, long expirationSecond, String secret) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expirationSecond * 1000);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(au)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static String generateToken(Map<String, Object> claims, long expirationSecond, String signingKey) {
        // 自动添加token的创建时间
        long createTime = System.currentTimeMillis();
        Date expirationAt = new Date(createTime + expirationSecond * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject("api")
                .setIssuedAt(new Date())
                .setExpiration(expirationAt)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }

}
