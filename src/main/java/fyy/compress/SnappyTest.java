package fyy.compress;

import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * 性能:压缩200M耗时1秒,反压也是1秒
 * Created by chris on 18/4/18.
 */
public class SnappyTest {

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis() + ":start");
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 200 * 1000 * 1000; i++) {//200M
            input.append("0");
        }
        System.out.println(System.currentTimeMillis() + ":" + input.length());
        byte[] bytes = compressHtml(input.toString());
        System.out.println("before:" + bytes.length);
        System.out.println(System.currentTimeMillis() + ":" + input.length());
        String output = decompressHtml(bytes);
        System.out.println("after:" + output.getBytes().length);
        System.out.println(System.currentTimeMillis() + ":" + output.length());
    }

    public static byte[] compressHtml(String html) {
        try {
            return Snappy.compress(html.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static String decompressHtml(byte[] bytes) {
        try {
            return new String(Snappy.uncompress(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
