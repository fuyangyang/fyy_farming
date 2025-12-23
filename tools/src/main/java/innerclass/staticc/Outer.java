package innerclass.staticc;

public class Outer {

    private static String out_static_mem = "out_static_mem";
    private String out_mem = "out_not_static_mem";

    private static class Inner {
        private static String inner_static_mem = "inner_static_mem";
        private  String inner_mem = "inner_static_mem";

        public Inner(String inner_mem) {
            this.inner_mem = inner_mem;
        }

        String what() {
            String x = inner_static_mem;
            //not allowed to access OUT_NON_STATIC_MEM;
//            x = out_mem;
            x = out_static_mem;
            x = inner_mem;
            return "what" + out_static_mem;
        }
    }

    public static void main(String[] args) {
        //创建多个静态内部类对象，对象不是同一个。
        Inner inner1 = new Inner("inner1");
        Inner inner2 = new Inner("inner2");
        System.out.println(inner1);
        System.out.println(inner2);
    }
}
