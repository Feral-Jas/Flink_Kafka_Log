package job;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

/**
 * @author liuchenyu
 * @date 2020/12/7
 */
public class SqlJoin {
    public static void main(String[] args) {
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        TableEnvironment tableEnv = TableEnvironment.create(environmentSettings);
        tableEnv.executeSql("CREATE TABLE left1 (" +
            " lid STRING," +
            " l1 STRING" +
            ")with('connector' = 'kafka'," +
            " 'topic' = 'left1'," +
            " 'properties.bootstrap.servers' = 'localhost:9092'," +
            " 'properties.group.id' = 'tester'," +
            " 'format' = 'json'," +
            " 'scan.startup.mode' = 'group-offsets')");
        tableEnv.executeSql("CREATE TABLE right1 (" +
            " rid STRING," +
            " r1 STRING" +
            ")with('connector' = 'kafka'," +
            " 'topic' = 'right1'," +
            " 'properties.bootstrap.servers' = 'localhost:9092'," +
            " 'properties.group.id' = 'tester'," +
            " 'format' = 'json'," +
            " 'scan.startup.mode' = 'group-offsets')");
        tableEnv.executeSql("CREATE TABLE join_x (" +
            " id STRING," +
            " l1 STRING," +
            " r1 STRING" +
            ")with('connector' = 'kafka'," +
            " 'topic' = 'join1'," +
            " 'properties.bootstrap.servers' = 'localhost:9092'," +
            " 'properties.group.id' = 'tester'," +
            " 'format' = 'json'," +
            " 'scan.startup.mode' = 'group-offsets')");
        tableEnv.executeSql(
            "insert into join_x select left1.lid,left1.l1,right1.r1 from left1 inner join right1 on left1.lid=right1.rid"
        );
    }
}
