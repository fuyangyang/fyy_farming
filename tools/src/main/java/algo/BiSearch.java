package algo;

public class BiSearch {

    public static void main(String[] args) {
        int[] arr = new int[]{1,2,3,4,5,6,7,8,9};
        System.out.println(biSearch(arr, 1));
        System.out.println(biSearch(arr, 2));
        System.out.println(biSearch(arr, 3));
        System.out.println(biSearch(arr, 9));
        System.out.println(biSearch(arr, 10));
        System.out.println(biSearch(arr, 0));
    }

    public static int biSearch(int[] arr, int target) {

        if (arr == null) {
            throw new IllegalArgumentException("input is null");
        }

        int left = 0;
        int right = arr.length - 1;

        while(left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }
}
