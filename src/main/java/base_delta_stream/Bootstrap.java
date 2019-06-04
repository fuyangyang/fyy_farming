package base_delta_stream;

import base_delta_stream.core.Clock;
import base_delta_stream.core.Message;
import base_delta_stream.core.MessageDrivenClock;
import base_delta_stream.source.KafkaMessageSource;

public class Bootstrap {

    public void start() throws Exception {
        KafkaMessageSource source = new KafkaMessageSource();
        Clock<Message> clock = new MessageDrivenClock("/Users/fuyangyang/github/fyy_farming/click_nn/");
        while(source.hasNext()) {
            clock.onElement(source.get());
        }
    }

    public static void main(String[] args) throws Exception {
        Bootstrap main = new Bootstrap();
        main.start();
    }
}
