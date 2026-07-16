package com.chris.dnn.forward;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class FlinkLocalRunTimeDemo {
    public static void main(String[] args) throws Exception {
        // 1. 获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 本地运行设置并行度为1，方便查看输出顺序
        env.setParallelism(1);

        // 2. 读取模拟数据源（无需外部组件，实时生成测试数据）
        DataStream<String> wordStream = env
                .fromData("hello", "flink", "java", "flink", "hello")
                .name("MockWordSource");

        // 3. 数据处理：转换格式 → 分组 → 窗口统计
        DataStream<Tuple2<String, Integer>> resultStream = wordStream
                // 转换为 (单词, 1) 格式
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String word) throws Exception {
                        System.out.println("receive: " + word);
                        return new Tuple2<>(word, 1);
                    }
                })
                // 按单词分组（Tuple2的第0个元素）
                .keyBy(tuple -> tuple.f0)
                // 设置5秒滚动窗口（基于处理时间）
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                // 窗口内求和
                .sum(1);

        // 4. 输出结果到控制台
        resultStream.print("WordCount Result");

        // 流处理必须手动触发执行（调用execute方法）
        env.execute("Local Stream WordCount Demo");
    }
}
