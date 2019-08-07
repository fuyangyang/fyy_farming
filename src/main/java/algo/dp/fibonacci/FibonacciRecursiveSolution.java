package algo.dp.fibonacci;

public class FibonacciRecursiveSolution {
    public long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be positive, but " + n);
        }

        if (n == 0 || n == 1) {
            return 1;
        }

        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static void main(String[] args) {
        FibonacciRecursiveSolution solution = new FibonacciRecursiveSolution();

        for (int i = 1; i < 100; i++) {
            System.out.println(i + ":" + solution.fibonacci(i));
        }
    }
}
