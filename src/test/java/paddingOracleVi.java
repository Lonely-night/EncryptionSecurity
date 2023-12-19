import com.alipay.api.internal.util.codec.Base64;

import java.util.Arrays;

public class paddingOracleVi {

    public static void main(String[] args) {
        byte[] miv = new byte[16];
        String exp = "1 It's one-way because once you encrypt something, you can never get it";
        byte[] plainTex = Util.paddingPlainText(exp);
        Util.printByteArray("plainTex", plainTex);
        String cipherText = "mvKYnofUMM7/SmEDI3WWD2MpJMEXP82hwBo877/nVKLUCBtnwX2WS9hZU/70SwpcbaRzOB+FVZtaEVhH+pA44Sx8G0LMo5qv/0LBaqp7pPdpUnZ9M6etCGWetxu/euv8y4qNklqcnm7Ii7NPhSJiJg==";
        byte[] ivAndEncryptedText = Base64.decodeBase64(cipherText.getBytes());
        Util.printByteArray("ivAndEncryptedText", ivAndEncryptedText);
        for(int i =0; i < ivAndEncryptedText.length/16; i++){
            miv = getMiv(
                    Arrays.copyOfRange(ivAndEncryptedText, i*16, i*16 +16),
                    Arrays.copyOfRange(plainTex, i*16, i*16 +16)
                );
            Util.printByteArray("第" + i+"组mvi为", miv);
        }
    }

    // vi xor mvi = pliant
    // vi xor mvi xor vi = pliant xor vi = mvi
    private static byte[]  getMiv(byte[] iv, byte[]  pliant){
        byte[] miv = new byte[16];
        Util.printByteArray("iv", iv);
        Util.printByteArray("pliant", pliant);
        for (int j = 0; j < 16; j++) miv[j] = (byte)(iv[j] ^ pliant[j]);
        return miv;
    }
}
