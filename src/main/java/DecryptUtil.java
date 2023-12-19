import com.alipay.api.AlipayApiException;

public interface  DecryptUtil {

    void decryptWithIv(String contentText, String ivText) throws Exception;
}
