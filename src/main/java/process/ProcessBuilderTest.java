package process;

import com.google.common.base.Joiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by fuyi on 17/5/3.
 */
public class ProcessBuilderTest {

    public static void main(String[] args) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("/usr/bin/python","print.py","hello", "python");
//        ProcessBuilder pb = new ProcessBuilder("ls","-ls");
        pb.directory(new File("/Users/fyy/"));
        printEnv(pb);
        Process p = pb.start();
        int exit = p.waitFor();
        List<String> command = pb.command();
        System.out.println(Joiner.on(" ").join(command));

        // 取得命令结果的输出流
        InputStream fis = p.getInputStream();
        // 用一个读输出流类去读
        InputStreamReader isr = new InputStreamReader(fis);
        // 用缓冲器读行
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        // 直到读完为止
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }




        System.out.println(exit);
    }

    private static void printEnv(ProcessBuilder pb) {
        Map<String, String> env = pb.environment(); //获得进程的环境
        System.out.println(env.toString());

        Iterator it=env.keySet().iterator();
        String sysatt = null;
        while(it.hasNext())
        {
            sysatt = (String)it.next();
            System.out.println("System Attribute:"+sysatt+"="+env.get(sysatt));
        }
    }
}
