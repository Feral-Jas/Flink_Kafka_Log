
import job.ParseLog;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


/**
 * @author liuchenyu
 * @date 2020/11/4
 */
public class ParseJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ParseLog.exec(env);
        env.execute("Parse");
    }
}
