package fyy.jna;

public class Hello {

    private native void sayHello();

    public static void main(String[] args) {
 //      # System.loadLibrary("Hello");
        System.load("/Users/fuyangyang/github/fyy_farming/src/main/java/libHello.so");
        Hello hello = new Hello();
        hello.sayHello();
    }
}
