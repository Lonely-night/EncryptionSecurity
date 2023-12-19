import com.alipay.api.AlipayApiErrorEnum;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.codec.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class JdkCrypt {

    private static final String AES_CBC_PCK_ALG = "AES/CBC/PKCS5Padding";
    private static final int  ivSize = 16;

    public static String encrypt(String content, String aesKey) throws AlipayApiException {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);

            byte[] iv = new byte[ivSize];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivParams = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey.getBytes(), "AES"), ivParams);

            byte[] encryptedText = cipher.doFinal(content.getBytes());
            byte[] ivAndEncryptedText = new byte[ivSize + encryptedText.length];

            System.arraycopy(iv, 0, ivAndEncryptedText, 0, ivSize);
            System.arraycopy(encryptedText, 0, ivAndEncryptedText, ivSize, encryptedText.length);
            return new String(Base64.encodeBase64(ivAndEncryptedText));
        } catch (Exception e) {
            throw new AlipayApiException(String.format(AlipayApiErrorEnum.ENCRYPT_ASE_V2_ERROR.getErrMsg(),
                content), e);
        }
    }


    public static String decrypt(String content, String key, String charset) throws AlipayApiException {
        try {
            byte[] ivAndEncryptedText = Base64.decodeBase64(content.getBytes());

            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivAndEncryptedText, 0, 16);
            Util.printByteArray("ivParameterSpec", ivParameterSpec.getIV());

            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), ivParameterSpec);
            Util.printByteArray("ivAndEncryptedText", ivAndEncryptedText);

            byte[] contentBytes = cipher.doFinal(ivAndEncryptedText, ivSize, ivAndEncryptedText.length - ivSize);
            return new String(contentBytes, charset);
        } catch (Exception e) {
            throw new AlipayApiException(String.format(AlipayApiErrorEnum.DECRYPT_ASE_V2_ERROR.getErrMsg(),
                content, charset), e);
        }
    }

    public static String decryptWithIv(String contentText, String key, String ivText) throws Exception {
        try {
            byte[] content = Base64.decodeBase64String(contentText);
            byte[] ivParameter = Base64.decodeBase64String(ivText);
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter);

            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), ivParameterSpec);

            byte[] plainText = cipher.doFinal(content);
            return new String(plainText);
        } catch (BadPaddingException e) {
            throw new AlipayApiException(String.format(AlipayApiErrorEnum.DECRYPT_ASE_V2_ERROR.getErrMsg(),
                contentText, "utf-8"), e);
        }
    }
}
