package algo.Permutation;

public class NptTest {
    public static void main(String[] args) {
        int i = 32;
        try {
            if (i > 2) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }
    }
}
