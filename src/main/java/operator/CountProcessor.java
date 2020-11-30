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
 * @date 2020/11/17
 * Tuple3:data_size_in,data_size_out,status
 * Tuple7:uuid,timestamp,sum(data_size_in),sum(data_size_out),count(status_success),count(status_failure),count(all)
 */
public class CountProcessor extends ProcessAllWindowFunction<
    Tuple3<Long,Long,String>,
    Tuple7<String,LocalDateTime,Long,Long,Integer,Integer,Integer>,
    TimeWindow> {

    @Override
    public void process(Context context, Iterable<Tuple3<Long, Long, String>> iterable, Collector<Tuple7<String, LocalDateTime, Long, Long, Integer, Integer, Integer>> collector) {
        Long dataSizeIn = 0L;
        Long dataSizeOut = 0L;
        int countStatusSuccess = 0;
        int countStatusFailure = 0;
        int countAll = 0;
        for (Tuple3<Long, Long, String> tuple3 : iterable) {
//            System.out.println(tuple3.toString());
            dataSizeIn += tuple3.f0;
            dataSizeOut += tuple3.f1;
            countAll++;
            if("1".equals(tuple3.f2)){
                countStatusSuccess++;
            }else{
                countStatusFailure++;
            }
        }
        Tuple7<String,LocalDateTime,Long,Long,Integer,Integer,Integer> countSec = new Tuple7<>(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            dataSizeIn, dataSizeOut, countStatusSuccess, countStatusFailure, countAll
        );
        System.out.println(countSec.toString());
        collector.collect(countSec);
    }
}
