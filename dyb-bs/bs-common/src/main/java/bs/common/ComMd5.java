package bs.common;
import bs.common.encryptor.Encryptor;
import java.io.File;

/**
 * MD5加密类
 */
public final class ComMd5 extends Encryptor {
    /** 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合 */
    private static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * 获取文件的 MD5 签名
     *
     * @param file 文件
     * @return 文件的 MD5 签名
     */
    public static String getString(File file) {
        byte[] bytes = digest(file, MD5);
        return bufferToHex(bytes);
    }

    /**
     * 对字符串进行 MD5 加密
     *
     * @param str 要加密的字符串
     * @return 加密后的密文
     */
    public static String getString(String str) {
        byte[] bytes = digest(str, MD5);
        return bufferToHex(bytes);
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        // 取字节中高 4 位的数字转换为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}