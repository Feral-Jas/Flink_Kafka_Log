import connector.DmSink;
import operator.DmSourceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author liuchenyu
 * @date 2020/11/24
 */
public class LearnJob {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<String> sql = env.addSource(new DmSourceFunction("sql", 3));

        sql.map(
            item-> {
                System.out.println(item);
                return item;
            }
        ).returns(Types.STRING);
        //.addSink(DmSink.sink());

        //从ES里查
        //定时任务  查ES

    }
}
