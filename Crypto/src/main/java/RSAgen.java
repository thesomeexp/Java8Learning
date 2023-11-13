import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author liangzhirong
 * @date 2/15/2022
 */
@Slf4j
public class RSAgen {
    public static void main(String[] args) {
        try {
            generateWithHutool();
        } catch (Exception e) {
            log.error("异常: ", e);
        }
    }

    private static void generateWithHutool() throws Exception {
        RSA rsa = new RSA();

//获得私钥
        rsa.getPrivateKey();
        log.info("RSA 私钥: {}", rsa.getPrivateKeyBase64());
//获得公钥
        rsa.getPublicKey();
        log.info("RSA 公钥: {}", rsa.getPublicKeyBase64());

//公钥加密，私钥解密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);

        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
        StrUtil.equals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

//私钥加密，公钥解密
        byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
        String base64encode = Base64.encode(encrypt2);
        log.info("加密密文: {}", base64encode);
        byte[] base64decode = Base64.decode(base64encode);
        byte[] decrypt2 = rsa.decrypt(base64decode, KeyType.PublicKey);
        String decodeData = StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8);
        log.info("解密明文: {}", decodeData);
        StrUtil.equals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
    }

    private static void generateRSA() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        try (FileOutputStream fos = new FileOutputStream("public.key")) {
            fos.write(publicKey.getEncoded());
        }

    }
}
