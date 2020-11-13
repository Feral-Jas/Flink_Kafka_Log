package operator;

import model.DmJdbc;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public class BatchSourceFunction implements SourceFunction<String> {
    private static Connection connection;
    private volatile boolean isRunning = true;
    private static Timestamp lastTime;
    @Override
    public void run(SourceContext<String> sourceContext) throws Exception {
        lastTime = Timestamp.valueOf(LocalDateTime.now());
        DmJdbc dmInstance = DmJdbc.INSTANCE;
        connection = dmInstance.getConnection();
        while(isRunning){
            synchronized (sourceContext.getCheckpointLock()){
                PreparedStatement ps = connection.prepareStatement("select * from CSSBASE_CL.S_LOG t where t.TIMESTAMP >=? and t.TIMESTAMP <=?");
                LocalDateTime now = LocalDateTime.now();
                ps.setTimestamp(1,Timestamp.valueOf(now.minusSeconds(1)));
                ps.setTimestamp(2,Timestamp.valueOf(now));
                ResultSet resultSet = ps.executeQuery();
                while(resultSet.next()){
                    String uuid = resultSet.getString(1);
                    StringBuilder sb = new StringBuilder();
                    sb.append(uuid);
                    sb.append(",");
                    sb.append(resultSet.getString(2));
                    sb.append(",");
                    sb.append(resultSet.getString(3));
                    sb.append(",");
                    Timestamp resTime = resultSet.getTimestamp(4);
                    sb.append(resTime);
                    sourceContext.collect(sb.toString());
                    lastTime = resTime;
                }
                ps.close();
                Thread.sleep(1000);
            }
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }


}
