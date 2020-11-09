package job;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import connector.ElasticSink;
import connector.KafkaConnector;
import model.GatewayLog;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import utils.RegexHelper;

/**
 * @author liuchenyu
 * @date 2020/11/4
 */
public class ParseLog {
    public static void exec(StreamExecutionEnvironment env){
        FlinkKafkaConsumer<String> logComsumer = KafkaConnector.consumer("log");
        DataStreamSource<String> logSourceStream = env.addSource(logComsumer);
        logSourceStream.name("kafka_log_origin");

        DataStream<String> parsedStream = logSourceStream
            .map((MapFunction<String, String>)
                source -> {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    GatewayLog gatewayLog = gson.fromJson(source, GatewayLog.class);
                    gatewayLog.esbMonitor = RegexHelper.parseRegex(gatewayLog.message);
                    System.out.println("入参:"+gatewayLog.esbMonitor.dataSizeIn+" , 出参："+gatewayLog.esbMonitor.dataSizeOut+", 状态："+gatewayLog.esbMonitor.status);
                    return gson.toJson(gatewayLog);
                }).name("Serialize log")
            .forward();

        parsedStream.addSink(ElasticSink.builder0("flink_parsed")).name("es_parsed");
        parsedStream.addSink(KafkaConnector.producer("parsed")).name("kafka_log_parsed");
    }
}
