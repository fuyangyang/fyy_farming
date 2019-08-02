package innerclass.notstatic;

public class Tester {
    public static void main(String[] args) {
        testTwoObject();
    }

    private static void testNormalFunc() {
        Goods p = new Goods();
        Contents contents = p.cont();
        System.out.println(contents.value());
        Destination destination = p.dest("Beijing");
        System.out.println(destination.readLabel());
    }

    private static void testDescriper() {
        // 因为Content类修饰符为private故外部不可见，而GDestination是包内可见的，故这里可以创建实例
        Goods.GDestination good = new Goods().new GDestination("TianJing");
        System.out.println(good.readLabel());
    }

    //两个非静态内部类对象的地址不一样
    private static void testTwoObject() {
        Goods p1 = new Goods();
        Contents contents1 = p1.cont();
        Destination destination1 = p1.dest("Beijing");

        Goods p2 = new Goods();
        Contents contents2 = p2.cont();
        Destination destination2 = p2.dest("Beijing");

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(contents1);
        System.out.println(contents2);
    }
}
