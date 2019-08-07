package algo.Permutation;


import com.google.common.collect.Lists;

import java.util.List;

/**
 * 给一个数组，列出这个数组的全排列
 *
 * input: {1,2,3}
 * output: 123,132,213,231,312,321
 */
public class AllPermutation {

    private static int count = 0;

    public static void getAllPermutations(List<Integer> input, String prefix) {
        for (int i = 0; i < input.size(); i++) {
            Integer currentValue = input.get(i);
            List<Integer> listButI = removeFromListAt(input, i);
            if(listButI.size() == 0) {
                System.out.println(prefix + currentValue);
                count++;
            }

            getAllPermutations(listButI, prefix + currentValue);

        }
    }

    public static List<Integer> removeFromListAt(List<Integer> input, int index) {
        List<Integer> list = Lists.newArrayList(input);
        list.remove(index);
        return list;
    }

    public static void main(String[] args) {
        List<Integer> input = Lists.newArrayList(1, 2, 3, 4, 5);
        AllPermutation.getAllPermutations(input, "");
        System.out.println("count:" + count);
    }
}
