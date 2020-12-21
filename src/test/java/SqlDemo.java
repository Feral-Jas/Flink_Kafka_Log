import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import connector.KafkaConnector;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.KafkaClient;
import utils.RegexHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuchenyu
 * @date 2020/12/2
 */
public class SqlDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(KafkaConnector.mapConsumer("filebeat_trade"))
            .map((MapFunction<Map, String>)
                item->{
                    Gson gson = new GsonBuilder().create();
                    Map message = RegexHelper.mapRegex(item.get("message").toString());
                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    message.put("buy_time", sdf.format(new Date()));
                    System.out.println(gson.toJson(message));
                    return gson.toJson(message);
                })
            .addSink(KafkaConnector.producer("sql_prod1"));
        env.execute("filebeat_pipeline");
    }
}
