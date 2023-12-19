import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipayEncrypt;

public class AliSdk {
    public static String secret = "000102030405060708090a0b0c0d0e0f";

    public static String decryptPhone(String encryptedPhone) throws Exception {

        String plainData = AlipayEncrypt.decryptContent(encryptedPhone, AlipayConstants.ENCRYPT_TYPE_AES, secret, AlipayConstants.CHARSET_UTF8);

        return plainData;
    }

    public static String encryptPhone(String encryptedPhone) throws Exception {

        String plainData = AlipayEncrypt.encryptContent(encryptedPhone, AlipayConstants.ENCRYPT_TYPE_AES, secret, AlipayConstants.CHARSET_UTF8);

        return plainData;
    }
}
