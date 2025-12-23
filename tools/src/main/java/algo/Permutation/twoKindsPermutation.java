package algo.Permutation;

/**
 * n个红球，n个白球，给出所有的排列
 */
public class twoKindsPermutation {


    public static void main(String[] args) {
        permutation(2, 2, "");
    }

    private static void permutation(int redCount, int whiteCount, String suffix) {
        if (redCount > 0) {
            permutation(redCount - 1, whiteCount, suffix + "r");
        }

        if (whiteCount > 0) {
            permutation(redCount, whiteCount - 1, suffix + "w");
        }

        if (redCount == 0 && whiteCount == 0) {
            System.out.println(suffix);
        }
    }
}
