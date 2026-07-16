package com.chris.dnn.forward;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * 1. 乱序数据处理：
 * 早到的数据会放在后面正确的窗口里
 * 晚到的数据如果超过lateness，则会放弃或到侧流等
 *
 * 2. watermark在flink任务中是如何传递的
 * Watermark 传递遵循「局部性(每个并行子任务都维护自己的wm)、单向性（从上游算子到下游算子）、最小水印原则（下游算子的wm是上游所有算子的最小水印）」，每个并行子任务独立维护 Watermark 状态；
 * 完整传递流程：Source/Assign 算子生成 → 转换算子透传 → 合流算子取最小值推进 → 窗口算子消费触发窗口 → Sink 算子终止；
 * 核心规则：合流场景下下游取上游 Watermark 最小值，空闲流需配置 withIdleness 避免水印阻塞；
 * 传递的本质：将「时间进度」从上到下同步，确保下游算子按事件时间准确触发窗口计算。
 *
 *
 */
public class FlinkWatermarkDemo {

    /**
     * 事件时间减去的秒数作为watermark，是一种watermark策略
     * Watermark = maxEventTimestamp - maxOutOfOrderness - 1ms
     */
    private static final int BOUNDED_OUT_OF_ORDERNESS = 4;

    /**
     * 窗口大小，当watermark越过窗口右侧时间时，则触发窗口计算
     */
    private static final int WINDOW_SECONDS = 10;

    /**
     * 允许迟到的秒数，当来的事件时间-watermark小于等于这个秒数时，窗口还会等到这个事件，即窗口触发晚的秒数
     */
    private static final int ALLOWED_LATENESS_SECONDS = 0;

    /**
     * 当作业并行度 > 1 时，若某一个上游并行子任务长时间无数据流入（空闲流），其 Watermark 会停留在初始值，导致下游算子的 Watermark 无法推进（下游取所有上游 Watermark 的最小值）。
     * 通过 withIdleness 配置空闲超时时间，当某流超过该时间无数据，会被标记为空闲流，下游计算 Watermark 时会忽略该流：
     */
    private static final int IDLENESS_SECONDS = 1;


    public static void main(String[] args) throws Exception {
        System.out.println("version: 4");
        // 1. 获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 默认就是EventTime
        System.out.println(env.getStreamTimeCharacteristic());
        // 本地运行设置并行度为1，方便查看输出顺序
        env.setParallelism(1);
        /**
         * 事件到达时，Flink 只会做两件事：
         * 提取事件时间戳，更新当前算子子任务维护的「最大事件时间（maxEventTimestamp）」（只增不减）；
         * 将数据向下游转发（或进入窗口状态）；
         * 并不会立即生成新的 Watermark。Watermark 的生成是「周期性触发」的，默认 200ms 一次，这个周期就是 AutoWatermarkInterval。
         * 简单说：「事件到达是更新 Watermark 的「素材」（maxEventTimestamp），而周期性调度是触发 Watermark 生成的「开关」」。
         *
         *
         * 1. 每条数据生成 Watermark 会带来巨大的网络与状态开销
         * Flink 是分布式系统，Watermark 的传递需要跨算子、跨节点通信：
         * 若每条数据到达都生成 Watermark，意味着每处理一条数据，就会向下游算子发送一次 Watermark 消息；
         * 对于高吞吐场景（如每秒百万级数据），会产生海量的 Watermark 通信消息，占用大量网络带宽和节点 CPU 资源，严重降低作业吞吐量；
         * 同时，下游算子接收 Watermark 后，需要更新自身状态、触发窗口检查等操作，过于频繁的 Watermark 会让下游算子陷入「处理 Watermark」的循环，无法专注于业务计算。
         * 2. Watermark 的核心作用是「触发窗口计算」，无需实时更新
         * Watermark 的核心使命是「告诉下游算子：「某个时间点之前的数据已经全部到达，可以触发对应窗口的计算了」」。
         * 窗口的最小粒度通常是秒级 / 分钟级（如 10 秒窗口、1 分钟窗口），即使 Watermark 延迟 200ms 生成，也不会影响窗口计算的最终准确性；
         * 对于 10 秒窗口来说，200ms 的 Watermark 生成延迟，完全可以忽略不计，不会影响业务对数据时效性的要求。
         * 3. 周期性生成是「批量处理」的优化思路
         * Flink 将 Watermark 生成设计为周期性触发，本质是一种「批量处理」优化：
         * 在 200ms 周期内，可能会处理成百上千条数据，这些数据只会更新一次 maxEventTimestamp；
         * 周期到达时，只需生成一次 Watermark 并向下游传递，大幅减少通信和状态更新的次数，在「Watermark 时效性」和「作业性能」之间取得最优平衡。
         */
        env.getConfig().setAutoWatermarkInterval(200);

        // 2. 读取模拟数据源（无需外部组件，实时生成测试数据）
        DataStream<ClickLog> clickStream = env
//                .fromData(CLICK_EVENTS) // 不能这样用，因为静态数据一次性加载完成后，Watermark 生成器还未到周期性触发时间，数据已全部处理完毕，导致 Watermark 始终停留在初始值（Long.MAX_VALUE/Long.MIN_VALUE）。
                .addSource(new CustomClickSource()) // Flink 的 Watermark 生成依赖「数据流的持续输入」和「时间周期触发」（默认 200ms）
                .name("click_events");

//        clickStream.print("input clickLog: ");

        WatermarkStrategy<ClickLog> watermarkStrategy = WatermarkStrategy.<ClickLog>forBoundedOutOfOrderness(Duration.ofSeconds(BOUNDED_OUT_OF_ORDERNESS))
                .withTimestampAssigner(
                        new SerializableTimestampAssigner<ClickLog>() {
                            @Override
                            public long extractTimestamp(ClickLog element, long recordTimestamp) {
                                System.out.println("assign-watermark: " + element.getTimestamp() + ",  " + formatTimestamp(element.getTimestamp()));
                                return element.getTimestamp();
                            }
                        }
                ).withIdleness(Duration.ofSeconds(IDLENESS_SECONDS));

        SingleOutputStreamOperator<ClickLog> clickLogSingleOutputStreamOperator = clickStream
                .assignTimestampsAndWatermarks(watermarkStrategy)
                .name("assign-watermark");

        // 定义侧输出标签用于收集迟到数据
        OutputTag<ClickLog> lateDataTag = new OutputTag<ClickLog>("late-data") {};

        SingleOutputStreamOperator<Long> windowResult = clickLogSingleOutputStreamOperator
                .keyBy(new KeySelector<ClickLog, String>() {
                    @Override
                    public String getKey(ClickLog value) throws Exception {
                        // keyby到一个window
                        return "unique";

                        // keyby到多个window
//                        return value.getUser();
                    }
                })
                .window(TumblingEventTimeWindows.of(Time.seconds(WINDOW_SECONDS)))
                .allowedLateness(Time.seconds(ALLOWED_LATENESS_SECONDS))
                .sideOutputLateData(lateDataTag)
                .process(new ProcessWindowFunction<ClickLog, Long, String, TimeWindow>() {
                    @Override
                    public void process(String s, ProcessWindowFunction<ClickLog, Long, String, TimeWindow>.Context context, Iterable<ClickLog> elements, Collector<Long> out) throws Exception {
                        System.out.println("窗口区间: " + formatTimestamp(context.window().getStart()) + " -----------> " + formatTimestamp(context.window().getEnd()));
                        Iterator<ClickLog> iterator = elements.iterator();
                        while (iterator.hasNext()) {
                            ClickLog clickLog = iterator.next();
                            System.out.println("window receive: " + clickLog + ", currentWatermark: " + context.currentWatermark() + ", now: " + formatTimestamp(System.currentTimeMillis()));
                            out.collect(clickLog.timestamp);
                        }
                    }
                });

        windowResult.getSideOutput(lateDataTag).process(new ProcessFunction<ClickLog, Object>() {
            @Override
            public void processElement(ClickLog clickLog, ProcessFunction<ClickLog, Object>.Context ctx, Collector<Object> out) throws Exception {
                out.collect(clickLog);
            }
        }).print("迟到事件：");

        env.execute("FlinkWatermarkDemo execute");
    }

    @Data
    @AllArgsConstructor
    static class ClickLog {
        public String user;
        public String url;
        public Long timestamp;
    }

    public static ClickLog[] CLICK_EVENTS = new ClickLog[]{
            new ClickLog("user_1", "url_1",   1750867204000L),
            new ClickLog("user_2", "url_2",   1750867202000L),
            new ClickLog("user_3", "url_3",   1750867207000L),
            new ClickLog("user_4", "url_4",   1750867210000L),
            new ClickLog("user_5", "url_5",   1750867209000L),
            new ClickLog("user_6", "url_6",   1750867215003L),
            new ClickLog("user_7", "url_7",   1750867212000L),
            new ClickLog("user_8", "url_8",   1750867213000L),
            new ClickLog("user_9", "url_9",   1750867225000L),
            new ClickLog("user_10", "url_10", 1750867205000L),
    };
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatTimestamp(Long ts) {
        return sdf.format(ts);
    }

    /**
     * 自定义流式数据源：模拟数据持续输出，推动Watermark推进
     */
    static class CustomClickSource implements SourceFunction<ClickLog> {
        private volatile boolean isRunning = true;

        @Override
        public void run(SourceContext<ClickLog> ctx) throws Exception {
            // 逐个输出模拟数据，间隔500ms，模拟流式输入
            for (ClickLog clickLog : CLICK_EVENTS) {
                if (!isRunning) {
                    break;
                }
                ctx.collect(clickLog);
                TimeUnit.MILLISECONDS.sleep(500); // 间隔输出，让Watermark有时间生成
            }
            // 输出完数据后，等待一段时间，让Watermark推进到窗口结束时间
            TimeUnit.SECONDS.sleep(BOUNDED_OUT_OF_ORDERNESS + 1);
            isRunning = false;
        }

        @Override
        public void cancel() {
            isRunning = false;
        }
    }

}
