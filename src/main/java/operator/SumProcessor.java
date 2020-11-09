package operator;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.LocalDateTime;

/**
 * @author liuchenyu
 * @date 2020/11/4
 */
public class SumProcessor extends ProcessAllWindowFunction<Tuple2<Long, Long>, Tuple2<Long,Long>, TimeWindow> {
    private Tuple2<Long, Long> res = new Tuple2<>();
    @Override
    public void process(Context context, Iterable<Tuple2<Long, Long>> iterable, Collector<Tuple2<Long,Long>> collector) throws Exception {
        Long dataSizeInSum=0L;
        Long dataSizeOutSum=0L;
        int successCount = 0;
        int failCount=0;
        for(Tuple2<Long,Long> item:iterable){
            dataSizeInSum+=item.f0;
            dataSizeOutSum+=item.f1;
            successCount++;
        }
        System.out.println("-------------------------------------------");
        System.out.println("降维策略：按1秒聚合 计算字段：入参、出参、成功次数");
        System.out.println("统计数据：入参 "+dataSizeInSum+" , 出参 "+dataSizeOutSum+", 成功次数："+successCount+" "+ LocalDateTime.now().toString());
        System.out.println("-------------------------------------------");
        Tuple2<Long,Long> res = new Tuple2<>(dataSizeInSum,dataSizeOutSum);
        collector.collect(res);
    }
}
