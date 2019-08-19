package cn.caigd.learn.flink;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StreamJob {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = env.readTextFile("D:\\ad.jiguang.cn_2019-07-03-00_part-00000");
        try {
            env.execute("hello world");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
