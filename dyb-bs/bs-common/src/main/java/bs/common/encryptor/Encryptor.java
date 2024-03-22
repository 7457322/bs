package bs.common.encryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import org.apache.commons.lang3.StringUtils;

/**
 * 加密、解密的基础类
 */
public class Encryptor {

    /**
     * RFC 1319中定义的MD2消息摘要算法。
     */
    public static final String MD2 = "MD2";

    /**
     * RFC 1321中定义的MD5消息摘要算法。
     */
    public static final String MD5 = "MD5";

    /**
     * Secure Hash Algorithm，安全散列算法
     */
    public static final String SHA = "SHA";

    /**
     * FIPS PUB 180-2中定义的SHA-1散列算法。
     */
    public static final String SHA_1 = "SHA-1";

    /**
     * FIPS PUB 180-2中定义的SHA-256散列算法。
     */
    public static final String SHA_256 = "SHA-256";

    /**
     * FIPS PUB 180-2中定义的SHA-384散列算法。
     */
    public static final String SHA_384 = "SHA-384";

    /**
     * FIPS PUB 180-2中定义的SHA-512散列算法。
     */
    public static final String SHA_512 = "SHA-512";

    private static final String NOT_EXIST_OR_JDK_NO_SUPPORT_MESSAGE = "] 不存在，或当前  JDK 不支持该算法。";

    /**
     * 获取 MessageDigest 实例
     *
     * @param algorithm 加密算法，请参见本类中的名称为 {@code MD*} 和 {@code SHA*} 的常量
     * @return MessageDigest 实例
     */
    public static MessageDigest getMessageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("算法 [" + algorithm + NOT_EXIST_OR_JDK_NO_SUPPORT_MESSAGE, e);
        }
    }

    /**
     * @param data      要加密的数据
     * @param algorithm 加密算法，请参见本类中的名称为 {@code MD*} 和 {@code SHA*} 的常量
     * @return 密文
     */
    public static byte[] digest(byte[] data, String algorithm) {
        if (data == null) {
            return null;
        }

        MessageDigest messageDigest = getMessageDigest(algorithm);
        messageDigest.update(data);
        return messageDigest.digest();
    }

    /**
     * @param data      要加密的字符串
     * @param algorithm 加密算法，请参见本类中的名称为 {@code MD*} 和 {@code SHA*} 的常量
     * @return 密文
     */
    public static byte[] digest(String data, String algorithm) {
        return (data == null) ? null : digest(data.getBytes(StandardCharsets.UTF_8), algorithm);
    }

    /**
     * @param file      要加密的文件
     * @param algorithm 加密算法，请参见本类中的名称为 {@code MD*} 和 {@code SHA*} 的常量
     * @return 密文
     */
    public static byte[] digest(File file, String algorithm) {
        if (file == null) {
            return null;
        }

        MessageDigest messageDigest = getMessageDigest(algorithm);
        try (InputStream input = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int length = 0;
            while ((length = input.read(buffer)) > 0) {
                messageDigest.update(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("文件 [" + file.getAbsolutePath() + "] 不存在。", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("读取文件 [" + file.getAbsolutePath() + "] 发生错误。", e);
        }
        return messageDigest.digest();
    }

    /**
     * 获取密钥生成器实例
     *
     * @param algorithm 加密算法的名称，支持的算法有：
     *                  AES、ARCFOUR、Blowfish、DES、DESede、HmacMD5、HmacSHA1、HmacSHA256、HmacSHA384、HmacSHA512、RC2
     * @return 密钥生成器
     */
    public static KeyGenerator getKeyGenerator(String algorithm) {
        try {
            return KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("获取密钥生成器实例失败：算法 [" + algorithm + NOT_EXIST_OR_JDK_NO_SUPPORT_MESSAGE, e);
        }
    }

    /**
     * 根据算法名称和密钥长度生成私钥公钥对
     *
     * @param algorithm 算法名称，如：RSA、DSA
     * @param size      密钥长度，如：512、1024
     * @return 私钥公钥对
     */
    public static KeyPair getKeyPair(String algorithm, int size) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
            keyGen.initialize(size);
            return keyGen.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("生成私钥公钥对失败：算法 [" + algorithm + NOT_EXIST_OR_JDK_NO_SUPPORT_MESSAGE, e);
        }
    }

    /**
     * 取得算法名称
     *
     * @param transformation 算法模型
     * @return 算法名称
     */
    protected static String getAlgorithmFromTransformation(String transformation) {
        return StringUtils.substringBefore(transformation, "/");
    }


    /**
     * 对数据进行加密
     *
     * @param data           要加密的明文数据
     * @param key            密钥
     * @param transformation 转换-转换的名称，格式是“算法/模式/填充”，例如DES/CBC/PKCS 5填充。有关标准转换名称的信息，请参见《Java加密体系结构参考指南》中的附录A
     *                       <table>
     *                       <caption><h3>下表列出了SunJCE提供程序中可用的密码算法</h3></caption>
     *                       <thead>
     *                       <tr>
     *                       <th>Algorithm Name</th>
     *                       <th>Modes</th>
     *                       <th>Paddings</th>
     *                       </tr>
     *                       </thead>
     *                       <tbody>
     *                       <tr>
     *                       <td>AES</td>
     *                       <td>ECB, CBC, PCBC, CTR, CTS, CFB, CFB8..CFB128, OFB,
     *                       OFB8..OFB128</td>
     *                       <td>NOPADDING, PKCS5PADDING, ISO10126PADDING</td>
     *                       </tr>
     *                       <tr>
     *                       <td>AESWrap</td>
     *                       <td>ECB</td>
     *                       <td>NOPADDING</td>
     *                       </tr>
     *                       <tr>
     *                       <td>ARCFOUR</td>
     *                       <td>ECB</td>
     *                       <td>NOPADDING</td>
     *                       </tr>
     *                       <tr>
     *                       <td>Blowfish, DES, DESede, RC2</td>
     *                       <td>ECB, CBC, PCBC, CTR, CTS, CFB, CFB8..CFB64, OFB,
     *                       OFB8..OFB64</td>
     *                       <td>NOPADDING, PKCS5PADDING, ISO10126PADDING</td>
     *                       </tr>
     *                       <tr>
     *                       <td>DESedeWrap</td>
     *                       <td>CBC</td>
     *                       <td>NOPADDING</td>
     *                       </tr>
     *                       <tr>
     *                       <td>PBEWithMD5AndDES, PBEWithMD5AndTripleDES,
     *                       PBEWithSHA1AndDESede, PBEWithSHA1AndRC2_40</td>
     *                       <td>CBC</td>
     *                       <td>PKCS5Padding</td>
     *                       </tr>
     *                       <tr>
     *                       <td>RSA</td>
     *                       <td>ECB</td>
     *                       <td>NOPADDING, PKCS1PADDING, OAEPWITHMD5ANDMGF1PADDING,
     *                       OAEPWITHSHA1ANDMGF1PADDING, OAEPWITHSHA-1ANDMGF1PADDING,
     *                       OAEPWITHSHA-256ANDMGF1PADDING, OAEPWITHSHA-384ANDMGF1PADDING,
     *                       OAEPWITHSHA-512ANDMGF1PADDING <!-- OAEPPadding later --></td>
     *                       </tr>
     *                       </tbody>
     *                       </table>
     * @return 密文
     */
    public static byte[] encrypt(byte[] data, Key key, String transformation) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new IllegalArgumentException("对数据进行 [" + transformation + "] 加密失败。", e);
        }
    }

    /**
     * 对数据进行解密
     *
     * @param data           要解密的明文数据
     * @param key            密钥
     * @param transformation 转换-转换的名称，格式是“算法/模式/填充”，例如DES/CBC/PKCS 5填充。有关标准转换名称的信息，请参见《Java加密体系结构参考指南》中的附录A。
     *                       <table>
     *                       <caption><h3>下表列出了SunJCE提供程序中可用的密码算法</h3></caption>
     *                       <thead>
     *                       <tr>
     *                       <th>Algorithm Name</th>
     *                       <th>Modes</th>
     *                       <th>Paddings</th>
     *                       </tr>
     *                       </thead>
     *                       <tbody>
     *                       <tr>
     *                       <td>AES</td>
     *                       <td>ECB, CBC, PCBC, CTR, CTS, CFB, CFB8..CFB128, OFB,
     *                       OFB8..OFB128</td>
     *                       <td>NOPADDING, PKCS5PADDING, ISO10126PADDING</td>
     *                       </tr>
     *                       <tr>
     *                       <td>AESWrap</td>
     *                       <td>ECB</td>
     *                       <td>NOPADDING</td>
     *                       </tr>
     *                       <tr>
     *                       <td>ARCFOUR</td>
     *                       <td>ECB</td>
     *                       <td>NOPADDING</td>
     *                       </tr>
     *                       <tr>
     *                       <td>Blowfish, DES, DESede, RC2</td>
     *                       <td>ECB, CBC, PCBC, CTR, CTS, CFB, CFB8..CFB64, OFB,
     *                       OFB8..OFB64</td>
     *                       <td>NOPADDING, PKCS5PADDING, ISO10126PADDING</td>
     *                       </tr>
     *                       <tr>
     *                       <td>DESedeWrap</td>
     *                       <td>CBC</td>
     *                       <td>NOPADDING</td>
     *                       </tr>
     *                       <tr>
     *                       <td>PBEWithMD5AndDES, PBEWithMD5AndTripleDES,
     *                       PBEWithSHA1AndDESede, PBEWithSHA1AndRC2_40</td>
     *                       <td>CBC</td>
     *                       <td>PKCS5Padding</td>
     *                       </tr>
     *                       <tr>
     *                       <td>RSA</td>
     *                       <td>ECB</td>
     *                       <td>NOPADDING, PKCS1PADDING, OAEPWITHMD5ANDMGF1PADDING,
     *                       OAEPWITHSHA1ANDMGF1PADDING, OAEPWITHSHA-1ANDMGF1PADDING,
     *                       OAEPWITHSHA-256ANDMGF1PADDING, OAEPWITHSHA-384ANDMGF1PADDING,
     *                       OAEPWITHSHA-512ANDMGF1PADDING <!-- OAEPPadding later --></td>
     *                       </tr>
     *                       </tbody>
     *                       </table>
     * @return 密文
     */
    public static byte[] decrypt(byte[] data, Key key, String transformation) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new IllegalArgumentException("对数据进行 [" + transformation + "] 解密失败。", e);
        }
    }

}
