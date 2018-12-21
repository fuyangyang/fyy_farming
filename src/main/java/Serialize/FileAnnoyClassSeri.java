package Serialize;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * Created by fuyi on 18/3/26.
 */
public class FileAnnoyClassSeri {

    public static void main(String[] args) throws Exception {
//        writeBirdToFile();
        readBirdFromFile();
    }

    public static void readBirdFromFile() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("bird.txt"));
        Bird bird = (Bird) ois.readObject();
        System.out.println("readok");
        bird.fly();
        ois.close();
    }

    public static void writeBirdToFile() throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("bird.txt"));
        oos.writeObject(getOneBird());
        System.out.println("writeok");
        oos.close();
    }

    public static Bird getOneBird() {
        return new Bird() {
            public void fly() {
                System.out.println("flying north");
            }
        };
    }

}
