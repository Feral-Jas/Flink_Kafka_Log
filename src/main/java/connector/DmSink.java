package connector;

import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author liuchenyu
 * @date 2020/11/13
 */
public class DmSink {
    public static SinkFunction<String> sink(){
        return JdbcSink
            .sink(
                "INSERT INTO CSSBASE_CL.S_LOG VALUES (?,?,?,?)",
                (ps, row) -> {
                    ps.setString(1, String.valueOf(UUID.randomUUID()));
                    ps.setString(2,"login.action");
                    ps.setString(3,"系统管理员");
                    ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                },
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                    .withUrl("jdbc:dm://10.15.0.173:5236/CSSBASE")
                    .withDriverName("dm.jdbc.driver.DmDriver")
                    .withUsername("CSSBASE")
                    .withPassword("1234567890")
                    .build()
            );
    }
}
