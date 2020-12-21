
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

import java.util.Arrays;

/**
 * @author liuchenyu
 * @date 2020/11/27
 */
public class FlinkSql {
    public static void main(String[] args) {
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        TableEnvironment tableEnv = TableEnvironment.create(environmentSettings);
        Arrays.stream(args).iterator().forEachRemaining(
            tableEnv::executeSql
        );
    }
}
