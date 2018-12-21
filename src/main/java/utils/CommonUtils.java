package utils;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.regex.Pattern;

public class CommonUtils {

    /**
     * 判断是否是自然数
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        if(str == null || str.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 将 Exception 转化为 String
     */
    public static String getExceptionToString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static String getExceptionToString2(Throwable e) {
        return ExceptionUtils.getStackTrace(e);
    }

    /**
     * 求两个list的并集，放入list1
     * @param list1
     * @param list2
     */
    public static void unionList(List list1, List list2) {
        if (list1 == null || list2 == null) {
            return;
        }
        list1.removeAll(list2);
        list1.addAll(list2);
    }
}
