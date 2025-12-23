package algo.dp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Set negSet = new HashSet(4050549);
        File posFile = new File("/tmp/pos_itemtypeid_225039_sort");
//        File negFile = new File("/tmp/neg_itmetypeid_4050549_sort");
        File negFile = new File("/tmp/neg_itemtypeid_base_2493597_sort");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(negFile));
            String tempString = null;
            int count = 0;
            int differentCount = 0;
            while ((tempString = reader.readLine()) != null) {
                boolean ifExist = negSet.add(tempString);
                count++;
                if(ifExist) {
                    differentCount++;
                } else {
//                    System.out.println(tempString);
                }
            }
            System.out.println("neg set size: " + negSet.size());
            System.out.println("neg different Count: " + differentCount);
            System.out.println("neg total count: " + count);

            count = 0;
            int containCount = 0;
            reader = new BufferedReader(new FileReader(posFile));
            while ((tempString = reader.readLine()) != null) {
                count++;
                if(negSet.contains(tempString)) {
                    containCount++;
                } else {
//                    System.out.println(tempString);
                }
            }
            System.out.println("pos total size: " + count);
            System.out.println("contain count: " + containCount);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
