import job.DmCompute;
import operator.BatchSourceFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public class DmJob {
    public static void main(String[] args) throws Exception {
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            env.setParallelism(1)
                .addSource(new BatchSourceFunction()).print();
            env.execute("达梦数据降维-5秒级");
    }
}
