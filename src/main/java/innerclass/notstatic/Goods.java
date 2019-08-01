package innerclass.notstatic;

public class Goods {
    private class Content implements Contents {
        private int i = 11;

        @Override
        public int value() {
            return i;
        }
    }

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
