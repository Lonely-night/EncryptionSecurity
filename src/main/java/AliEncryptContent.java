import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipayEncrypt;
import com.alipay.api.internal.util.codec.Base64;

import javax.crypto.Cipher;
import java.util.Arrays;

public class AliEncryptContent {



    public static void main(String[] args) throws Exception {
        String cipherText = "9DQb8FzGvyoYgr82P2vjAnf4ps5Pb8gY/oTYyyK7IrxN63O6rRjsVu+uzsDSOHCAckpkNinCZXVjs9cDyNEtllAHESvJPID6vowP7iuqrUc=";
        StringBuilder plainText = new StringBuilder("plainText : ");
        // ali sdk 中 vi 加密使用vi都是 000000000000
        byte[] ivAndEncryptedText = Util.concat(Util.initIv(), Base64.decodeBase64(cipherText.getBytes()));
        int cbcBlackNum = ivAndEncryptedText.length/16;
        byte[] iv;
        byte[] cipherTextBlock; // 密文块
        byte[] plainTexBlock = new byte[16]; // 明文块
        byte[] mvi; // 中间值
        for(int i=0; i < cbcBlackNum-1; ++i ){
            // cbc加密算法每16位一组，
            // 前面的作为vi，后面的是密文。
            iv = Arrays.copyOfRange(ivAndEncryptedText, i*16, i*16 +16);
            cipherTextBlock = Arrays.copyOfRange(ivAndEncryptedText, (i+1)*16, (i+1)*16 +16);
            mvi = Util.getMvi(cipherTextBlock);
            Util.printByteArray("mvi", mvi);
            for (int j = 0; j < 16; j++) plainTexBlock[j] = (byte)(iv[j] ^ mvi[j]);
            System.out.print(" expr (" + new String(plainTexBlock) + "");
            plainText.append(new String(plainTexBlock));
        }
        System.out.println(plainText.toString());
    }



}
