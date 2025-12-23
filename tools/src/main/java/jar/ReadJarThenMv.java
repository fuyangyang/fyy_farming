package jar;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReadJarThenMv {
    private static int count = 1;

    public static void main(String[] args) throws Exception {
        String jarFile = "/Users/fuyangyang/github/fyy_farming/target/fyy_farming-1.0-shaded.jar";
        read(jarFile);
    }

    public static void read(String jarFile) throws Exception {
        try {
            //通过将给定路径名字符串转换为抽象路径名来创建一个新File实例
            File f = new File(jarFile);
            URL url1 = f.toURI().toURL();
            URLClassLoader myClassLoader = new URLClassLoader(new URL[]{url1}, Thread.currentThread().getContextClassLoader());
            //通过jarFile和JarEntry得到所有的类
            JarFile jar = new JarFile(jarFile);
            while (true) {
                //返回zip文件条目的枚举
                Enumeration<JarEntry> enumFiles = jar.entries();
                JarEntry entry;

                //测试此枚举是否包含更多的元素
                while (enumFiles.hasMoreElements()) {
                    entry = (JarEntry) enumFiles.nextElement();
                    if (entry.getName().indexOf("META-INF") < 0 || entry.getName().indexOf("BOOT-INF") < 0) {
                        String classFullName = entry.getName();
                        if (!classFullName.endsWith(".class")) {
                            classFullName = classFullName.substring(0, classFullName.length() - 1);
                        } else {
                            //去掉后缀.class
                            String className = classFullName.substring(0, classFullName.length() - 6).replace("/", ".");
                            if (className.contains("com")) {
                                className = className.substring(className.indexOf("com"));
                            }
                            try {
                                Class<?> myclass = myClassLoader.loadClass(className);
                                Method[] methods = myclass.getMethods();
                                //得到类中包含的属性
                                for (Method method : methods) {
                                    String methodName = method.getName();
                                    System.out.println("方法名称:" + methodName);
                                    Class<?>[] parameterTypes = method.getParameterTypes();
                                    for (Class<?> clas : parameterTypes) {
                                        // String parameterName = clas.getName();
                                        String parameterName = clas.getSimpleName();
                                        System.out.println("参数类型:" + parameterName);
                                    }
                                    System.out.println("==========================");
                                }
                            } catch (Throwable e) {

                            }
                            //打印类名
                            System.out.println("*****************************");
                            System.out.println(count++ + "全类名:" + className);
                        }
                    }
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
