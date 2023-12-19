public class testAlisdk {
    public static void main(String[] args) throws Exception{
        String  plainText =  "It's one-way because once you encrypt something, you can never get it";
        //String  plainText =  "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
        System.out.println(AliSdk.encryptPhone(plainText));




    }
}
