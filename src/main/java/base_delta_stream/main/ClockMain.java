package base_delta_stream.main;

import base_delta_stream.*;

public class ClockMain {

    public void start() throws Exception {
        KafkaMessageSource source = new KafkaMessageSource();
        Clock<Message> clock = new MessageDrivenClock("/Users/fuyangyang/github/fyy_farming/click_nn/");
        while(source.hasNext()) {
            clock.onElement(source.get());
        }
    }

    public static void main(String[] args) throws Exception {
        ClockMain main = new ClockMain();
        main.start();
    }
}
