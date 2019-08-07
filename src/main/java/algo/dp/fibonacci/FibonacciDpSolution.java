package algo.dp.fibonacci;

/**
 * 利用数组或两个变量来存储子问题的解
 */
public class FibonacciDpSolution {
    public long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be positive, but " + n);
        }

        if (n == 0 || n == 1) {
            return 1;
        }

        long[] arr = new long[n];
        arr[0] = arr[1] = 1;

        for (int i = 2; i < n; i++) {
            arr[i] = arr[i - 1] + arr[i - 2];
        }

        return arr[n - 1];
    }

    public static void main(String[] args) {
        FibonacciDpSolution solution = new FibonacciDpSolution();

        for (int i = 1; i < 50; i++) {
            System.out.println(i + ":" + solution.fibonacci(i));
        }
    }
}
