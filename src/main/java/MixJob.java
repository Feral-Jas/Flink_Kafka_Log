import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import connector.DmSink;
import connector.DmSink1;
import connector.ElasticSink;
import connector.KafkaConnector;

import model.EsbMonitor;
import operator.CountProcessor;
import operator.GatewayTypeProcessor;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple7;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import utils.RegexHelper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuchenyu
 * @date 2020/11/4
 * @description 处理网关日志降维
 */
public class MixJob {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        FlinkKafkaConsumer<Map> consumer = KafkaConnector.mapConsumer("gateway_filebeat");
        consumer.assignTimestampsAndWatermarks(
            WatermarkStrategy.forMonotonousTimestamps());
        SingleOutputStreamOperator<EsbMonitor> modeled = env.setParallelism(1)
            .addSource(consumer)
            .map((MapFunction<Map, EsbMonitor>)
                hit -> {
                    String message = hit.get("message").toString();
                    return RegexHelper.parseRegex(message);
                });
        modeled.map((MapFunction<EsbMonitor,String>)
            hit-> hit.gatewayCode)
            .windowAll(TumblingEventTimeWindows.of(Time.seconds(Integer.parseInt(args[0]))))
            .process(new GatewayTypeProcessor())
            .addSink(new DmSink1("insert into CSSBASE_FLINK.GATEWAYTYPE_SECOND_"+args[0]+" values(?,?,?,?,?,?,?)"));
        DataStream<Tuple7<String, LocalDateTime, Long, Long, Integer, Integer, Integer>> broadcast =
            modeled
                .map((MapFunction<EsbMonitor, Tuple3<Long, Long, String>>)
                        hit -> {
                            Tuple3<Long, Long, String> keyAttrs = new Tuple3<>();
                            keyAttrs.f0 = hit.dataSizeIn;
                            keyAttrs.f1 = hit.dataSizeOut;
                            keyAttrs.f2 = hit.status;
                            return keyAttrs;
                        }
                )
                .returns(Types.TUPLE(Types.LONG, Types.LONG, Types.STRING))
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(Integer.parseInt(args[0]))))
                .process(new CountProcessor())
                .broadcast();
        broadcast.map(Tuple7::toString).addSink(KafkaConnector.producer("gateway_sec"));
        broadcast.map(
            item->{
                Map<String,Object> res = new HashMap<>(7);
                res.put("uuid",item.f0);
                res.put("timestamp",item.f1.toString());
                res.put("data_size_in",item.f2);
                res.put("data_size_out",item.f3);
                res.put("count_success",item.f4);
                res.put("count_failure",item.f5);
                res.put("count_all",item.f6);
                Gson gson = new GsonBuilder().create();
                return gson.toJson(res);
            }
        ).addSink(ElasticSink.builder0("gateway_second_"+args[0]));
        broadcast.addSink(new DmSink("insert into CSSBASE_FLINK.GATEWAY_SECOND_"+args[0]+" values(?,?,?,?,?,?,?)"));
        //System.out.println(env.getExecutionPlan());
         env.execute();
    }
}
