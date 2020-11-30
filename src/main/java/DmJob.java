import connector.DmSink;
import operator.DmSourceFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public class DmJob {
    public static void main(String[] args) throws Exception {
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);


            env.setParallelism(1)
                .addSource(
                    new DmSourceFunction("select * from CSSBASE_CL.S_LOG t where t.TIMESTAMP >=? and t.TIMESTAMP <=?",3)
                );
                //计算
                //.addSink(DmSink.sink());
                //.print();
            env.execute("达梦数据降维-5秒级");
    }
}
