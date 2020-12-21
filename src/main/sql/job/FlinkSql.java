package job;

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
        tableEnv.executeSql("create table gateway_sec(name STRING,proctime AS PROCTIME())" +
            "with('connector' = 'kafka'," +
            " 'topic' = 'dm_prod1'," +
            " 'properties.bootstrap.servers' = 'localhost:9092'," +
            " 'properties.group.id' = 'testGroup'," +
            " 'format' = 'json'," +
            " 'scan.startup.mode' = 'group-offsets')"
        );
        tableEnv.executeSql("create table dm_sql(name STRING)" +
            "with(" +
            "'connector' = 'jdbc'," +
            "'url' = 'jdbc:dm://10.10.133.220:5236/CSSBASE_FLINK'," +
            "'driver' = 'dm.jdbc.driver.DmDriver',"+
            "'table-name' = 'SQL_TEST',"+
            "'username' = 'CSSBASE',"+
            "'password' = '1234567890'"+
            ")");
        tableEnv.executeSql("insert into dm_sql select name from gateway_sec");
    }
}
