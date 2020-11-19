package operator;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.LocalDateTime;

/**
 * @author liuchenyu
 * @date 2020/11/17
 */
public class CountProcessor extends ProcessAllWindowFunction<Tuple4<String,String,String,String>,String, TimeWindow> {

    @Override
    public void process(Context context, Iterable<Tuple4<String, String, String, String>> iterable, Collector<String> collector) throws Exception {
        int count = 0;
        LocalDateTime timestamp;
        for (Tuple4<String,String,String,String> item:iterable){
            count++;
            timestamp = LocalDateTime.now();
        }
        //collector.collect();
    }
}
