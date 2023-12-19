import com.alipay.api.internal.util.codec.Base64;

import java.util.Arrays;

public class TestBianma {

    public static void main(String[] args) {
        ////String cipherText = "Nd0EE44P0erYljZF4G1XMohfdUgJ2QNgSmpiN9FTKNrwkmkbZUvSFG/IOx2jKcUF41d7MNipbRxi/T61JDZB3+Xf0ULX64j7Rvokq5P+gBetT1gR6OquJI2WtZThFwZoHrmIY2HCiH2e1hgnvZsaGBi4YdGTgubnzsHSQnXJ5jiqNZkTPEwBl1rjpPvq2aZ8jEu+bqb+l3yIXZAG0qtV2g==";
        //String cipherText = "Xz9Leq8hfMYQDer4XFUG7SzjTDOG9hifueu9d7xgTfQLWxPHdVTNMrAIftu9rPcKMoUzpO7pK/+3GiHKlsmyxTxb8NKOjlLTEiwciZWL/8MT/iZD8Xd6rTk3PqMQIT4XA00fOl1QADT5jplTvReY+jAhAewWSuBrpW1ydp2WXjtJSP9ibkWKVxMKXrlqf+dr";
        //StringBuilder plainText = new StringBuilder("plainText : ");
        //byte[] ivAndEncryptedText = Base64.decodeBase64(cipherText.getBytes());
        //System.out.println(ivAndEncryptedText.length);

        String rawE1 = "Xz9Leq8hfMYQDer4XFUG7SzjTDOG9hifueu9d7xgTfQLWxPHdVTNMrAIftu9rPcKMoUzpO7pK/+3GiHKlsmyxTxb8NKOjlLTEiwciZWL/8MT/iZD8Xd6rTk3PqMQIT4XA00fOl1QADT5jplTvReY+jAhAewWSuBrpW1ydp2WXjtJSP9ibkWKVxMKXrlqf+dr";
        String rawE2 = "TfaKCfNj1EWn7wDIaVTolZEPDwdy2mudjS/VlHtVVT1LxTQ+OPGZsF5aPjpgejG4TwJUhjQmkWHsHXLbqbi2STKFM6Tu6Sv/txohypbJssU8W/DSjo5S0xIsHImVi//DE/4mQ/F3eq05Nz6jECE+FwNNHzpdUAA0+Y6ZU70XmPowIQHsFkrga6Vtcnadll47SUj/Ym5FilcTCl65an/naw==";
        byte[] bRaw1 = Base64.decodeBase64String(rawE1);
        byte[] bRaw2 = Base64.decodeBase64String(rawE2);
        Util.printByteArray("bRaw1", bRaw1);
        Util.printByteArray("bRaw2", bRaw2);
        byte[]  brawblock1;
        byte[]  brawblock2;
        for( int i=10 ; i > 1 ; i--){
            brawblock1 = Arrays.copyOfRange(bRaw1, (i-1) * 16, i * 16);
            brawblock2 = Arrays.copyOfRange(bRaw2, i * 16, i * 16 +16);
            System.out.println(" " + i);
            Util.printByteArray("brawblock1", brawblock1 );
            Util.printByteArray("brawblock2", brawblock2 );

        }


    }
}
