import com.alipay.api.internal.util.codec.Base64;

import javax.swing.*;
import java.net.URLDecoder;
import java.util.Arrays;

public class DecryptTest {
    public static void main(String[] args) throws Exception {
        //String encryptData="r2b0ZI4Wea%2B7atAuEEmEfUKvsXrq23%2F3SSRHdhojtj5whNHSXcvl1%2BIHt0KahE6aYhVUdG9NtsmeQMe3AkKxw%2FYq4dgxTsyJnmZsZFZtpenlW%2FW%2B2Nl7r0zE4t2vbJKESF9cah7DZkNcQWZHj1cMmt0ncM89LsPaQqNJu3bLQO12%2BnILHJwAv7VqBkwUW2nzJOU7TshwbfeKWVel6ndGMA%3D%3D";
        //String encryptData="3Lsba1ODkbcTVot/RZb2f6m1MlMrOKRBwQi9bBwYAKCXO7BUJQCp4lg/YnwcLK09id0r9SCc4WzC5MM1hqefD+89QxAEP0r1GZGuUYrA01XZn+R/dPAGII7PD41edC65aBxBdax7tYEvOlfEKt4PCxA4SNCfBpTqARVX77dLVVHrAc71POrN0OQw2B9KnG5bQzxeSihG1iQS53z95l6i4Q==";
        //String encryptData="CftAHbkZ%2BRQ%2FwTBIkkgfuHPo%2BK%2FQV%2F%2Fx9KOA0HPtVANdtrlmcuGIsrZg0V3Ax5fepShSq506kQgQwEOp%2BA8PP%2FCQSRSIrLlO%2B1D0XoUScwl63IbTgHfiAMTixT%2BVIz%2FUHf0kOzBSvHClgtMrqJZB9F0KZi%2BUrlyYiVYVM4qYxF95Xv%2Fjmy%2B6%2Bk%2FSPLqGTyYnpPWwCpqedISr5P3EDuLWBw%3D%3D";
        //encryptData = URLDecoder.decode(encryptData);
        //byte[] encryptByte= Base64.decodeBase64String(encryptData);
        //
        ////byte[]  encryptByte = encryptData.getBytes();
        //System.out.println("encryptByte.length: " +  encryptByte.length);
        //
        //String ivStr = "aUoY6AlgksSKZxwWXGBDyw%3D%3D";
        //ivStr = URLDecoder.decode(ivStr);
        //byte[] ivByte= Base64.decodeBase64String(ivStr);
        ////byte[]  ivByte = ivStr.getBytes("UTF-8");
        //System.out.println("iv: " + ivByte.length);
        String  rawC1 = "3Lsba1ODkbcTVot%2FRZb2f6m1MlMrOKRBwQi9bBwYAKCXO7BUJQCp4lg%2FYnwcLK09id0r9SCc4WzC5MM1hqefD%2B89QxAEP0r1GZGuUYrA01XZn%2BR%2FdPAGII7PD41edC65aBxBdax7tYEvOlfEKt4PCxA4SNCfBpTqARVX77dLVVHrAc71POrN0OQw2B9KnG5bQzxeSihG1iQS53z95l6i4Q%3D%3D";
        rawC1 =  URLDecoder.decode(rawC1);
        byte[] rawC1Bytes = rawC1.getBytes();
        Util.printByteArray("rawC1Bytes", rawC1Bytes);
        int blockNum = rawC1Bytes.length/16;
        //Arrays.copyOf(rawC1Bytes, 4*16, Arrays.copyOfRange)
        Arrays.copyOfRange(rawC1Bytes, 4*16, rawC1Bytes.length);

        System.out.println(rawC1Bytes);
        String  e1 = "{\"phoneNumber\":\"13580006666\",\"purePhoneNumber\":\"13580006666\",\"co";
        byte[] e1WithPadding = e1.getBytes();
        System.out.println("E1 " + e1WithPadding.length );
    }
}
