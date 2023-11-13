import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * @author liangzhirong
 * @date 2021/7/9
 */
public class JwtUtil {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String CLAIM_KEY_CREATEDTIME = "CreatedTime";

    /**
     * Token缺省过期时间是30分钟
     */
    private static final Long TOKEN_EXPIRATION = 1800000L;
    /**
     * 缺省情况下，Token会每5分钟被刷新一次
     */
    private static final Long REFRESH_TOKEN_INTERVAL = 300000L;

    /**
     * 生成加密后的JWT令牌，生成的结果中包含令牌前缀，如"Bearer "
     *
     * @param claims           令牌中携带的数据
     * @param expirationSecond 过期的秒数
     * @return 生成后的令牌信息
     */
    public static String generateToken(Map<String, Object> claims, Date issDate, long expirationSecond, String signingKey) {
        // 自动添加token的创建时间
        long createTime = issDate.getTime();
        Date expirationAt = new Date(createTime + expirationSecond * 1000);
        claims.put(CLAIM_KEY_CREATEDTIME, createTime);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject("api")
                .setIssuedAt(issDate)
                .setExpiration(expirationAt)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }

    /**
     * 生成加密后的JWT令牌，生成的结果中包含令牌前缀，如"Bearer "
     *
     * @param claims 令牌中携带的数据
     * @return 生成后的令牌信息
     */
    public static String generateToken(Map<String, Object> claims, String signingKey) {
        return generateToken(claims, new Date(), TOKEN_EXPIRATION, signingKey);
    }

    /**
     * 获取token中的数据对象
     *
     * @param token 令牌信息(需要包含令牌前缀，如"Bearer:")
     * @return 令牌中的数据对象，解析失败返回null。
     */
    public static Claims parseToken(String token, String signingKey) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        String tokenKey = token.substring(TOKEN_PREFIX.length());
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(tokenKey).getBody();
        } catch (Exception e) {
            System.out.println("JWT 过期或签名验证失败, 异常信息: " + e.getMessage());
        }
        return claims;
    }

    /**
     * 判断令牌是否过期
     *
     * @param claims 令牌解密后的Map对象。
     * @return true 过期，否则false。
     */
    public static boolean isNullOrExpired(Claims claims) {
        return claims == null || claims.getExpiration().before(new Date());
    }

    /**
     * 判断解密后的Token payload是否需要被强制刷新，如果需要,则调用generateToken方法重新生成Token。
     *
     * @param claims Token解密后payload数据
     * @return true 需要刷新，否则false
     */
    public static boolean needToRefresh(Claims claims) {
        if (claims == null) {
            return false;
        }
        Long createTime = (Long) claims.get(CLAIM_KEY_CREATEDTIME);
        return createTime == null || System.currentTimeMillis() - createTime > REFRESH_TOKEN_INTERVAL;
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private JwtUtil() {
    }
}
