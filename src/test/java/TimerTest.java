
import operator.BatchSourceFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public class TimerTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1)
            .addSource(new BatchSourceFunction()).print();
//            .map(
//                item-> {
//                    System.out.println(item);
//                    return item;
//                }
//            )
//            .addSink(DmSink.sink());

        env.execute();
    }
}
