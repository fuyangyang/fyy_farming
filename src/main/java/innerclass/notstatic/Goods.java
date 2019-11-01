package innerclass.notstatic;

public class Goods {

    private static String out_static_mem = "out_static_mem";
    private String out_mem = "out_mem";
    private class Content implements Contents {
        //staticc member is not allowed, but final staticc member is allowed
//        staticc String what = "no RecWordsInfo";
        static final String what = "no RecWordsInfo";

        private int i = 11;

//        staticc method not allowed in non-staticc inner class
//        public staticc String what() {
//            return what;
//        }

        @Override
        public int value() {
            //可以访问外部的静态、非静态变量
            String x = out_mem;
            x = out_static_mem;
            return i;
        }
    }

    //protected只为测试，一般情况下用private就行，没必要让类外知道
    protected class GDestination implements Destination {
        private String label;
        protected GDestination(String whereTo) {
            label = whereTo;
        }

        @Override
        public String readLabel() {
            return label;
        }
    }

    public Destination dest(String s) {
        return new GDestination(s);
    }

    public Contents cont() {
        return new Content();
    }
}
