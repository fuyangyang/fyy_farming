package algo.dp.lis;

import java.util.Arrays;

/**
 * 最长递增子序列
 *
 * 重叠子问题
 * 问题是以当前元素结尾的最长递增子序列，子问题是当前元素前面所有比当前元素小的元素对应的最长递增子序列。
 * 最优子结构
 * 当前问题的最优解来自于子问题的最优解，如果所有子问题都不是最优解，那肯定比现在的最优解更好的解。
 *
 */
public class LisSolution {
    public String lis(int[] nums) {
        int size = nums.length;
        if (size < 1) {
            return "input error";
        }

        //最大长度
        int max_length = 1;

        //最长递增子序列
        String max_sublist = String.valueOf(nums[0]);

        //以index元素结尾的子序列的最大长度
        int[] lengthOfLISEndAtI = new int[size];
        lengthOfLISEndAtI[0] = 1;

        //以index元素结尾的子序列中最大升序对应的子序列
        String[] subList = new String[size];
        subList[0] = String.valueOf(nums[0]);

        for (int i = 1; i < size; i++) {
            lengthOfLISEndAtI[i] = 1;
            subList[i] = String.valueOf(nums[i]);
            // 当前扫描到的元素是nums[i]
            for (int j = 0; j < i; j++) {
                // 找出那些在nums[i]左边且比nums[i]小的元素
                if (nums[j] >= nums[i]) {
                    continue;
                }
                // 以nums[j]结尾的LIS与nums[i]组合，是否能产生更长的LIS（以nums[i]结尾）
                if (lengthOfLISEndAtI[i] < lengthOfLISEndAtI[j] + 1) {
                    lengthOfLISEndAtI[i] = lengthOfLISEndAtI[j] + 1;
                    subList[i] = subList[j] + nums[i];
                }
            }
            // 以哪个元素结尾的LIS最长
            if (max_length < lengthOfLISEndAtI[i]) {
                max_length = lengthOfLISEndAtI[i];
                max_sublist = subList[i];
            }
        }
        return max_sublist;
    }

    public static void main(String[] args) {
//        int[] nums = new int[]{2, 8, 3, 4, 1};
        int[] nums = new int[]{5, 2, 8, 3, 4, 1};
        LisSolution solution = new LisSolution();
        System.out.println(Arrays.toString(nums) + ":" + solution.lis(nums));
//        for (int i = 1; i < nums.length; i++) {
//            int[] subNums = Arrays.copyOfRange(nums, 0, i);
//            System.out.println(Arrays.toString(subNums) + ":" + solution.lis(subNums));
//        }

    }
}
