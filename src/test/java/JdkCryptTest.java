import com.alipay.api.internal.util.codec.Base64;

import java.nio.charset.StandardCharsets;

class JdkCryptTest {

    public static void main(String[] args) throws Exception {
        String  plainText =  "{\"phoneNumber\":\"13580006666\",\"purePhoneNumber\":\"13580006666\",\"countryCode\":\"86\",\"watermark\":{\"appid\":\"xxxxxxxxxxxxxxxxxxx\",\"timestamp\": xxxxxx}}";
        String  exp = JdkCrypt.encrypt(plainText, AliSdk.secret);
        System.out.println(exp);
        //exp = "77nJA4H9QdqeJqyZPvPHUcMSWoqqGBEtDPoCdNZBBv4QilbY2E/S/cuUFT5QjoBon1VVP276CBBfVEVzbaEGwCsfFLZ5F7tdiAwwyfUordP3fNWtEksJCwGqueVW+B7bvECUd+OfeiubaYQzE+HpHl/N9rCfsodErWG3xj3BhVg=";
        byte [] plainTextb = Base64.decodeBase64(exp.getBytes());

        byte[] iv = new byte[16];
        byte[] plainTextbyte = new byte[plainTextb.length -16];
        System.arraycopy(plainTextb, 0, iv, 0, 16);
        System.arraycopy(plainTextb, 16, plainTextbyte, 0, plainTextb.length-16);

        System.out.println(JdkCrypt.decrypt(exp, AliSdk.secret, "UTF-8"));


        Util.printByteArray("iv", iv);
        byte[] ivBS = Base64.encodeBase64(iv);
        byte[] plainTextbytebs = Base64.encodeBase64(plainTextbyte);

        System.out.println(JdkCrypt.decryptWithIv(new String(plainTextbytebs),AliSdk.secret, new String(ivBS)));

    }

}
