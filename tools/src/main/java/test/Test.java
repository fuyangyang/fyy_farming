package test;

/**
 * Created by fuyi on 17/4/5.
 */
public class Test {

    public static int add(int first, int second) {
        for (int i = 0; i < 10; i++) {
            boolean b = "sdafdsafdsaf".substring(0, 2).contains("23");
            boolean c = "sdasdaffdsafdsaf".substring(0, 2).contains("23");
            if (b && c) {
                System.out.println("contains");
            }

        }
        return first + second;
    }

    public static void main(String[] args) {
        System.out.println("start: " + System.currentTimeMillis());
        for (int i = 0; i < 10000000; i++) {
            add(1, i + 1);
        }
        System.out.println("stop:  " + System.currentTimeMillis());
    }
}
