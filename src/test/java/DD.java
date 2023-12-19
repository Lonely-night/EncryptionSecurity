public class DD {
    public static void main(String[] args) {
        String  str = "{\"phoneNumber\":\"17338137976\",\"purePhoneNumber\":\"17338137976\",\"co";
        System.out.println(str.getBytes().length);
        String hexStr = "5f3f4b7aaf217cc6100deaf85c5506ed2ce34c3386f6189fb9ebbd77bc604df40b5b13c77554cd32b0087edbbdacf70a";
        byte [] bytes = Util.hexToBytes(hexStr);
        System.out.println(bytes.length);

    }
}
