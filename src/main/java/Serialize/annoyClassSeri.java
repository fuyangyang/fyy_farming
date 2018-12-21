package Serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 证明匿名内部类不可以序列化,但是没能证明
 * Created by fuyi on 18/1/22.
 */
public class annoyClassSeri {

    public static void main(String[] args) {
        byte[] s = seri(getOneBird());
        System.out.println("s:" + s);
        Object obj = unserialize(s);
        System.out.println("obj:" + obj);

        Bird bird = (Bird)obj;
        bird.fly();
    }

    public static Bird getOneBird() {
        return new Bird() {
            public void fly() {
                System.out.println("flying north");
            }
        };
    }

    public static byte[] seri(Bird bird) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(bird);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}
