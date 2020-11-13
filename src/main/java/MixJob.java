import job.AggBySec;
import job.MultiAggBySec;
import job.ParseLog;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author liuchenyu
 * @date 2020/11/4
 */
public class MixJob {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //ParseLog.exec(env);
        long parseLong = Long.parseLong(args[0]);
        MultiAggBySec.exec(env,parseLong);

        //System.out.println(env.getExecutionPlan());
        env.execute("MixJob");

    }
}
