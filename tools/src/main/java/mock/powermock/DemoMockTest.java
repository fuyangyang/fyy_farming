package mock.powermock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * powermock是对mocktio的升级版
 * 感觉mockito家族的jar包不太好用，还是尝试一下jmockit吧
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Demo.class, Utils.class})
@PowerMockIgnore("javax.management.*") //为了解决使用powermock后，提示classloader错误
public class DemoMockTest {

    @Spy //需要手动new
    @InjectMocks
    private Demo demo = new Demo();

    @Mock  // 非@Spy修饰的对象，可以不手动new，由mockito框架实例化
    private UserService userService;

    /**
     * 测试
     * @see Demo#dealUser()
     */
    @Test
    public void testDealUser() throws Exception {
        // correct
        PowerMockito.when(userService.getUserName(ArgumentMatchers.anyInt())).thenReturn("zero007");

        // error
        // PowerMockito.doReturn("zero007").when(userService.getUserName(ArgumentMatchers.anyInt()));

        // correct
//        PowerMockito.doReturn("zero007").when(userService).getUserName(ArgumentMatchers.anyInt());

        // when 里面参数  如果方法返回值为void
        // error
        // PowerMockito.doNothing().when(userService.handle(ArgumentMatchers.anyString()));
        // correct
        PowerMockito.doNothing().when(userService).handle(ArgumentMatchers.anyString());
        // correct
        PowerMockito.doNothing().when(userService, "handle", ArgumentMatchers.anyString());


        // mock私有方法，demo 必须是mock或者spy的实例
        // 必须先录制预期行为
        PowerMockito.doNothing().when(demo, "say", ArgumentMatchers.anyString());

        demo.dealUser();
    }

    /**
     * @see Demo#say(String)
     */
    @Test
    public void testSay() throws Exception {

        // throw org.mockito.exceptions.misusing.UnfinishedStubbingException:
        //        PowerMockito.doReturn("zero").when(demo).sys(ArgumentMatchers.anyString());

        // throw org.mockito.exceptions.misusing.UnfinishedStubbingException:
        //        PowerMockito.doReturn("zero").when(demo.sys(ArgumentMatchers.anyString()));

        // correct
//        Mockito.doReturn("zero").when(demo).sys(ArgumentMatchers.anyString());
        // error   Mockito.doReturn("zero").when(demo.sys(ArgumentMatchers.anyString()));


        Mockito.when(demo.sys(ArgumentMatchers.anyString())).thenReturn("zero");
//        ---sys() :
        System.out.println(demo.sys("007"));

        Whitebox.invokeMethod(demo, "say", "Hello World!");//测试private方法
    }

    /**
     * @see Demo#work()
     */
    @Test
    public void testWork() throws Exception {
    }

    /**
     * @see Demo#getString()
     */
    @Test
    public void tetsGetString() throws Exception {
        //mock静态方法
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.when(Utils.getObject(ArgumentMatchers.any(Demo.class), ArgumentMatchers.anyString())).thenCallRealMethod();
        PowerMockito.doNothing().when(Utils.class, "deal");
        // PowerMockito.when(Utils.xxx(xx).thenReturn(xxx));
        Whitebox.setInternalState(Utils.class, "URL_STRING", "www.123.com");
        String result = demo.getString();
        System.out.println(result);
    }
}
