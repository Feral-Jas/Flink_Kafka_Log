
import job.ParseLog;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


/**
 * @author liuchenyu
 * @date 2020/11/4
 */
public class ParseJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        ParseLog.exec(env);
        env.execute("Parse");
    }
}
