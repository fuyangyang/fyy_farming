package leetcode;

/**
 * Symbol       Value
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 *
 *
 * I can be placed before V (5) and X (10) to make 4 and 9.
 * X can be placed before L (50) and C (100) to make 40 and 90.
 * C can be placed before D (500) and M (1000) to make 400 and 900.
 *
 *
 * input: from 1 to 3999
 */
public class Roman2Integer {

    public int romanToInt(String s) {
        int sum = 0;
        int len = s.length();
        byte[] input = s.getBytes();
        for (int i = 0; i < len; i++) {
            byte cur = input[i];
            if(cur == 'I') {

            } else if(cur == 'X') {

            } else if(cur == 'C') {

            } else {
                sum += getValue(cur);
            }

        }

        return sum;
    }

    private int getValue(byte b) {
        if(b == 'I') {
            return 1;
        } else if(b == 'V') {
            return 5;
        } else if(b == 'X') {
            return 10;
        } else if(b == 'L') {
            return 50;
        } else if(b == 'C') {
            return 100;
        } else if(b == 'D') {
            return 500;
        } else if(b == 'M') {
            return 1000;
        }
        throw new IllegalStateException(String.valueOf(b) + " not supported!");
    }


    public static void main(String[] args) {

    }
}
