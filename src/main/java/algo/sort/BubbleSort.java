package algo.sort;

import java.util.Arrays;

public class BubbleSort {

    public static void main(String[] args) {
        int[] arr = null;
//        sort(arr);
//        System.out.println(arr);

        arr = new int[0];
        sort(arr);
        System.out.println(Arrays.toString(arr));

        arr = new int[]{8,2,1,5,3,4};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void sort(int[] arr) {

        if (arr == null) {
            throw new IllegalArgumentException("arr is null");
        }

        int size = arr.length;

        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}
