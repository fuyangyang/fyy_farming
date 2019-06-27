package leetcode;

public class PalindromeNumber {
    public static boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        byte[] num = String.valueOf(x).getBytes();
        for(int i = 0; i < num.length / 2; i++) {
            if(num[i] != num[num.length - 1 - i]) {
                return false;
            }
        }
        return true;

    }

    public static void main(String[] args) {
        System.out.println("12321:" + PalindromeNumber.isPalindrome(12321));
        System.out.println("-123:" + PalindromeNumber.isPalindrome(-123));
        System.out.println("10:" + PalindromeNumber.isPalindrome(10));
    }

}
