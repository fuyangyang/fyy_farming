package algo.sampling;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 写码题：实现delete(value), insert(value),getRandom()方法，时间复杂度O(1)
 * 答：给出了使用数组的近似均匀采样方法，数据结构使用HashMap的底层数组上挂链表的
 * 方式。最后在面试官的提示下使用一个数组和map来解决，map key放value，value放
 * 数组的下标，数组用来放value。insert时先在数组尾指针放value，然后把(value, index）
 * 放到map里。delete时根据map查到下标，然后map中删除kv对，数组根据index删除数据，
 * 通过把数组最后一个数据搬到index处覆盖的方式。尾指针前移一位。
 */
public class UniformSampling {

    private String[] arr;
    private int tailIndex;
    private Map<String, Integer> map;
    private Random r;


    public UniformSampling(int capacity) {
        arr = new String[capacity];
        tailIndex = 0;
        map = new HashMap<>();
        r = new Random();
    }

    public String getRandom() {
        return arr[r.nextInt(tailIndex)];
    }

    public void insert(String value) {
        arr[tailIndex] = value;
        map.put(value, tailIndex++);
    }

    public void delete(String value) {
        Integer deleteIndex = map.remove(value);
        if (deleteIndex != null) {
            arr[deleteIndex] = arr[tailIndex--];
        }
    }

    public static void main(String[] args) {
        UniformSampling sampling = new UniformSampling(10);

        for (int i = 0; i < 11; i++) {
            sampling.insert(String.valueOf("value" + i));
            System.out.println(i + ": " + sampling.getRandom());
        }
    }
}
