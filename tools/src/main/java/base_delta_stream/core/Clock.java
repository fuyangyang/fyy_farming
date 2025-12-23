package base_delta_stream.core;


public interface Clock<T> {

    void onElement(T t) throws Exception;
}
