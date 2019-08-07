package algo.Permutation;

/**
 * 一个人一次只能吃1或2或3个面包，给n个面包，输出所有吃法
 */
public class BreadPermutation {


    public static void main(String[] args) {
        getAllPermutation(4, "");
    }

    private static void getAllPermutation(int n, String suffix) {
        if (n >= 3) {
            getAllPermutation(n - 3, suffix + "3");
        }
        if (n >= 2) {
            getAllPermutation(n - 2, suffix + "2");
        }
        if (n >= 1) {
            getAllPermutation(n - 1, suffix + "1");
        }
        if(n == 0) {
            System.out.println(suffix);
        }
    }
}
