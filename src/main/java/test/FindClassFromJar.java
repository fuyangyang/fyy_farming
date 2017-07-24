package test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by fuyi on 17/7/6.
 */
public class FindClassFromJar {
    public static void main(String[] args) {
        JarFile jfile = null;
        try {
            jfile = new JarFile("/Users/fyy/IdeaProjects/my_udf_test/target/my_udf_test-jar-with-dependencies.jar");
        } catch (IOException e) {

        }

        Enumeration files = jfile.entries();
        while(files.hasMoreElements()) {
            JarEntry entry = (JarEntry)files.nextElement();
            if(entry.getName().startsWith("com/chris/utf/LowerUDF.class")) {
                System.out.println(entry.getName());
            }
        }
    }
}
