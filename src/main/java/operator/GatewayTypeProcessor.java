package operator;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple7;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author liuchenyu
 * @date 2020/12/3
 */
public class GatewayTypeProcessor extends ProcessAllWindowFunction<String, Tuple7<String, LocalDateTime,Integer,Integer,Integer,Integer,Integer>, TimeWindow> {
    @Override
    public void process(Context context, Iterable<String> iterable, Collector<Tuple7<String, LocalDateTime, Integer, Integer, Integer, Integer, Integer>> collector) throws Exception {
        int count_suser=0;
        int count_role=0;
        int count_spost = 0;
        int count_sdept=0;
        int count_cert=0;
        for (String s : iterable) {
            switch (s){
                case "suser":count_suser++;break;
                case "sdept":count_sdept++; break;
                case "spost":count_spost++;break;
                case "role":count_role++;break;
                default:break;
            }
        }
        Tuple7<String, LocalDateTime, Integer, Integer, Integer, Integer, Integer> res = new Tuple7<>(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            count_sdept, count_spost, count_role, count_suser, count_cert
        );
        collector.collect(res);
    }
}
