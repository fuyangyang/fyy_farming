package mock.mockito;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

/**
 * https://liuzhijun.iteye.com/blog/1512780/
 * https://segmentfault.com/a/1190000006746409?utm_source=tag-newest
 */
public class SimpleTest {

    /**
     * mock出来对象mockedObj不是真实对象
     * 针对mockedObj的方法可以指定返回值，抛异常等
     * ref https://liuzhijun.iteye.com/blog/1512780/
     */
    @Test
    public void testMock() {

        //创建mock对象，参数可以是类，也可以是接口
        List<String> list = mock(List.class);

        //设置方法的预期返回值
        when(list.get(0)).thenReturn("helloworld");

        String result = list.get(0);

        //验证方法调用(是否调用了get(0))
        verify(list).get(0);

        //junit测试
        Assert.assertEquals("helloworld", result);

        when(list.get(1)).thenThrow(new RuntimeException("test excpetion"));

        list.get(1);
    }

    /**
     * spy出来的是真实对象，需要先new一个出来
     * ref https://segmentfault.com/a/1190000006746409?utm_source=tag-newest
     */
    @Test
    public void testSpy() {
        List list = new LinkedList();
        List spy = spy(list);

        // 对 spy.size() 进行定制.
        when(spy.size()).thenReturn(100);

        spy.add("one");
        spy.add("two");

        // 因为我们没有对 get(0), get(1) 方法进行定制,
        // 因此这些调用其实是调用的真实对象的方法.
        Assert.assertEquals(spy.get(0), "one");
        Assert.assertEquals(spy.get(1), "two");

        Assert.assertEquals(spy.size(), 100);
    }
}
