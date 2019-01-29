package mock.powermock;

public class Utils {
    private static String URL_STRING = "http://www.baidu.com.cn";

    private static void deal() {
        System.out.println("deal() " + URL_STRING);
    }

    public static String getObject(Demo object, String str) {
        deal();
        System.out.println("getObject() ");
        return URL_STRING + str;
    }
}