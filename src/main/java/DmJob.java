import job.DmCompute;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public class DmJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DmCompute.exec(env);
        env.execute();
    }
}
