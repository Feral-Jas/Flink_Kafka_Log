package job;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

/**
 * @author liuchenyu
 * @date 2020/11/30
 */
public class FlinkSql2 {
    public static void main(String[] args) {
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        TableEnvironment tableEnv = TableEnvironment.create(environmentSettings);
        tableEnv.executeSql("CREATE TABLE datagen (" +
            " f_sequence INT," +
            " f_random INT," +
            " f_random_str STRING" +
            ") WITH (" +
            " 'connector' = 'datagen'," +
            " 'rows-per-second'='5',"+
            " 'fields.f_sequence.kind'='sequence'," +
            " 'fields.f_sequence.start'='1'," +
            " 'fields.f_sequence.end'='1000'," +
            " 'fields.f_random.min'='1'," +
            " 'fields.f_random.max'='1000',"+
            " 'fields.f_random_str.length'='10'" +
            ")");
        tableEnv.executeSql("CREATE TABLE datagen_kafka (" +
            " f_sequence INT," +
            " f_random INT," +
            " f_random_str STRING" +
            ")with('connector' = 'kafka'," +
            " 'topic' = 'test_prod1'," +
                " 'properties.bootstrap.servers' = 'localhost:9092'," +
                " 'properties.group.id' = 'testGroup'," +
                " 'format' = 'json'," +
                " 'scan.startup.mode' = 'group-offsets')");
        tableEnv.executeSql("INSERT INTO datagen_es select * from datagen");
    }
}
