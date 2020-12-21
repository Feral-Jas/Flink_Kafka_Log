
import connector.KafkaConnector;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public class JsonJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStream<Tuple1> forward = env.addSource(KafkaConnector.JsonNodeConsumer("demo1"))
            .map((MapFunction<ObjectNode, Tuple1>)
                item -> {
                    String name = item.get("name").asText();
                    Tuple1<String> stringTuple1 = new Tuple1<>(name);
                    return stringTuple1;
                })
            .returns(Types.TUPLE(Types.STRING))
            .forward();
//        forward
//
//             .addSink(new DmSinkUniversal("INSERT INTO CSSBASE_FLINK.SQL_TEST VALUES(?)"));
        env.execute("json deserialze");
    }
}
