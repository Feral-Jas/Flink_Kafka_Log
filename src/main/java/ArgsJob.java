import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import job.MultiAggBySec;
import job.ParseLog;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

/**
 * @author liuchenyu
 * @date 2020/11/19
 */
public class ArgsJob {
    public static void main(String[] args) throws Exception {
        Gson gson  = new GsonBuilder().create();
        Map<String,Object> map = gson.fromJson(args[0], Map.class);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String jobName = "";
        for(Map.Entry<String,Object>  x:map.entrySet()){
            switch (x.getKey()){
                case "Parse":
                    System.out.println("Parse");
                    jobName = "Parse";
                    ParseLog.exec(env);
                    break;
                case "Compute":
                    System.out.println("Compute");
                    jobName = "Compute";
                    MultiAggBySec.exec(env,1L);
                    break;
                default:
                    break;
            }
        }
        env.execute(jobName);

    }
}
