package thrift;


import org.apache.thrift.TException;

public class HelloWorldImpl implements HelloWorldService.Iface {

    @Override
    public String sayHello(String username) throws TException {
        return "hello, nice to see you";
    }
}
