import connector.DmSink;
import job.DmCompute;
import operator.BatchSourceFunction;
import operator.CountProcessor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.sql.Timestamp;

/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public class DmJob {
    public static void main(String[] args) throws Exception {
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
            env.setParallelism(1)
                .addSource(new BatchSourceFunction("select * from CSSBASE_CL.S_LOG t where t.TIMESTAMP >=? and t.TIMESTAMP <=?",3))
                .map(row->{
                    String[] splited = row.split(",");
                    return new Tuple4<>(splited[0], splited[1], splited[2], splited[3]);
                })
                .returns(Types.TUPLE(Types.STRING,Types.STRING,Types.STRING,Types.STRING))
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(5)))
                .process(new CountProcessor())
                .addSink(DmSink.sink());
                //.print();
            env.execute("达梦数据降维-5秒级");
    }
}
