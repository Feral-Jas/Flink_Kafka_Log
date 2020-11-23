
import operator.BatchSourceFunction;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public class TimerTest {
    public static void main(String[] args){

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
//        env.setParallelism(1)
//            .addSource(new BatchSourceFunction()).print();
//            .map(
//                item-> {
//                    System.out.println(item);
//                    return item;
//                }
//            )
//            .addSink(DmSink.sink());

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
