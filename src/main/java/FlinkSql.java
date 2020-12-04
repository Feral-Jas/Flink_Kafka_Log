
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

/**
 * @author liuchenyu
 * @date 2020/11/27
 */
public class FlinkSql {
    public static void main(String[] args) {
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        TableEnvironment tableEnv = TableEnvironment.create(environmentSettings);
        tableEnv.executeSql("create table gateway_sec(name STRING,age INT,proctime AS PROCTIME())" +
            "with('connector' = 'kafka'," +
            " 'topic' = 'test_prod1'," +
            " 'properties.bootstrap.servers' = 'localhost:9092'," +
            " 'properties.group.id' = 'testGroup'," +
            " 'format' = 'json'," +
            " 'scan.startup.mode' = 'group-offsets')"
        );
        tableEnv.executeSql("create table es_gateway_sec(name STRING,age INT)" +
            "with(" +
                "'connector' = 'kafka'," +
            " 'topic' = 'test_prod2'," +
                " 'properties.bootstrap.servers' = 'localhost:9092'," +
                " 'properties.group.id' = 'testGroup'," +
                " 'format' = 'json'," +
                " 'scan.startup.mode' = 'group-offsets')");
        tableEnv.executeSql("insert into es_gateway_sec select name,age from gateway_sec");
    }
}
