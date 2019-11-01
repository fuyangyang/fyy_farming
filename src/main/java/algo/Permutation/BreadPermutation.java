package algo.Permutation;

import java.util.ArrayList;

/**
 * 一个人一次只能吃1或2或3个面包，给n个面包，输出所有吃法
 */
public class BreadPermutation {


    public static void main(String[] args) {
//        getAllPermutation(4, "");
        f(4, new ArrayList());
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



        public static void f(int n, ArrayList<Integer> arrayList) {
            if(n == 0) {
                //times ++;
                for(Integer i : arrayList) {
                    System.out.println(arrayList);
                }
                return ;
            } else if(n < 0) {
                return ;
            }

            //dfs
            arrayList.add(1);
            f(n - 1, arrayList);
            arrayList.remove(arrayList.size() - 1);

            arrayList.add(2);
            f(n - 2, arrayList);
            arrayList.remove(arrayList.size() - 1);

            arrayList.add(3);
            f(n - 3, arrayList);
            arrayList.remove(arrayList.size() - 1);

            //
            //arrayList.remove(arrayList.size() - 1);
    }
}
