package base_delta_stream;

import java.io.IOException;

public interface Clock<T> {

    void onElement(T t) throws Exception;
}
