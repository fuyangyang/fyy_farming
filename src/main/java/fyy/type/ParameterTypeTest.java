package fyy.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * ParameterType用于拿到泛型上的类型
 * Created by fuyi on 17/5/12.
 */
public class ParameterTypeTest {
//    public static void main(String[] args) {
//        AbstractMap<String, Integer> map = new HashMap<String, Integer>();
//        Class<? extends Map> mapClass = map.getClass();
//        Type type = mapClass.getGenericSuperclass();
//        ParameterizedType pt = (ParameterizedType) type;
//        System.out.println(pt.getActualTypeArguments()[0]);
//
//    }

    /**
     * 输出String
     * @param args
     */
    public static void main(String[] args) {
        MyMapper mapper = new MyMapper();
        Class<?> mapClass = mapper.getClass();
        Type type = mapClass.getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) type;
        System.out.println(pt.getActualTypeArguments()[0]);
    }

    private static class Mapper<KEYOUT, VALUEOUT> {

    }

    private static class MyMapper<String, Integer> extends Mapper<String, Integer> {

    }
}
