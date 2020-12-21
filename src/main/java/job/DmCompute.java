package job;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcInputFormat;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;

/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public class DmCompute {
    public static void exec(StreamExecutionEnvironment env) throws Exception {
        DataStreamSource<Row> cssbase = env.createInput(
            JdbcInputFormat.buildJdbcInputFormat()
                .setDBUrl("jdbc:dm://10.15.0.173:5236/CSSBASE")
                .setDrivername("dm.jdbc.driver.DmDriver")
                .setUsername("CSSBASE")
                .setPassword("1234567890")
                .setQuery("select * from CSSBASE_CL.CACHE_LOG_IN")
                .setRowTypeInfo(
                    new RowTypeInfo(
                        BasicTypeInfo.STRING_TYPE_INFO,
                        BasicTypeInfo.STRING_TYPE_INFO,
                        BasicTypeInfo.STRING_TYPE_INFO,
                        BasicTypeInfo.STRING_TYPE_INFO,
                        BasicTypeInfo.STRING_TYPE_INFO,
                        BasicTypeInfo.DATE_TYPE_INFO
                        )
                )
                .finish()
        );
        cssbase.addSink(
            JdbcSink
                .sink(
                    "INSERT INTO CSSBASE_CL.CACHE_LOG_IN(UUID,CLASSNAME) VALUES (?,?)",
                    (ps, row) -> {
                        ps.setString(1,"tiktok");
                        ps.setString(2,"douyin");
                    },
                    new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("jdbc:dm://10.15.0.173:5236/CSSBASE")
                        .withDriverName("dm.jdbc.driver.DmDriver")
                        .withUsername("CSSBASE")
                        .withPassword("1234567890")
                        .build()
                )
        );
    }
}
