import job.AggBySec;
import job.ParseLog;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author liuchenyu
 * @date 2020/11/4
 */
public class ParseTest {
    public static void main(String[] args) throws Exception {
        System.out.println(LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai")).toString());
    }
}
