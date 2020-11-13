package job;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import connector.ElasticSink;
import connector.KafkaConnector;
import model.EsbMonitor;
import model.GatewayLog;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import operator.SumProcessor;
import operator.LogTimestampAssigner;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuchenyu
 * @date 2020/11/4
 */
public class MultiAggBySec {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static void exec(StreamExecutionEnvironment env,Long... intervals){
        FlinkKafkaConsumer<String> parsedConsumer = KafkaConnector.consumer("parsed");

        DataStreamSource<String> parsedStream = env.addSource(parsedConsumer);

        parsedStream.name("kafka_log_parsed");

        DataStream<Tuple2<Long, Long>> tuple2DataStream = parsedStream
            //.assignTimestampsAndWatermarks(new LogTimestampAssigner())
            .map((MapFunction<String, Tuple2<Long, Long>>)
                parsed -> {
                    EsbMonitor esbMonitor = gson.fromJson(parsed, GatewayLog.class).esbMonitor;
                    return new Tuple2<>(esbMonitor.dataSizeIn, esbMonitor.dataSizeOut);
                }).name("Map:String to Tuple2")
            .returns(Types.TUPLE(Types.LONG, Types.LONG)).forward();

        for (Long interval:intervals){
            tuple2DataStream
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(interval)))
                .process(new SumProcessor()).name("Processor:sum by "+interval+"sec")
                .map(
                    tuple2->{
                        Map chartMap = new HashMap();
                        chartMap.put("data_size_in",tuple2.f0);
                        chartMap.put("data_size_out",tuple2.f1);
                        chartMap.put("@timestamp",LocalDateTime.now().minusHours(8).toString());
                        return gson.toJson(chartMap);
                    }).name("Map:Tuple2 to String")
                .returns(Types.STRING)
                .addSink(ElasticSink.builder0("flink_tupled_"+interval+"sec")).name("es_sum_"+interval+"sec");
        }
    }
}
